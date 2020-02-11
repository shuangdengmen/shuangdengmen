package com.men.takeout.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.men.takeout.R;
import com.men.takeout.presenter.LoginPresenter;
import com.men.takeout.utils.SMSUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends BaseActivity {
    private static final int KEEP_TIME_MIN = 100;
    private static final int RESET_TIME = 101;
    private static final int SEND_CODE_SUCCESS = 102;
    private static final int SEND_CODE_FAIL = 103;
    private static final int CHECK_CODE_SUCCESS = 104;
    private static final int CHECK_CODE_FAIL = 105;
    @InjectView(R.id.iv_user_back)
    ImageView ivUserBack;
    @InjectView(R.id.iv_user_password_login)
    TextView ivUserPasswordLogin;
    @InjectView(R.id.et_user_phone)
    EditText etUserPhone;
    @InjectView(R.id.tv_user_code)
    TextView tvUserCode;
    @InjectView(R.id.et_user_code)
    EditText etUserCode;
    @InjectView(R.id.login)
    TextView login;

    private EventHandler eventHandler = new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE){
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    Log.i("msg","验证:"+Thread.currentThread().getId());
//                    handler.sendEmptyMessage(GET_VERIFICATION_CODE_SUCCESS);
                    //如果事件是发送了验证码短信,获取短信验证码后可以进行短信验证
                    handler.sendEmptyMessage(SEND_CODE_SUCCESS);
                }

                if (event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    handler.sendEmptyMessage(CHECK_CODE_SUCCESS);
                }

            }else{
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //如果事件是发送了验证码短信,获取短信验证码后可以进行短信验证
//                    handler.sendEmptyMessage(GET_VERIFICATION_CODE_FAIL);
                    handler.sendEmptyMessage(SEND_CODE_FAIL);
                }
                if (event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    handler.sendEmptyMessage(CHECK_CODE_FAIL);

                }
            }
            super.afterEvent(event, result, data);
        }
    };

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEEP_TIME_MIN:
                    time--;
                tvUserCode.setText("稍后再发("+time+")");
                    break;
                case RESET_TIME:
                    time=5;
                    tvUserCode.setText("重新获取验证码");
                    break;
                case SEND_CODE_SUCCESS:
                    Toast.makeText(getApplicationContext(),"发送验证码成功",Toast.LENGTH_SHORT).show();
                    break;
                case SEND_CODE_FAIL:
                    Toast.makeText(getApplicationContext(),"发送验证码失败",Toast.LENGTH_SHORT).show();
                    break;
                case CHECK_CODE_SUCCESS:
                    Toast.makeText(getApplicationContext(),"验证验证码成功",Toast.LENGTH_SHORT).show();
                    login();
                    break;
                case CHECK_CODE_FAIL:
                    Toast.makeText(getApplicationContext(),"验证验证码失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



    private int time =60;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        SMSUtil.checkPermission(this);
        SMSSDK.registerEventHandler(eventHandler);
        presenter = new LoginPresenter(this);
    }

    @OnClick({R.id.tv_user_code,R.id.login})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_user_code :
                sendCode();
                break;
            case R.id.login :
                checkLogin();
                break;

        }
    }

    private void checkLogin() {
        String phone = etUserPhone.getText().toString().trim();
        boolean mobileNO = SMSUtil.isMobileNO(phone);
        String code = etUserCode.getText().toString().trim();
        boolean codeNo= SMSUtil.isMobileNO(code);
    /*    if (mobileNO && !codeNo) {
            SMSSDK.submitVerificationCode("86",phone,code);
        }*/
        login();
    }

    private void login() {
        String phone = etUserPhone.getText().toString().trim();
        boolean mobileNO = SMSUtil.isMobileNO(phone);
        String code = etUserCode.getText().toString().trim();
        boolean codeNo= TextUtils.isEmpty(code);
        if (mobileNO && !codeNo) {
            presenter.getLoginData(phone,"",phone,2);
        }

    }

    private void sendCode() {
        String phone = etUserPhone.getText().toString().trim();

        boolean mobileNO = SMSUtil.isMobileNO(phone);
        if (mobileNO) {
            SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
                @Override
                public boolean onSendMessage(String s, String s1) {
                    return false;
                }
            });

            new Thread(){
                @Override
                public void run() {
                    while (time > 0) {
                        handler.sendEmptyMessage(KEEP_TIME_MIN);
                        try {
                            Thread.sleep(999);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    handler.sendEmptyMessage(RESET_TIME);
                }
            }.start();


        }

        tvUserCode.setEnabled(false);
        //开启子线程,开始倒计时
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler(eventHandler);
        super.onDestroy();

    }
}
