package com.example.user.chatbox;

public class UserDetail {
    private String name;
    private String about;
    private String imageUri;

    public UserDetail() {

    }

    public UserDetail(String name, String about, String imageUri) {

        this.name = name;
        this.about = about;
        this.imageUri = imageUri;
    }

    public String getName() {

        return name;
    }

    public String getAbout() {

        return about;
    }

    public String getImageUri() {

        return imageUri;
    }

    public void setAbout(String about) {

        this.about = about;
    }

    public void setImageUri(String imageUri) {

        this.imageUri = imageUri;
    }

    public void setName(String name) {

        this.name = name;
    }
}
