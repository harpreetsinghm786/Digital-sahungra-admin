package com.official.dsa.lists;

import java.util.List;

public class update {
    List<String> url;
    String title;
    String description;
    String key;

    public update(){

    }
    public update(List<String> url, String title, String description, String key) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
