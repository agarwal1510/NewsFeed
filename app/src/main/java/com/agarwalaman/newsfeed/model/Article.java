package com.agarwalaman.newsfeed.model;

import java.util.List;

public class Article {

    private String title;
    private String url;
    private String summary;
    private List<Image> images;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public List<Image> getImages() {
        return images;
    }

    public String getSummary() {
        return summary;
    }

    public class Image {

        private String originalUrl;

        public String getOriginalUrl() {
            return originalUrl;
        }
    }
}
