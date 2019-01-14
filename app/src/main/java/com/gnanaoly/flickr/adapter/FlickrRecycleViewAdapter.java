package com.gnanaoly.flickr.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.gnanaoly.flickr.adapter.view.LoadingViewHolder;
import com.gnanaoly.flickr.util.AppLog;
import com.gnanaoly.flickr.R;
import com.gnanaoly.flickr.adapter.view.ChildViewHolder;
import com.gnanaoly.flickr.adapter.view.FlickrImageViewHolder;
import com.gnanaoly.flickr.adapter.view.UberImageViewHolder;
import com.gnanaoly.flickr.model.FlickrPhotoDetail;
import com.gnanaoly.flickr.model.PhotoData;


import java.util.ArrayList;

public class FlickrRecycleViewAdapter extends FlickerBindAdapter {

    private static final String TAG = "FlickrRecycleViewAdapt";

    public ArrayList getPhotoDataArrayList() {
        return photoDataArrayList;
    }

    private ArrayList photoDataArrayList;
    private Context context;


    @Override
    public ChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {

            case VIEW_TYPE_PUBLIC:

                AppLog.logDebug(TAG, "onCreateViewHolder: VIEW_TYPE_PUBLIC ");

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
                return new FlickrImageViewHolder(view);

            case VIEW_TYPE_PRIVATE:

                AppLog.logDebug(TAG, "onCreateViewHolder: VIEW_TYPE_PRIVATE ");

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
                return new UberImageViewHolder(view);

            case VIEW_TYPE_LOADING:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(view);

        }


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new UberImageViewHolder(view);

    }


    @Override
    protected int getChildViewType(int position) {


        if (photoDataArrayList != null && photoDataArrayList.size() != 0 && photoDataArrayList.get(position) instanceof FlickrPhotoDetail) {

            return VIEW_TYPE_PRIVATE;

        } else if (photoDataArrayList != null && photoDataArrayList.size() != 0 && photoDataArrayList.get(position) instanceof PhotoData) {

            return VIEW_TYPE_PUBLIC;

        } else if (photoDataArrayList == null || photoDataArrayList.size() == 0) {

            return VIEW_TYPE_PUBLIC;

        } else {

            return VIEW_TYPE_LOADING;
        }


    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getChildViewType(position);

        switch (viewType) {

            case VIEW_TYPE_PUBLIC:


                if (photoDataArrayList == null || photoDataArrayList.size() == 0) {

                    ((FlickrImageViewHolder) holder).bind(context, null, position);

                } else {

                    ((FlickrImageViewHolder) holder).bind(context, (PhotoData) photoDataArrayList.get(position), position);
                }


                return;

            case VIEW_TYPE_PRIVATE:

                ((UberImageViewHolder) holder).bind(context, (FlickrPhotoDetail) photoDataArrayList.get(position), position);
                return;

            case VIEW_TYPE_LOADING:


                return;
        }

    }

    @Override
    public int getItemCount() {


        return (((photoDataArrayList != null) && (photoDataArrayList.size() != 0)) ? photoDataArrayList.size() : 1);
    }

    public void loadNewData(ArrayList newphotoData) {

        AppLog.logDebug(TAG, "loadNewData: new data requested");

        photoDataArrayList = newphotoData;
        notifyDataSetChanged();
    }

    public void loadMoreData(ArrayList newphotoData) {

        AppLog.logDebug(TAG, "loadNewData: new data requested");

        if(newphotoData!=null)
        photoDataArrayList.addAll(newphotoData) ;

        notifyDataSetChanged();
    }

    public Object getPhotoData(int position) {

        // returns the photo data of the picture that is being tapped using its index number
        return ((photoDataArrayList != null) && (photoDataArrayList.size() > 0) ? photoDataArrayList.get(position) : null);
    }

    public FlickrRecycleViewAdapter(ArrayList PhotoDataArrayList, Context Context) {
        photoDataArrayList = PhotoDataArrayList;
        context = Context;
    }

    public void setImageHeight(int height) {

    }
}
