package com.gnanaoly.flickr.adapter.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gnanaoly.flickr.R;
import com.gnanaoly.flickr.loader.ImageLoader;
import com.gnanaoly.flickr.model.FlickrPhotoDetail;


public class UberImageViewHolder extends ChildViewHolder {

    private static final String TAG = UberImageViewHolder.class.getName();

    ImageView thumbnail = null;
    TextView title =null;

    public UberImageViewHolder(View itemView) {

        super(itemView);

        this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        this.title = (TextView) itemView.findViewById(R.id.title);
    }

    public void bind(Context context, FlickrPhotoDetail photoItem, int position){


        if(photoItem==null){

            thumbnail.setImageResource(R.drawable.placeholder);
            title.setText(R.string.error404);

        }else {


            ImageLoader imageLoader=new ImageLoader(context);
            imageLoader.DisplayImage(photoItem.toLink(),thumbnail);

//            Glide.with(context)
//                    .load(photoItem.toLink())
//                    .apply(new RequestOptions()
//                            .placeholder(R.drawable.placeholder)
//                            .optionalCenterCrop()
//                    )
//                    .into(thumbnail);
            title.setText(photoItem.getTitle());
        }
    }
}
