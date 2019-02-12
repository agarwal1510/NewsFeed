package com.agarwalaman.newsfeed.listener;

public interface ClickListener {
    void onItemClick(int position);
    void onFavorite(int position, String id);
}
