package com.gnanaoly.flickr.adapter.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gnanaoly.flickr.loader.ImageLoader;
import com.gnanaoly.flickr.util.AppLog;
import com.gnanaoly.flickr.R;
import com.gnanaoly.flickr.model.PhotoData;

public class FlickrImageViewHolder extends ChildViewHolder {

    private static final String TAG = "FlickrImageViewHolder";

    ImageView thumbnail = null;
    TextView title =null;

    public FlickrImageViewHolder(View itemView) {

        super(itemView);

        AppLog.logDebug(TAG, "FlickrImageViewHolder: start");

        this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        this.title = (TextView) itemView.findViewById(R.id.title);
    }

    public void bind(Context context, PhotoData photoItem,int position){


        if(photoItem==null){

            thumbnail.setImageResource(R.drawable.placeholder);
            title.setText(R.string.error404);

        }else {

            AppLog.logDebug(TAG, "onBindViewHolder:" + photoItem.getTitle() + "--->" + position);

           ImageLoader imageLoader=new ImageLoader(context);
            imageLoader.DisplayImage(photoItem.getImage(),thumbnail);

//            Glide.with(context)
//                    .load(photoItem.getImage())
//                    .apply(new RequestOptions()
//                            .placeholder(R.drawable.placeholder)
//                            .optionalCenterCrop()
//                    )
//                    .into(thumbnail);


            title.setText(photoItem.getTitle());
        }
    }
}
