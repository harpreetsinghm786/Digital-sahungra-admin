package com.official.dsa.lists;

public class assets_list {
    String name,key,category;

   public assets_list(){

   }
    public assets_list(String name, String key,String category) {
        this.name = name;
        this.key = key;
        this.category=category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
