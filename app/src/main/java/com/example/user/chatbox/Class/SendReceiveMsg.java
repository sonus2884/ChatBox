package com.example.user.chatbox.Class;

public class SendReceiveMsg {

    private String message;
    private int user;
    private String msgTime;
    private boolean isSeenMsg;

    public SendReceiveMsg() {

    }

    public SendReceiveMsg(String msg, int id, String msgTime, boolean isSeenMsg) {

        this.message = msg;
        this.user = id;
        this.msgTime = msgTime;
        this.isSeenMsg = isSeenMsg;
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
}
