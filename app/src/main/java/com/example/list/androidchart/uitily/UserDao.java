package com.example.list.androidchart.uitily;

public class UserDao {

    static String userId,userName;
    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        UserDao.userId = userId;
    }
    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userNAME) {
        userName = userNAME;
    }

}
