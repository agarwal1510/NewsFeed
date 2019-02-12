package com.agarwalaman.newsfeed.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.agarwalaman.newsfeed.model.NewsFeed;
import com.agarwalaman.newsfeed.model.NewsListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Utils {


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static List<String> getFavorites(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        String favKeys = sharedPreferences.getString(Constants.FAV_KEY, null);
        List<String> res = new Gson().fromJson(favKeys, new TypeToken<List<String>>(){}.getType());
        return res;
    }

    public static void setFavorites(Context context, String id) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        String favKeys = sharedPreferences.getString(Constants.FAV_KEY, null);
        List<String> res = gson.fromJson(favKeys, new TypeToken<List<String>>(){}.getType());
        if (res == null) {
            res = new ArrayList<>();
        }
        if (res.contains(id)) {
            res.remove(id);
        } else {
            res.add(id);
        }
        sharedPreferences.edit().putString(Constants.FAV_KEY,  gson.toJson(res)).commit();
    }

    public static List<NewsListItem> getArticles(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        String articles = sharedPreferences.getString(Constants.ARTICLE_KEY, null);
        NewsFeed newsFeed = new Gson().fromJson(articles, NewsFeed.class);
        return newsFeed.getResult();
    }

    public static void setArticles(Context context, NewsFeed newsFeed) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Constants.ARTICLE_KEY,  gson.toJson(newsFeed)).commit();
    }

}
