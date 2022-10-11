package com.bnb.doggydoo.sos.ui.model;

public class CommentModels {

    private String comment;
    private Integer count;
    private String name;
    private String time;
    private String userID;
    private String userPic;

    public CommentModels() {
    }

    public CommentModels(String comment, Integer count, String name, String time, String userID, String userPic) {
        this.comment = comment;
        this.count = count;
        this.name = name;
        this.time = time;
        this.userID = userID;
        this.userPic = userPic;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
}
