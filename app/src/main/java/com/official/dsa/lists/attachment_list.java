package com.official.dsa.lists;

public class attachment_list {
    String filename,type,url;

    public attachment_list(){

    }
    public attachment_list(String filename, String type, String url) {
        this.filename = filename;
        this.type = type;
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
