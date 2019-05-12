package com.example.user.chatbox.Class;

public class UserDetail {
    private String name;
    private String about;
    private String imageUri;
    private String onOffLine;


    public UserDetail() {

    }

    public UserDetail(String name, String about, String imageUri, String onOffLine) {

        this.name = name;
        this.about = about;
        this.imageUri = imageUri;
        this.onOffLine = onOffLine;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getAbout() {

        return about;
    }

    public void setAbout(String about) {

        this.about = about;
    }

    public String getImageUri() {

        return imageUri;
    }

    public void setImageUri(String imageUri) {

        this.imageUri = imageUri;
    }

    public String getOnOffLine() {
        return onOffLine;
    }

    public void setOnOffLine(String onOffLine) {
        this.onOffLine = onOffLine;
    }
}
