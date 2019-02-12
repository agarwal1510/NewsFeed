package com.agarwalaman.newsfeed.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.agarwalaman.newsfeed.R;
import com.agarwalaman.newsfeed.adapter.NewsAdapter;
import com.agarwalaman.newsfeed.listener.ClickListener;
import com.agarwalaman.newsfeed.model.Article;
import com.agarwalaman.newsfeed.model.NewsFeed;
import com.agarwalaman.newsfeed.model.NewsListItem;
import com.agarwalaman.newsfeed.service.NewsSyncService;
import com.agarwalaman.newsfeed.util.Constants;
import com.agarwalaman.newsfeed.util.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private NewsFeed newsFeed;
    private List<NewsListItem> newsListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
        setSupportActionBar(toolbar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter == null) {
            if (Utils.isNetworkAvailable(this)) {
                new NewsSyncTask(this).execute();
            } else {
                Toast.makeText(this, getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            }
        } else {
            mAdapter.setFavArticles(Utils.getFavorites(this));
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        //searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mAdapter.setNewsList(newsListItems);
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favorite:
                openFavorites();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openFavorites() {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query != null && !query.isEmpty()) {
            if (newsListItems != null) {
                List<NewsListItem> filterList = new ArrayList<>();
                for (NewsListItem item : newsListItems) {
                    Article article = item.getContent();
                    if (article.getTitle().toLowerCase().contains(query.toLowerCase()) || article.getSummary().toLowerCase().contains(query.toLowerCase())) {
                        filterList.add(item);
                    }
                }
                mAdapter.setNewsList(filterList);
                mAdapter.notifyDataSetChanged();
            }
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText != null && newText.isEmpty()) {
            mAdapter.setNewsList(newsListItems);
            mAdapter.notifyDataSetChanged();
        }
        return false;
    }

    private static class NewsSyncTask extends AsyncTask<Void, Void, NewsFeed> {

        private final WeakReference<MainActivity> mainActivityWeakReference;
        private final ProgressDialog dialog;

        private NewsSyncTask(MainActivity activity) {
            this.mainActivityWeakReference = new WeakReference<>(activity);
            this.dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage(mainActivityWeakReference.get().getString(R.string.news_load_message));
            this.dialog.show();
        }

        @Override
        protected NewsFeed doInBackground(Void... params) {
            return NewsSyncService.getNewsFeed(mainActivityWeakReference.get());
        }

        @Override
        protected void onPostExecute(final NewsFeed newsFeed) {
            super.onPostExecute(newsFeed);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            mainActivityWeakReference.get().newsListItems = newsFeed.getResult();
            mainActivityWeakReference.get().mAdapter = new NewsAdapter(mainActivityWeakReference.get().newsListItems, Utils.getFavorites(mainActivityWeakReference.get()), new ClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(mainActivityWeakReference.get(), WebViewActivity.class);
                    intent.putExtra(Constants.URL, newsFeed.getResult().get(position).getContent().getUrl());
                    mainActivityWeakReference.get().startActivity(intent);
                }

                @Override
                public void onFavorite(int position, String id) {
                    Utils.setFavorites(mainActivityWeakReference.get(), id);
                }
            });
            mainActivityWeakReference.get().mRecyclerView.setAdapter(mainActivityWeakReference.get().mAdapter);
        }
    }
}
