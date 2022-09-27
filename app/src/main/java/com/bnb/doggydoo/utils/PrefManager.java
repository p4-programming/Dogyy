package com.bnb.doggydoo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String PREF_NAME = "doggyDoo";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_IMAGE = "user_image";
    public static final String USER_MOBILE = "user_mobile";
    public static final String CALLER_ID = "caller_id";
    public static final String CALL_ID = "call_id";
    public static final String USER_REGISTERED = "is_register";
    public static final String USER_ID_NAME = "username";
    public static final String USER_EMAIL = "email";
    public static final String USER_REQUEST_MAP = "reqType";
    public static final String USER_CURRENT_LAT = "latitude";
    public static final String USER_CURRENT_LONG = "longitude";
    public static final String NEWS_TYPE_FILTER = "newsfilter";
    public static final String TYPE = "type";

    public void setUSERId(String USER_id) {
        editor.putString(USER_ID, USER_id);
        editor.commit();
    }

    public String getUSERId() {
        return pref.getString(USER_ID, "");
    }

    public void setUserName(String userName) {
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(USER_NAME, "");
    }

    public void setUserImage(String userImage) {
        editor.putString(USER_IMAGE, userImage);
        editor.commit();
    }

    public String getUserImage() {
        return pref.getString(USER_IMAGE, "");
    }

    public void setUserMobile(String userMobile) {
        editor.putString(USER_MOBILE, userMobile);
        editor.commit();
    }

    public String getUserMobile() {
        return pref.getString(USER_MOBILE, "");
    }

    public void setCallerId(String caller_id) {
        editor.putString(CALLER_ID, caller_id);
        editor.commit();
    }

    public String getCallerId() {
        return pref.getString(CALLER_ID, "");
    }


    public void setCallId(String call_id) {
        editor.putString(CALL_ID, call_id);
        editor.commit();
    }

    public String getCallId() {
        return pref.getString(CALL_ID, "");
    }

    public void setUserRegistered(String userRegistered) {
        editor.putString(USER_REGISTERED, userRegistered);
        editor.commit();
    }

    public String getUserRegistered() {
        return pref.getString(USER_REGISTERED, "");
    }


    public void setUserIdName(String username) {
        editor.putString(USER_ID_NAME, username);
        editor.commit();
    }

    public String getUserIdName() {
        return pref.getString(USER_ID_NAME, "");
    }

    public void setUserEmail(String email) {
        editor.putString(USER_EMAIL, email);
        editor.commit();
    }

    public String getUserEmail() {
        return pref.getString(USER_EMAIL, "");
    }


    public void setUserReqType(String reqType) {
        editor.putString(USER_REQUEST_MAP, reqType);
        editor.commit();
    }

    public String getUserReqType() {
        return pref.getString(USER_REQUEST_MAP, "");
    }


    public void setUserLat(String latitude) {
        editor.putString(USER_CURRENT_LAT, latitude);
        editor.commit();
    }

    public String getUserLat() {
        return pref.getString(USER_CURRENT_LAT, "");
    }


    public void setUserLong(String longitude) {
        editor.putString(USER_CURRENT_LONG, longitude);
        editor.commit();
    }

    public String getUserLong() {
        return pref.getString(USER_CURRENT_LONG, "");
    }


    public void setNewsTypeFilter(String newsfilter) {
        editor.putString(NEWS_TYPE_FILTER, newsfilter);
        editor.commit();
    }

    public String getNewsTypeFilter() {
        return pref.getString(NEWS_TYPE_FILTER, "");
    }


    public void setType(String type){
        editor.putString(TYPE,type);
        editor.commit();
    }

    public String getType(){
        return pref.getString(TYPE,"");
    }

}
