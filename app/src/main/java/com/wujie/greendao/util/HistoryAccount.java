package com.wujie.greendao.util;

import java.io.Serializable;

/**
 * Created by wujie on 2017/1/9.
 */
public class HistoryAccount implements Serializable {

    private String username;
    private String DevID;

    public HistoryAccount(String username, String devID) {
        this.username = username;
        DevID = devID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDevID() {
        return DevID;
    }

    public void setDevID(String devID) {
        DevID = devID;
    }
}
