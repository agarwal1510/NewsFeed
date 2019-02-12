package com.agarwalaman.newsfeed.service;

import android.content.Context;
import android.util.Log;

import com.agarwalaman.newsfeed.model.NewsFeed;
import com.agarwalaman.newsfeed.util.Constants;
import com.agarwalaman.newsfeed.util.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsSyncService {

    public static NewsFeed getNewsFeed(Context context) {
        NewsFeed feed = null;
        try {
            URL url = new URL(Constants.API_ENDPOINT + Constants.GET_NEWS);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder itemJSON = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                itemJSON.append(line);
            }
            JSONObject jsonObject = new JSONObject(itemJSON.toString());
            feed = new Gson().fromJson(jsonObject.getJSONObject("items").toString(), NewsFeed.class);
            Utils.setArticles(context, feed);
        } catch (MalformedURLException e) {
            Log.e("URL Malformed: ", e.getMessage());
        } catch (IOException e) {
            Log.e("IO Exception: ", e.getMessage());
        } catch (JSONException e) {
            Log.e("JSONException: ", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
        return feed;
    }

}
