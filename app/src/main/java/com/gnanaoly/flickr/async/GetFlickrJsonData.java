package com.gnanaoly.flickr.async;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.gnanaoly.flickr.model.PagingDetail;
import com.gnanaoly.flickr.util.AppLog;
import com.gnanaoly.flickr.listner.OnDataAvailable;
import com.gnanaoly.flickr.constant.DownloadStatus;
import com.gnanaoly.flickr.model.FlickrPhotoDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetFlickrJsonData extends AsyncTask<String, Void, ArrayList<FlickrPhotoDetail>> implements RawJsonData.OnDownloadComplete {


    private static final String baseURL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1&safe_search=1";

    private static final String TAG = "GetFlickrJsonDataPri";

    private ArrayList photoDataArrayList;

    private String lang = "en-us";
    private boolean MatchAll = true;
    private final OnDataAvailable Callback;
    private boolean runningOnSameThread = false;
    private int pageNumber = 1;

    PagingDetail pagingDetail;

    public GetFlickrJsonData(int pageNumber, String lang, Boolean matchAll, OnDataAvailable callback) {

        AppLog.logDebug(TAG, "GetFlickrJsonDataPublic: called");

        this.lang = lang;
        MatchAll = matchAll;
        Callback = callback;
        this.pageNumber = pageNumber;
    }


    @Override
    protected void onPostExecute(ArrayList<FlickrPhotoDetail> photoData) {

        AppLog.logDebug(TAG, "onPostExecute: starts");

        if (Callback != null) {
            Callback.onDataAvailable(pagingDetail, photoData, DownloadStatus.OK);
        }

        AppLog.logDebug(TAG, "onPostExecute: ends");
    }


    void executeOnSameThread(String searchCriteria) {
        AppLog.logDebug(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria, MatchAll, lang);
        RawJsonData rawJsonData = new RawJsonData(this);
        rawJsonData.execute(destinationUri);

        AppLog.logDebug(TAG, "executeOnSameThread: ends ");
    }

    @Override
    protected ArrayList<FlickrPhotoDetail> doInBackground(String... params) {

        AppLog.logDebug(TAG, "doInBackground: starts");

        String destinationUri = createUri(params[0], MatchAll, lang);

        AppLog.logDebug(TAG, "doInBackground: destinationUri "+destinationUri);


        RawJsonData jsonData = new RawJsonData(this);

        jsonData.runOnSameThread(destinationUri);

        AppLog.logDebug(TAG, "doInBackground: ends");

        return photoDataArrayList;
    }

    private String createUri(String searchCriteria, Boolean matchAll, String lang) {

        AppLog.logDebug(TAG, "createUri: starts");

        return Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("text", searchCriteria)
                .appendQueryParameter("api_key", "3e7cc266ae2b0e0d78e279ce8e361736")
                .appendQueryParameter("page", "" + pageNumber).build().toString();


    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {

        if (status == DownloadStatus.OK) {

            AppLog.logDebug(TAG, "onDownloadComplete: starts status:" + status + "  : " + data);
            photoDataArrayList = new ArrayList<>();

            try {

                JSONObject jsonObject = new JSONObject(data);

                JSONObject photosObject = jsonObject.getJSONObject("photos");


                try {

                    int page = photosObject.getInt("page");
                    int pages = photosObject.getInt("pages");
                    int perpage = photosObject.getInt("perpage");
                    String total = photosObject.getString("total");

                    pagingDetail = new PagingDetail();
                    pagingDetail.setPage(page);
                    pagingDetail.setPages(pages);
                    pagingDetail.setPerpage(perpage);
                    pagingDetail.setTotal(total);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray itemsData = photosObject.getJSONArray("photo");

                for (int i = 0; i < itemsData.length(); i++) {

                    JSONObject item = itemsData.getJSONObject(i);

                    String id = item.getString("id");
                    String owner = item.getString("owner");
                    String secret = item.getString("secret");
                    String server = item.getString("server");

                    int farm = item.getInt("farm");


                    String title = item.getString("title");
                    int isPublic = item.getInt("ispublic");
                    int isFriend = item.getInt("isfriend");
                    int isFamily = item.getInt("isfamily");


                    FlickrPhotoDetail photo = new FlickrPhotoDetail(id, owner, secret, server, farm, title, isPublic == 1, isFriend == 1, isFamily == 1);

                    photoDataArrayList.add(photo);

                }


            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: unable yo parse json: " + e.getMessage());
            }

        }


        if (runningOnSameThread && Callback != null) {
            //error
            AppLog.logDebug(TAG, "onDownloadComplete: running on same thread: " + runningOnSameThread);
            Callback.onDataAvailable(pagingDetail, photoDataArrayList, status);

        } else {
            //Download Failed
            Log.e(TAG, "onDownloadComplete: Download Failed with status: " + status);
        }
    }
}
