package com.official.dsa.lists;

public class member_list {
    String name,designation,education,currently_doing,hobbies,dob,phone,url,key,business;

    public member_list(){

    }
    public member_list(String education, String currently_doing, String hobbies, String dob, String phone, String name, String designation,String url,String key,String business) {
        this.education = education;
        this.currently_doing = currently_doing;
        this.hobbies = hobbies;
        this.dob = dob;
        this.phone = phone;
        this.name=name;
        this.designation=designation;
        this.url=url;
        this.key=key;
        this.business=business;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCurrently_doing() {
        return currently_doing;
    }

    public void setCurrently_doing(String currently_doing) {
        this.currently_doing = currently_doing;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
