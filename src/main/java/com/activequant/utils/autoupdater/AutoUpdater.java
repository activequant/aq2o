package com.activequant.utils.autoupdater;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.activequant.utils.FileUtils;

public class AutoUpdater {

    private final String appFolder;
    private final String tempFolder;
    private Logger log = Logger.getLogger(AutoUpdater.class);

    public AutoUpdater(String appFolder, String tmpFolder, String remoteLocation) {
        this.appFolder = appFolder;
        this.tempFolder = tmpFolder;
    }

    public void syncInBackground() throws Exception {
        
        File appFolderFile = new File(appFolder);
        File tempFolderFile = new File(tempFolder);
        
        // immediately make a lock file.
        File lockFile = new File(tempFolder + File.separator + ".lock");
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
        
        // 
        Thread.sleep(10000);
        // clean up former backup.
        File backupFile = new File(appFolder+".backup");
        if(backupFile.exists())
            FileUtils.deleteDir(backupFile);
        
        //        
        appFolderFile.renameTo(backupFile);
        tempFolderFile.renameTo(appFolderFile);       
    }
    
    public static void main(String[] args) throws Exception{
        new AutoUpdater("./sample", "./tmp", "http://").syncInBackground();
        
    }

}
