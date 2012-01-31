package com.activequant.utils.env;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.Tuple;
import com.activequant.utils.FileUtils;

public class AutoUpdater {

    private final String appFolder;
    private final String tempFolder;
    private final String remoteLocation;
    private Logger log = Logger.getLogger(AutoUpdater.class);

    public AutoUpdater(String appFolder, String tmpFolder, String remoteLocation) throws Exception {
        this.appFolder = appFolder;
        this.tempFolder = tmpFolder;
        this.remoteLocation = remoteLocation;

        // have to switch version?
        changeVersion();
    }

    public void changeVersion() throws Exception {
        log.info("Checking if a new version has been downloaded. ");
        // clean up former backup.
        File backupFile = new File(appFolder + ".backup");
        File appFolderFile = new File(appFolder);
        File tempFolderFile = new File(tempFolder);

        // immediately make a lock file.
        File versionFile = new File(tempFolder + File.separator + ".version");

        //
        if (!(appFolderFile.exists() && tempFolderFile.exists() && versionFile.exists())) {
            log.info("No new version, continuing as usual. ");
            return;
        }

        log.info("Changing to new version.");
        //
        if (backupFile.exists())
            FileUtils.deleteDir(backupFile);
        //
        appFolderFile.renameTo(backupFile);
        tempFolderFile.renameTo(appFolderFile);
    }

    public void syncInBackground() {
        log.info("Starting AutoUpdater to update in background. ");
        File tempFolderFile = new File(tempFolder);

        try {
            //
            File lockFile = new File(tempFolder + File.separator + ".lock");
            File versionFile = new File(tempFolder + File.separator + ".version");
            if (lockFile.exists()) {
                log.warn("Lock file for auto-updater exists. Unless another app is running, this might be due to an incomplete update. If this is the case, please delete the entire folder "
                        + tempFolder + ".");
                return;
            }

            FileUtils.deleteDir(tempFolderFile);
            if (!tempFolderFile.exists())
                tempFolderFile.mkdir();
            //
            lockFile.createNewFile();
            //
            // here we check for a new version.
            //
            List<Tuple<String, String>> files = loadFileList(this.remoteLocation);
            boolean mismatch = false;
            for (Tuple<String, String> file : files) {
                log.info("Checking file: " + file.getA());                
                String fileName = appFolder + File.separator + file.getA();
                if (new File(fileName).exists()) {
                    String md5 = md5(fileName);
                    if (!md5.equals(file.getB()))
                        mismatch = true;
                } else {
                    mismatch = true;
                }
                if (mismatch) {
                    log.info("Mismatch detected.");
                    break; 
                } 
            }
            
            if(mismatch){
                // ok, download all files.
                for (Tuple<String, String> file : files) {
                    // download all files into temp folder. 
                    downloadFile(remoteLocation+"/"+file.getA(), file.getA(), tempFolder);
                    // 
                }
                // all good, rename lock to new version file.
                lockFile.renameTo(versionFile);
                log.info("AutoUpdater updated application in background. Latest version will be used on next restart.");
            }
            else{
                // cleanup. 
                FileUtils.deleteDir(tempFolderFile);
                log.info("No new version available.");
            }
            
        } catch (Exception ex) {
            log.warn("Exception while auto-updating in background: "+ ex.getStackTrace()[0]);
            FileUtils.deleteDir(tempFolderFile);
        }
    }

    public void downloadFile(String fAddress, String localFileName, String destinationDir) throws Exception {
        OutputStream outStream = null;
        URLConnection uCon = null;

        InputStream is = null;
        try {
            URL Url;
            byte[] buf;
            int ByteRead, ByteWritten = 0;
            Url = new URL(fAddress);
            outStream = new BufferedOutputStream(new FileOutputStream(destinationDir + File.separator + localFileName));

            uCon = Url.openConnection();
            is = uCon.getInputStream();
            buf = new byte[1024];
            while ((ByteRead = is.read(buf)) != -1) {
                outStream.write(buf, 0, ByteRead);
                outStream.flush();
                ByteWritten += ByteRead;
            }
            log.info("Downloaded Successfully.");
            log.info("File name:\"" + localFileName + "\". No of bytes :" + ByteWritten);
        } catch (Exception e) {
            throw e; 
        } finally {
            try {
                is.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Tuple<String, String>> loadFileList(String remoteLocation) throws Exception {
        log.info("Loading file list from " + remoteLocation);
        URL url = new URL(remoteLocation + "/files.txt");
        InputStream in = url.openStream();
        log.info("Stream opened.");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String l = br.readLine();
        List<Tuple<String, String>> ret = new ArrayList<Tuple<String, String>>();
        while (l != null) {
            //
            String[] parts = l.split(";");
            String fileName = parts[0];
            String md5 = parts[1];
            Tuple<String, String> pair = new Tuple<String, String>(fileName, md5);
            ret.add(pair);
            //
            l = br.readLine();
        }
        log.info("Returning.");
        return ret;

    }

    public String md5(String file) throws Exception {
        String output = "";
        MessageDigest digest = MessageDigest.getInstance("MD5");
        File f = new File(file);
        InputStream is = new FileInputStream(f);
        byte[] buffer = new byte[8192];
        int read = 0;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            output = bigInt.toString(16);

        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
            }
        }
        return output;
    }

    public static void main(String[] args) throws Exception {
        new AutoUpdater("./sample", "./tmp", "http://www.activequant.org/distro/a1").syncInBackground();

    }

}
