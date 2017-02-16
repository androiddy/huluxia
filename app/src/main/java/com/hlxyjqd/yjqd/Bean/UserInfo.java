package com.hlxyjqd.yjqd.Bean;

/**
 * Created by zhangzhongping on 16/12/26.
 */

public class UserInfo {
    public String avatar;
    public String nick;
    public int userID;
    public String token;
    public String devicecode;
    public String versioncode = "169";
    public String app_version = "3.5.1.57";
    private String identityTitle;
    private long indentityColor;
    private int nextExp;
    private int exp;

    public void setIdentityTitle(String identityTitle) {
        this.identityTitle = identityTitle;
    }

    public void setIndentityColor(long indentityColor) {
        this.indentityColor = indentityColor;
    }

    public void setNextExp(int nextExp) {
        this.nextExp = nextExp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getIdentityTitle() {
        return identityTitle;
    }

    public long getIndentityColor() {
        return indentityColor;
    }

    public int getNextExp() {
        return nextExp;
    }

    public int getExp() {
        return exp;
    }

    public void setDevicecode(String devicecode) {
        this.devicecode = devicecode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = "169";
    }

    public void setApp_version(String app_version) {
        this.app_version = "3.5.1.57";
    }

    public String getDevicecode() {
        return devicecode;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public String getApp_version() {
        return app_version;
    }

    public String getToken() {
        return token;
    }

    public void setToken( String token) {
        this.token = token;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNick() {
        return nick;
    }

    public int getUserID() {
        return userID;
    }
}
