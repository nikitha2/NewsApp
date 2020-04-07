package com.nikitha.android.newscovid19;

import android.graphics.Bitmap;

public class ListItems {

    Bitmap image;
    String title;
    String source;
    String url;
    String publishDate;
    String author;

    public ListItems(Bitmap image, String title, String source, String url,String publishDate,String author) {
        this.image = image;
        this.title = title;
        this.source = source;
        this.url = url;
        this.publishDate=publishDate;
        this.author=author;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
