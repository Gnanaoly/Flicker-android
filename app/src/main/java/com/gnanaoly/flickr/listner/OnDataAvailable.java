package com.gnanaoly.flickr.listner;

import com.gnanaoly.flickr.constant.DownloadStatus;
import com.gnanaoly.flickr.model.PagingDetail;


import java.util.ArrayList;

public interface OnDataAvailable{

    void onDataAvailable(PagingDetail pagingDetail, ArrayList data, DownloadStatus status);

}