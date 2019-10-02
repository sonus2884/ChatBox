package com.example.user.chatbox.Class;

public class SendReceiveMsg {

    private String message;
    private String image_message;
    private int user;
    private String msgTime;
    private boolean isSeenMsg;
    private  boolean isTyping;
    private String msgDate;

    public SendReceiveMsg() {

    }

    public SendReceiveMsg(String msg, int id, String msgTime, boolean isSeenMsg,boolean isTyping,String msgDate,String image_message) {

        this.message = msg;
        this.user = id;
        this.msgTime = msgTime;
        this.isSeenMsg = isSeenMsg;
        this.isTyping = isTyping;
        this.msgDate = msgDate;
        this.image_message=image_message;
    }

    public int getId() {
        return user;
    }

    public void setId(int id) {
        this.user = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public boolean isSeenMsg() {
        return isSeenMsg;
    }

    public void setSeenMsg(boolean seenMsg) {
        isSeenMsg = seenMsg;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getImage_message() {
        return image_message;
    }

    public void setImage_message(String image_message) {
        this.image_message = image_message;
    }
}
