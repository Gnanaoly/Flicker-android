package com.gnanaoly.flickr.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.gnanaoly.flickr.adapter.view.ChildViewHolder;

public abstract class FlickerBindAdapter<CVH extends ChildViewHolder>
        extends RecyclerView.Adapter {


    static final int VIEW_TYPE_PUBLIC = 1;
    static final int VIEW_TYPE_PRIVATE = 2;
    static final int VIEW_TYPE_LOADING = 3;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CVH cvh = onCreateChildViewHolder(parent, viewType);

        return cvh;

    }

    public abstract CVH onCreateChildViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public int getItemViewType(int position) {
        return getChildViewType(position);
    }

    protected abstract int getChildViewType(int position);


}
