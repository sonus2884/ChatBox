package com.example.user.chatbox;

public class SendReceiveMsg {

    private String message;
    private int user;
   private String msgTime;

    public SendReceiveMsg(){

    }

    public SendReceiveMsg(String msg,int id,String msgTime){

        this.message = msg;
        this.user = id;
        this.msgTime = msgTime;
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

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgTime() {
        return msgTime;
    }
}
