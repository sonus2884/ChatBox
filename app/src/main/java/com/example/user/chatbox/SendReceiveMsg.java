package com.example.user.chatbox;

public class SendReceiveMsg {

    private String message;
    private int user;

    public SendReceiveMsg(){

    }

    public SendReceiveMsg(String msg,int id){

        this.message = msg;
        this.user = id;
    }

    public void setId(int id) {
        this.user= id;
    }

    public int getId() {
        return user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
