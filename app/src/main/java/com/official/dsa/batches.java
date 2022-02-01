package com.official.dsa;

public class batches {
    String title,button,url,imageurl;
    boolean visibility;

    public batches(){

    }

    public batches(String title, String button, String url, String imageurl,boolean visibility) {
        this.title = title;
        this.button = button;
        this.url = url;
        this.visibility=visibility;
        this.imageurl = imageurl;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
