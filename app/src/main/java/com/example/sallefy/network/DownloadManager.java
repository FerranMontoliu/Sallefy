package com.example.sallefy.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sallefy.network.callback.DownloadCallback;

import java.net.URI;
import java.net.URL;

import javax.inject.Singleton;

@Singleton
public class DownloadManager {

    private static DownloadManager downloadManager;

    public static DownloadManager getInstance(){
        if(downloadManager == null){
            downloadManager = new DownloadManager();
        }
        return downloadManager;
    }

    // PLAYBACK ENDPOINT
    public synchronized void getFile(String url, Context context, DownloadCallback callback){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onDownloaded(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(new Throwable(String.valueOf(error)));
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
