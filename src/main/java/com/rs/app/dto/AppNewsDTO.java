package com.rs.app.dto;

import java.util.List;

public class AppNewsDTO {

    private AppNews appnews;

    public AppNews getAppnews() {
        return appnews;
    }

    public void setAppnews(AppNews appnews) {
        this.appnews = appnews;
    }

    public static class AppNews {
        private List<NewsItem> newsitems;

        public List<NewsItem> getNewsitems() {
            return newsitems;
        }

        public void setNewsitems(List<NewsItem> newsitems) {
            this.newsitems = newsitems;
        }
    }

    public static class NewsItem {
        private String title;
        private String url;
        private String contents;
        long date;
        int appid;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public int getAppid() {
            return appid;
        }

        public void setAppid(int appid) {
            this.appid = appid;
        }
    }
}
