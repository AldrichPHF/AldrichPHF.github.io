package com.phfsdy.qqcommon;

import java.io.Serializable;
/**
 * @author 庞化峰
 * @version 1.0
 * 表示一个用户信息
 */ 
public class User implements Serializable {//序列化
    private static final long serialVersionUID = 1L;//增强兼容性

    private String userId;
    private String passwd;


    public User(String userId,String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

}

