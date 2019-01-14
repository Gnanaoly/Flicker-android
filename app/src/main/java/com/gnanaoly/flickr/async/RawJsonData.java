package com.gnanaoly.flickr.async;

import android.os.AsyncTask;
import android.util.Log;

import com.gnanaoly.flickr.constant.DownloadStatus;
import com.gnanaoly.flickr.util.AppLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class RawJsonData extends AsyncTask<String, Void, String> {

    private static final String TAG = "RawJsonData";

    private final OnDownloadComplete Callback;
    private DownloadStatus downloadStatus;

    public interface OnDownloadComplete {

        void onDownloadComplete(String data, DownloadStatus status);

    }

    public RawJsonData(OnDownloadComplete callback) {

        downloadStatus = DownloadStatus.IDLE;
        Callback = callback;
    }

    public void runOnSameThread(String s) {


        if (Callback != null) {

            Callback.onDownloadComplete(doInBackground(s), downloadStatus);
        }


    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);

        if (Callback != null) {

            Callback.onDownloadComplete(s, downloadStatus);
        }


    }


    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection feedloader = null;
        BufferedReader bufferedReader = null;

        if (strings == null) {

            downloadStatus = DownloadStatus.NOT_INITIALIZED;

            return null;
        }

        try {

            downloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);
            feedloader = (HttpURLConnection) url.openConnection();
            feedloader.setRequestMethod("GET");
            feedloader.connect();
            int response = feedloader.getResponseCode();
            
            AppLog.logDebug(TAG, "doInBackground: The response was: " + response);
            
            StringBuilder builder = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(feedloader.getInputStream()));
            String line;

            while (null != (line = bufferedReader.readLine())) {
                builder.append(line).append("\n");
            }

            downloadStatus = DownloadStatus.OK;

            return builder.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: I/O error " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: Unknown error " + e.getMessage());
        } finally {
            if (feedloader != null) {
                feedloader.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground:  error in closing stream " + e.getMessage());
                }
            }
        }
        downloadStatus = DownloadStatus.FAILED_OR_EMPTY;

        return null;
    }

}
