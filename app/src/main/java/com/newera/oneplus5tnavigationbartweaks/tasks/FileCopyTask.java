package com.newera.oneplus5tnavigationbartweaks.tasks;

import android.os.AsyncTask;

import com.newera.oneplus5tnavigationbartweaks.listener.OnFileCopyListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileCopyTask extends AsyncTask<Void, Void, Void> {
    private File src, dest;
    private OnFileCopyListener onFileCopyListener;
    private boolean result;

    public FileCopyTask(File src, File dest, OnFileCopyListener onFileCopyListener) {
        this.src = src;
        this.dest = dest;
        this.onFileCopyListener = onFileCopyListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        result = copy(src, dest);
        return null;
    }

    @Override
    protected void onPostExecute(Void o) {
        if (result)
            onFileCopyListener.onSuccess();
        else
            onFileCopyListener.onFailed();
    }

    public static boolean copy(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }


}
