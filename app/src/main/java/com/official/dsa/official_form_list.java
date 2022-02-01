package com.official.dsa;


import java.util.Date;

public class official_form_list {
    String url, name, description, key;
    java.util.Date date;

    public official_form_list() {

    }

    public official_form_list(String url, String name, String description, String key, java.util.Date date) {
        this.url = url;
        this.name = name;
        this.description = description;
        this.key = key;
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
