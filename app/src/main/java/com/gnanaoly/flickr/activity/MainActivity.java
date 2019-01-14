package com.gnanaoly.flickr.activity;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gnanaoly.flickr.R;
import com.gnanaoly.flickr.adapter.FlickrRecycleViewAdapter;
import com.gnanaoly.flickr.async.GetFlickrJsonData;
import com.gnanaoly.flickr.async.GetFlickrJsonDataPublic;
import com.gnanaoly.flickr.listner.OnDataAvailable;
import com.gnanaoly.flickr.model.FlickrPhotoDetail;
import com.gnanaoly.flickr.model.PagingDetail;
import com.gnanaoly.flickr.model.PhotoData;
import com.gnanaoly.flickr.listner.RecyclerItemClickListener;
import com.gnanaoly.flickr.constant.DownloadStatus;
import com.gnanaoly.flickr.constant.PhotoScopeType;
import com.gnanaoly.flickr.util.AppLog;
import com.gnanaoly.flickr.util.DeviceUtils;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {

    private static final String TAG = "MainActivity";

    private FlickrRecycleViewAdapter recycleViewAdapter;

    PhotoScopeType type = PhotoScopeType.PRIVATE;

    int GRID_COLUM = 3;
    RecyclerView recyclerView;
    boolean isLoading = false;


    PagingDetail pagingDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();

    }

    public void initView() {

        type = PhotoScopeType.PRIVATE;

        activateToolbar(false);

        FloatingActionButton refresh = findViewById(R.id.fab);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppLog.logDebug(TAG, "onClick: refresh called");

                pagingDetail = null;

                if (!isLoading) {

                    onRefresh();

                } else {

                    Toast.makeText(MainActivity.this, getString(R.string.data_is_loading), Toast.LENGTH_SHORT).show();
                }

            }
        });

        recyclerView = findViewById(R.id.recycler_view);


        GridLayoutManager manager = new GridLayoutManager(this, GRID_COLUM);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(manager);


        int width = (int) (DeviceUtils.getScreenWidth(this) - DeviceUtils.convertDpToPixel(20, this));


        int height = width / GRID_COLUM;


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        recycleViewAdapter = new FlickrRecycleViewAdapter(new ArrayList<PhotoData>(), this);

        recycleViewAdapter.setImageHeight(height);
        recyclerView.setAdapter(recycleViewAdapter);

        initScrollListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_main, menu);

        AppLog.logDebug(TAG, "onCreateOptionsMenu: Menu created");

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search) {

            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

            return true;
        }

        if (item.getItemId() == R.id.action_settings) {

            Toast.makeText(this, "Settings is futrue update", Toast.LENGTH_SHORT).show();
//
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        pagingDetail = null;

        onRefresh();
    }


    public void onRefresh() {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryTags = sharedPreferences.getString(FLICKR_QUERY, "kittens");

        if (type == PhotoScopeType.PRIVATE) {

            getFlickerPhots(queryTags);

        } else {

            getFlickerPublicAccessPhots(queryTags);
        }


    }


    public void getFlickerPublicAccessPhots(String queryTags) {

        AppLog.logDebug(TAG, "onResume: start");

        GetFlickrJsonDataPublic flickrJsonData = new GetFlickrJsonDataPublic("en-us", true, this);

        if (queryTags.length() > 0) {
            flickrJsonData.execute(queryTags);
        }

        AppLog.logDebug(TAG, "onResume: ends");
    }

    public void getFlickerPhots(String queryTags) {

        AppLog.logDebug(TAG, "onResume: start");

        int page = 0;

        if (pagingDetail != null) {

            page = pagingDetail.getPage() + 1;
        }

        GetFlickrJsonData flickrJsonData = new GetFlickrJsonData(page, "en-us", true, this);

        if (queryTags.length() > 0) {
            flickrJsonData.execute(queryTags);
        }
        AppLog.logDebug(TAG, "onResume: ends");
    }

    @Override
    public void onDataAvailable(PagingDetail pagingDetail, ArrayList data, DownloadStatus status) {

        if (status == DownloadStatus.OK) {

            this.pagingDetail=pagingDetail;

            if (pagingDetail != null) {

                if (pagingDetail.getPage() > 1) {

                    recycleViewAdapter.loadMoreData(data);

                } else {

                    recycleViewAdapter.loadNewData(data);
                }

            } else {

                recycleViewAdapter.loadNewData(data);
            }


        } else {

            AppLog.logError(TAG, "onDataAvailable: download failed with status: " + status);

        }


        isLoading = false;
    }

    @Override
    public void OnItemClickListener(View view, int position) {

    }

    @Override
    public void OnItemLongClickListener(View view, int position) {

        Intent intent = new Intent(this, PhotoDetailActivity.class);

        if (type == PhotoScopeType.PRIVATE) {

            intent.putExtra(PHOTO_TRANSFER, (FlickrPhotoDetail) recycleViewAdapter.getPhotoData(position));
            intent.putExtra(PHOTO_TRANSFER_IS_PUBLIC, false);

        } else {

            intent.putExtra(PHOTO_TRANSFER, (PhotoData) recycleViewAdapter.getPhotoData(position));
            intent.putExtra(PHOTO_TRANSFER_IS_PUBLIC, true);
        }

        startActivity(intent);
    }


    private void initScrollListener() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                System.out.println(" Load more isLoading " + isLoading);


                if (!isLoading) {


                    if (dy > 0) {

                        int visibleItemCount = linearLayoutManager.getChildCount();
                        int totalItemCount = linearLayoutManager.getItemCount();
                        int lastVisiblesItems = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                        AppLog.logDebug(TAG," >>>>>> " + visibleItemCount + " : " + totalItemCount + " : " + lastVisiblesItems);


                        if (totalItemCount > 10 && lastVisiblesItems >= (totalItemCount - 10)) {
                            //bottom of list!

                            AppLog.logDebug(TAG,">>>>>> Load more call ");

                            loadMore();

                            isLoading = true;

                        } else {

                            AppLog.logDebug(TAG,">>>>>> Load more call condition fail ");
                        }
                    }
                }else{

                    AppLog.logDebug(TAG,">>>>>> Load more is under process ");
                }

            }
        });


    }


    private void loadMore() {


        onRefresh();


    }


}

