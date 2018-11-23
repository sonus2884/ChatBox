package com.example.user.chatbox;

public class ChatsMsg {

   private String message;
   private String user;
   private String msgDate;
    private String msgTime;

    public ChatsMsg() {

    }

    public ChatsMsg(String message, String user) {

        this.message = message;
        this.user = user;
        //this.msgTime = msgTime;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public String getUser() {

        return user;
    }

    public void setUser(String user) {

        this.user = user;
    }

    public void setMsgTime(String msgTime) {

        this.msgTime = msgTime;
    }

    public String getMsgTime() {

        return msgTime;
    }

    public String getMsgDate() {

        return msgDate;
    }

    public void setMsgDate(String msgDate) {

        this.msgDate = msgDate;
    }
}
