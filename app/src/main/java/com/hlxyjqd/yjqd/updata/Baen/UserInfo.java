package com.hlxyjqd.yjqd.updata.Baen;

import java.io.Serializable;

/**
 * Created by zhangzhongping on 16/12/27.
 */

public class UserInfo implements Serializable {
    public int status;
    public String msg;
    public int var;
    public String url;
    public int type;
    public long time;
    public String varname;
    public String size;
    public String md5;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setVar(int var) {
        this.var = var;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setVarname(String varname) {
        this.varname = varname;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public int getVar() {
        return var;
    }

    public String getUrl() {
        return url;
    }

    public int getType() {
        return type;
    }

    public long getTime() {
        return time;
    }

    public String getVarname() {
        return varname;
    }

    public String getSize() {
        return size;
    }
}
