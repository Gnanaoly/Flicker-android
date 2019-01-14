package com.gnanaoly.flickr.listner;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.gnanaoly.flickr.util.AppLog;

public class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {

   private static final String TAG = "RecyclerItemClickListen";

   public  interface OnRecyclerClickListener{

        void OnItemClickListener(View view, int position);
        void OnItemLongClickListener(View view, int position);

    }

    private final OnRecyclerClickListener mlistener;
    private final GestureDetectorCompat mgestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnRecyclerClickListener listener) {

        this.mlistener = listener;

        this.mgestureDetector = new GestureDetectorCompat(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                AppLog.logDebug(TAG, "onSingleTapUp: starts");
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());

                if(childView != null && mlistener != null){

                    AppLog.logDebug(TAG, "onSingleTapUp: calling listener: listener.onItemClick");
                    mlistener.OnItemClickListener(childView,recyclerView.getChildAdapterPosition(childView));

                }

                return true;

            }

            @Override
            public void onLongPress(MotionEvent e) {

                AppLog.logDebug(TAG, "onLongPress: starts");
//                super.onLongPress(e);
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());

                if(childView != null && childView != null){

                    AppLog.logDebug(TAG, "onLongPress: calling listener: listener.onItemLongClick");
                    mlistener.OnItemLongClickListener(childView,recyclerView.getChildAdapterPosition(childView));
                }

            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        AppLog.logDebug(TAG, "onInterceptTouchEvent: starts");

        if(mgestureDetector!=null){

            boolean result= mgestureDetector.onTouchEvent(e);
            AppLog.logDebug(TAG, "onInterceptTouchEvent: returned: "+result);

            return result;

        }else {

            AppLog.logDebug(TAG, "onInterceptTouchEvent: returned: false");
            return false;
        }

    }
}
