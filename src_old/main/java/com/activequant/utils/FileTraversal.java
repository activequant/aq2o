package com.activequant.utils;

import java.io.File;
import java.io.IOException;

/**
 * copy paste from : http://vafer.org/blog/20071112204524/
 * 
 * @author Torsten Curdt
 * 
 */
public class FileTraversal {
    public final void traverse(final File f) throws IOException {
        if (f.isDirectory()) {
            onDirectory(f);
            final File[] childs = f.listFiles();
            for (File child : childs) {
                traverse(child);
            }
            return;
        }
        onFile(f);
    }

    public void onDirectory(final File d) {
    }

    public void onFile(final File f) {
    }
}
