package com.hlxyjqd.yjqd.Bean;

/**
 * Created by zhangzhongping on 16/12/27.
 */

public class SigninInfo {
    public String msg;
    public int signin;
    public int status;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSignin(int signin) {
        this.signin = signin;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public int getSignin() {
        return signin;
    }

    @Override
    public String toString() {
        return "SigninInfo{" +
                "msg='" + msg + '\'' +
                ", signin=" + signin +
                ", status=" + status +
                '}';
    }
}
