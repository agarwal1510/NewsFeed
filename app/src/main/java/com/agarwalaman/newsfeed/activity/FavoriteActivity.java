package com.agarwalaman.newsfeed.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.agarwalaman.newsfeed.R;
import com.agarwalaman.newsfeed.adapter.NewsAdapter;
import com.agarwalaman.newsfeed.listener.ClickListener;
import com.agarwalaman.newsfeed.model.NewsListItem;
import com.agarwalaman.newsfeed.util.Constants;
import com.agarwalaman.newsfeed.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private List<NewsListItem> favoriteList;
    private List<String> favoriteIds;
    private List<NewsListItem> parentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
        setSupportActionBar(toolbar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        favoriteList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        parentList = Utils.getArticles(this);
        favoriteIds = Utils.getFavorites(this);
        if (favoriteIds != null && favoriteIds.size() > 0) {
            for (NewsListItem item : parentList) {
                if (favoriteIds.contains(item.getId())) {
                    favoriteList.add(item);
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.no_fav_articles), Toast.LENGTH_SHORT).show();
        }
        mAdapter = new NewsAdapter(favoriteList, favoriteIds, new ClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(FavoriteActivity.this, WebViewActivity.class);
                intent.putExtra(Constants.URL, favoriteList.get(position).getContent().getUrl());
                startActivity(intent);
            }

            @Override
            public void onFavorite(int position, String id) {
                Utils.setFavorites(FavoriteActivity.this, id);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }
}
