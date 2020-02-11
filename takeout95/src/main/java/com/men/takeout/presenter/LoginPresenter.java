package com.men.takeout.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;
import com.men.takeout.global.MyApplication;
import com.men.takeout.model.dao.DBHelper;
import com.men.takeout.model.dao.bean.UserInfo;
import com.men.takeout.presenter.net.bean.ResponseInfo;
import com.men.takeout.ui.activity.LoginActivity;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import retrofit2.Call;


/**
 * Created by HASEE on 2017/1/12.
 */

public class LoginPresenter extends BasePresenter {
    private Context mCtx;
    private AndroidDatabaseConnection connection;
    private Savepoint start;

    public LoginPresenter(Context ctx) {
        this.mCtx = ctx;
    }

    @Override
    protected void showError(String message) {

    }

    @Override
    public void parseGson(String json) {
        Log.i("",json);
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(json,UserInfo.class);
        MyApplication.userId = userInfo.get_id();
        //原来的处理方式
        //1.获取bean中的所有的字段   ormLite---->以注解的形式,替代掉了原生的sql语句
        //2.创建数据库takeout.db,创建用户表,
        //3,向表中插入一条用户数据

        //现在处理方式,将第一步和第三部可以面向对象的方式,合并到一起去做
        if (userInfo!=null){
            //1.获取DBHelper对象,获取userInfo指向t_user表的操作Dao对象
            DBHelper dbHelper = DBHelper.getInstance(mCtx);
            Dao<UserInfo,Integer> dao = dbHelper.getDao(UserInfo.class);

            //让ormlite中的sql语句绑定事物
            connection = new AndroidDatabaseConnection(dbHelper.getWritableDatabase(), true);
            //2.先将数据库中所有的用户登陆状态设置为未登录.
            try {
                //指定事物的回滚点
                start = connection.setSavePoint("start");
                //告知ormlite无需自动管理事物
                connection.setAutoCommit(false);
                List<UserInfo> userInfos = dao.queryForAll();//select * from t_user;  S(structs)S(spring)H(hibrenate)
                for (int i = 0; i < userInfos.size(); i++) {
                    int login = userInfos.get(i).getLogin();
                    if (login == 1){
                        //设置为未登录
                        userInfos.get(i).setLogin(0);
                        //update更新语句
                        dao.update(userInfos.get(i));
                    }
                }

                //3.将现在获取登录成功请求数据的这个用户设置为已登录的状态
                //3.1 此用户已经在数据库中存在,修改器登录状态即可
                UserInfo userBean = dao.queryForId(userInfo.get_id());//此方法等同于select * from t_user where _id = ?
                if (userBean!=null){
                    userBean.setLogin(1);
                    //update更新数据库
                    dao.update(userBean);
                }else{
                    //3.2 此用户之前从来没有在此手机上登录过,插入一条登录数据
                    userInfo.setLogin(1);
                    dao.create(userInfo);//等同于insert into
                }
                //提交事物
                connection.commit(start);

                //页面的结束
                ((LoginActivity)mCtx).finish();
            } catch (SQLException e) {
                e.printStackTrace();
                //让代码回顾到设置回滚点的地方
                try {
                    connection.rollback(start);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }
    //请求网络的触发方法
    public void getLoginData(String username,String password,String phone,int type){
        Call<ResponseInfo> loginInfo = responseInfoApi.getLoginData(username, password, phone, type);
        loginInfo.enqueue(new CallBackAdapter());
    }
}
