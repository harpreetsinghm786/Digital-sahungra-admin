package com.official.dsa.lists;

public class stars_list{
    String name,designation,url,key,desc;

    public stars_list(){

    }

    public stars_list(String name, String designation, String url,String key,String desc) {
        this.name = name;
        this.designation = designation;
        this.url = url;
        this.desc=desc;
        this.key=key;


    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
