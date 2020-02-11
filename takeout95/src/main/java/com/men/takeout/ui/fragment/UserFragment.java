package com.men.takeout.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.men.takeout.R;
import com.men.takeout.global.MyApplication;
import com.men.takeout.model.dao.DBHelper;
import com.men.takeout.model.dao.bean.UserInfo;
import com.men.takeout.ui.activity.LoginActivity;

import java.sql.SQLException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by HASEE on 2017/1/9.
 */
public class UserFragment extends BaseFragment {

    @InjectView(R.id.tv_user_setting)
    ImageView tvUserSetting;
    @InjectView(R.id.iv_user_notice)
    ImageView ivUserNotice;
    @InjectView(R.id.login)
    ImageView login;
    @InjectView(R.id.username)
    TextView username;
    @InjectView(R.id.phone)
    TextView phone;
    @InjectView(R.id.ll_userinfo)
    LinearLayout llUserinfo;
    @InjectView(R.id.iv_address)
    ImageView ivAddress;
    private Dao<UserInfo, Integer> dao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        DBHelper ormHelper = DBHelper.getInstance(getActivity());
        dao = ormHelper.getDao(UserInfo.class);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onResume() {
        if (MyApplication.userId == -1) {
            login.setVisibility(View.VISIBLE);
            llUserinfo.setVisibility(View.INVISIBLE);

        } else {
            try {
                UserInfo userInfo = dao.queryForId(MyApplication.userId);
                if (userInfo != null) {
                    login.setVisibility(View.GONE);
                    llUserinfo.setVisibility(View.VISIBLE);
                    username.setText(userInfo.getName());
                    phone.setText(userInfo.getPhone());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        super.onResume();
    }

    @OnClick(R.id.login)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
