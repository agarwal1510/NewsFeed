package com.agarwalaman.newsfeed.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agarwalaman.newsfeed.R;
import com.agarwalaman.newsfeed.listener.ClickListener;
import com.agarwalaman.newsfeed.model.Article;
import com.agarwalaman.newsfeed.model.NewsListItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements View.OnClickListener {


    private List<NewsListItem> newsList;
    private final ClickListener listener;
    private List<String> favArticles = new ArrayList<>();

    public NewsAdapter(List<NewsListItem> newsList, List<String> favArticles, ClickListener listener) {
        this.newsList = newsList;
        this.listener = listener;
        if (favArticles != null)
            this.favArticles = favArticles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Article article = newsList.get(position).getContent();
        final String id = newsList.get(position).getId();
        holder.newsTitle.setText(article.getTitle());
        if (favArticles != null && favArticles.contains(id)) {
            holder.favorite.setImageDrawable(ContextCompat.getDrawable(holder.favorite.getContext(), R.drawable.baseline_favorite_black_18));
        } else {
            holder.favorite.setImageDrawable(ContextCompat.getDrawable(holder.favorite.getContext(), R.drawable.baseline_favorite_border_black_18));
        }
        Glide.with(holder.newsImage.getContext()).load(article.getImages().get(0).getOriginalUrl()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.newsImage);
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favArticles.contains(id)) {
                    favArticles.remove(id);
                    holder.favorite.setImageDrawable(ContextCompat.getDrawable(holder.favorite.getContext(), R.drawable.baseline_favorite_border_black_18));
                } else {
                    favArticles.add(id);
                    holder.favorite.setImageDrawable(ContextCompat.getDrawable(holder.favorite.getContext(), R.drawable.baseline_favorite_black_18));
                }
                listener.onFavorite(holder.getLayoutPosition(), id);
            }
        });
       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.getLayoutPosition());
            }
        });
    }

    public void setFavArticles(List<String> favArticles) {
        this.favArticles = favArticles;
    }

    public void setNewsList(List<NewsListItem> newsList) {
        this.newsList = newsList;
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView newsTitle;
        private ImageView newsImage;
        private ImageView favorite;
        private View view;

        private ViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            this.newsTitle = itemView.findViewById(R.id.news_title);
            this.newsImage = itemView.findViewById(R.id.news_image);
            this.favorite = itemView.findViewById(R.id.favorite);
        }
    }
}
