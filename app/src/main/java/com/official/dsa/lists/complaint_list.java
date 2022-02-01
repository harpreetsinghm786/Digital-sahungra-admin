package com.official.dsa.lists;

import java.util.Date;

public class complaint_list {
    String name,title,phone_number,message,key;
    Date date;
    attachment_list attachment_list;


    public complaint_list(){

    }
    public complaint_list(String name, String title, String phone_number, String message, attachment_list attachment_list, Date date, String key) {
        this.name = name;
        this.attachment_list=attachment_list;
        this.phone_number = phone_number;
        this.message = message;
        this.key=key;

        this.date = date;
        this.title=title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public attachment_list getAttachment_list() {
        return attachment_list;
    }

    public void setAttachment_list(attachment_list attachment_list) {
        this.attachment_list = attachment_list;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
