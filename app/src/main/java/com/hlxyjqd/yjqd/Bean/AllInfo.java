package com.hlxyjqd.yjqd.Bean;


/**
 * Created by zhangzhongping on 16/12/26.
 */

public class AllInfo{
    private int status; //状态 0正常
    private String msg; //消息
    private Object data; //数据
    private String userinfo;

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "NoteResult [status=" + status + ", msg=" + msg + ", data="
                + data + "]";
    }
}
