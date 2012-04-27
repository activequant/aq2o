/****

    activequant - activestocks.eu

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

	
	contact  : contact@activestocks.eu
    homepage : http://www.activestocks.eu

 ****/
package com.activequant.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @TODO desc<br>
 * <br>
 *       <b>History:</b><br>
 *       - [30.09.2007] Created (Erik Nijkamp)<br>
 * 
 * 
 * @author Erik Nijkamp
 */
public class FileUtils {

    public static void copy(String source, String dest) throws IOException {
        FileChannel in = null, out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();

            long size = in.size();
            MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
            out.write(buf);
        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }

    public static void move(String source, String dest) throws IOException {
        copy(source, dest);
        new File(source).delete();
    }

    public static String check(String dir) {
        mkdir(dir);
        return appendSlash(dir);
    }

    public static void mkdir(String dir) {
        if (!new File(dir).exists()) {
            new File(dir).mkdirs();
        }
    }

    public static String appendSlash(String dir) {
        return dir.endsWith(File.separator) ? dir : dir + File.separator;
    }

    public static String[] readLines(String fileName) throws IOException {
        return readLines(new FileInputStream(fileName));
    }

    public static String[] readLines(InputStream in) throws IOException {
        List<String> r = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String l = br.readLine();
        while (l != null) {
            r.add(l);
            l = br.readLine();
        }
        return r.toArray(new String[] {});
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

}
