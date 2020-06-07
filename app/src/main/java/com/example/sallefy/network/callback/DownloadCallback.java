package com.example.sallefy.network.callback;

public interface DownloadCallback extends FailureCallback {
    void onDownloaded(String data);
}
