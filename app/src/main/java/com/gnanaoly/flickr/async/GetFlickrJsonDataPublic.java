package com.gnanaoly.flickr.async;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.gnanaoly.flickr.model.PagingDetail;
import com.gnanaoly.flickr.util.AppLog;
import com.gnanaoly.flickr.listner.OnDataAvailable;
import com.gnanaoly.flickr.model.PhotoData;
import com.gnanaoly.flickr.constant.DownloadStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetFlickrJsonDataPublic extends AsyncTask<String, Void, ArrayList<PhotoData>> implements RawJsonData.OnDownloadComplete {



    private static final String baseURL="https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";



    private static final String TAG = "GetFlickrJsonDataPublic";

    private ArrayList photoDataArrayList;

    private String lang="en-us";
    private boolean MatchAll=true;
    private final OnDataAvailable Callback;
    private boolean runningOnSameThread = false;
    PagingDetail pagingDetail;

    public GetFlickrJsonDataPublic(String lang, Boolean matchAll, OnDataAvailable callback) {
        AppLog.logDebug(TAG, "GetFlickrJsonDataPublic: called");
        this.lang = lang;
        MatchAll = matchAll;
        Callback = callback;
    }



    @Override
    protected void onPostExecute(ArrayList<PhotoData> photoData) {
        AppLog.logDebug(TAG, "onPostExecute: starts");
        if(Callback!=null){
            Callback.onDataAvailable(pagingDetail,photoData,DownloadStatus.OK);
        }
        AppLog.logDebug(TAG, "onPostExecute: ends");
    }



    public void executeOnSameThread(String searchCriteria){
        AppLog.logDebug(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria,MatchAll,lang);
        RawJsonData rawJsonData = new RawJsonData(this);
        rawJsonData.execute(destinationUri);
        AppLog.logDebug(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected ArrayList<PhotoData> doInBackground(String... params) {
        AppLog.logDebug(TAG, "doInBackground: starts");
        String destinationUri =createUri(params[0],MatchAll,lang);
        RawJsonData jsonData = new RawJsonData(this);
        jsonData.runOnSameThread(destinationUri);
        AppLog.logDebug(TAG, "doInBackground: ends");
        return photoDataArrayList;
    }

    private String createUri(String searchCriteria, Boolean matchAll, String lang){
        AppLog.logDebug(TAG, "createUri: starts");
        return Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("text",searchCriteria).build().toString();

//                .appendQueryParameter("tagmode", matchAll ? "ALL":"ANY")
//                .appendQueryParameter("lang",lang).build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {

        if(status==DownloadStatus.OK){

            AppLog.logDebug(TAG, "onDownloadComplete: starts status:" + status+ "  : "+data);

            photoDataArrayList = new ArrayList<>();


            try{

                JSONObject jsonObject = new JSONObject(data);
                JSONArray itemsData = jsonObject.getJSONArray("items");

                for(int i=0; i< itemsData.length();i++){
                    JSONObject item = itemsData.getJSONObject(i);
                    String title = item.getString("title");
                    String author = item.getString("author");
                    String authorID = item.getString("author_id");
                    String published= item.getString("published");
                    String tags=item.getString("tags");
                    JSONObject media = item.getJSONObject("media");
                    String image = media.getString("m");
                    String dateTaken = item.getString("date_taken");
                    String link = item.getString("link");
                    String big_image_link =image.replaceFirst("_m","_b");


                PhotoData photo= new PhotoData(author,authorID,title,dateTaken,published,tags,big_image_link,image);
                photoDataArrayList.add(photo);



                }
            }catch (JSONException e){
                Log.e(TAG, "onDownloadComplete: unable yo parse json: "+ e.getMessage());
            }

        }if (runningOnSameThread && Callback!=null){
            //error
            AppLog.logDebug(TAG, "onDownloadComplete: running on same thread: "+ runningOnSameThread);
            Callback.onDataAvailable(pagingDetail,photoDataArrayList,status);

        }
        else {
            //Download Failed
            Log.e(TAG, "onDownloadComplete: Download Failed with status: "+status);
        }
    }
}
