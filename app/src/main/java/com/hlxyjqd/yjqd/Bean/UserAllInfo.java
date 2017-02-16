package com.hlxyjqd.yjqd.Bean;

/**
 * Created by zhangzhongping on 16/12/26.
 */

public class UserAllInfo {
    public int cat_id;
    public String signinurl;
    public String signincheckurl;
    public String title;
    public String icon;
    public String signintext;

    public void setSignintext(String signintext) {
        this.signintext = signintext;
    }

    public String getSignintext() {
        return signintext;
    }

    public int getCat_id() {
        return cat_id;
    }
    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }
    public String getSigninurl() {
        return signinurl;
    }
    public void setSigninurl(String signinurl) {
        this.signinurl = signinurl;
    }
    public String getSignincheckurl() {
        return signincheckurl;
    }
    public void setSignincheckurl(String signincheckurl) {
        this.signincheckurl = signincheckurl;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
