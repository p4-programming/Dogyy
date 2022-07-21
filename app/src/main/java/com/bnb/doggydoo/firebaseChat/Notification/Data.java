package com.bnb.doggydoo.firebaseChat.Notification;

public class Data {
    String user, body, title, sent, type,userImage, code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data(String user, String body, String title, String sent, String type, String userImage, String code) {
        this.user = user;
        this.body = body;
        this.title = title;
        this.sent = sent;
        this.type = type;
        this.userImage = userImage;
        this.code = code;
    }


    public Data() {
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

}
