package com.example.sallefy.network;

import android.os.AsyncTask;
import android.os.Environment;

import com.example.sallefy.network.callback.DownloadCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFileAsync extends AsyncTask<String, String, String> {

    private DownloadCallback callback;
    private long idTrack;

    public DownloadFileAsync(DownloadCallback callback, long idTrack){
        this.callback = callback;
        this.idTrack = idTrack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... aurl) {
        int count;
        try {
            URL url = new URL(aurl[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            int lenghtOfFile = connection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream());
            String path = new File("").getAbsolutePath();
            String[] urlSplit = url.getPath().split("/");
            File trackFile = new File(Environment.getExternalStorageDirectory(), "/" + urlSplit[urlSplit.length - 1]);
            OutputStream output = new FileOutputStream(trackFile);
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress(""+(int)((total*100)/lenghtOfFile));
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            callback.onDownloaded();

        } catch (Exception e) {
            callback.onFailure(new Throwable(e));
        }
        return null;
    }

    protected void onProgressUpdate(String... progress) {}

    @Override
    protected void onPostExecute(String unused) {}

}