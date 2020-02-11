package com.men.takeout.global;

import android.app.Application;

import com.j256.ormlite.dao.Dao;
import com.men.takeout.model.dao.DBHelper;
import com.men.takeout.model.dao.bean.UserInfo;

import java.sql.SQLException;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;

public class MyApplication extends Application {
    public static int statusBarHeight;
    public static int userId =-1;

    @Override
    public void onCreate() {
        super.onCreate();

        SMSSDK.initSDK(this, "1ab0922b18f6f", "c3f657c30a47c531d46d94730e7dc05c");

        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        //初始化用户的userId
        initUserId();
        //初始化极光推送
        initJpush();


    }

    private void initJpush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    private void initUserId() {
        Dao<UserInfo,Integer> dao = DBHelper.getInstance(this).getDao(UserInfo.class);

        List<UserInfo> userInfos = null;
        try {
            userInfos = dao.queryBuilder().where().eq("islogin", "1").query();
            if (userInfos != null && userInfos.size() > 0) {
                UserInfo userInfo = userInfos.get(0);
                userId= userInfo.get_id();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
