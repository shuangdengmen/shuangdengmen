package com.men.takeout.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.men.takeout.R;
import com.men.takeout.pay.AuthResult;
import com.men.takeout.pay.PayResult;
import com.men.takeout.presenter.net.bean.GoodsInfo;
import com.men.takeout.utils.CountPriceFormater;
import com.men.takeout.utils.OrderInfoUtil2_0;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PayOnlineActivity extends BaseActivity {
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_residualTime)
    TextView tvResidualTime;
    @InjectView(R.id.tv_order_name)
    TextView tvOrderName;
    @InjectView(R.id.tv)
    TextView tv;
    @InjectView(R.id.tv_order_detail)
    TextView tvOrderDetail;
    @InjectView(R.id.iv_triangle)
    ImageView ivTriangle;
    @InjectView(R.id.ll_order_toggle)
    RelativeLayout llOrderToggle;
    @InjectView(R.id.tv_receipt_connect_info)
    TextView tvReceiptConnectInfo;
    @InjectView(R.id.tv_receipt_address_info)
    TextView tvReceiptAddressInfo;
    @InjectView(R.id.ll_goods)
    LinearLayout llGoods;
    @InjectView(R.id.ll_order_detail)
    LinearLayout llOrderDetail;
    @InjectView(R.id.tv_pay_money)
    TextView tvPayMoney;
    @InjectView(R.id.iv_pay_alipay)
    ImageView ivPayAlipay;
    @InjectView(R.id.cb_pay_alipay)
    CheckBox cbPayAlipay;
    @InjectView(R.id.tv_selector_other_payment)
    TextView tvSelectorOtherPayment;
    @InjectView(R.id.ll_hint_info)
    LinearLayout llHintInfo;
    @InjectView(R.id.iv_pay_wechat)
    ImageView ivPayWechat;
    @InjectView(R.id.cb_pay_wechat)
    CheckBox cbPayWechat;
    @InjectView(R.id.iv_pay_qq)
    ImageView ivPayQq;
    @InjectView(R.id.cb_pay_qq)
    CheckBox cbPayQq;
    @InjectView(R.id.iv_pay_fenqile)
    ImageView ivPayFenqile;
    @InjectView(R.id.cb_pay_fenqile)
    CheckBox cbPayFenqile;
    @InjectView(R.id.ll_other_payment)
    LinearLayout llOtherPayment;
    @InjectView(R.id.bt_confirm_pay)
    Button btConfirmPay;
    private List<GoodsInfo> shopCartList;
    private String deliveryFee;
    public static final String APPID = "2021001119609661";
    public static final String RSA2_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC/uRI+hbrhbjnygm/O16ItghEQeDlQqGbmuEt/iwFZkLG5mhCU5+bzGSf/oYn3oA2p6UQ9SGnUo68H3Obu7B8Uj1Hbb1MVulIuBD7nH6bwWRDkJYDHukiIifySaP84FIkeVjtYt/26WYffdfgETE9nDBFzS74WGbzaPQDTn+vBJ3ae5nZQUtNoG1jZS3esFAomd963blgWKT51UnJM6BGLhYY8wCwD/qQeZFk+WIVO2wWShzzgTCdvmVUxsDB9ULdZiSzewGpg9J9yDrxnHu3mSPjj5CY22BMbLjDyOLdN/8d0Q0OcpA6m+ZtwFRI4avuIcqL4HErAr/NCQJyOVTPhAgMBAAECggEBAIKU7g0xjmiOZ9954edSzT4Lsc2y5O6/1F5eteVn6wJy3+nXOmYBUTbQMoMVJmBHXicVHwpScOqIleVYGt4uarjZKNq5hzBsWB7Je4J194EKWMvbk0v14ANHYITkCLjMm9vSxdcWcjVjV3zm34cl5or8GEKcGxr4ar2GATMiYDOMnJK7u9SKZwQ5cJUAkVUgFMr3ZOGt5HtbrsoTSsbDlB+ij8XlIz7GlKqq68x58r3fqqm4CIKtgYSmo/KRO8vcdRGyGqFtECT+rY4Yb+MONqpYDw8J5GJsW6yqRk/YDa5evUE4UxaEt4y8zPvrdM28lMW3DqBXSEshRJh45ffF7fECgYEA4aT6JdJWD4qQuu83U53H49s5lsNtdYKWWEbF/PmB5naAyx+e/5fwUztiUY7wAsaOYCmPGnW4/iIZ10lte7CIAz4Y6ANuvc8ZZw2TlRY7kDqLadYlRt12j2f+/bdFwHCfjTfPfjYg7QRAJ01nOmeomHEuSulNLdrAmXowWKkN6e0CgYEA2YPc/KML7Pc0Izm3VSrvumMCOTI4PWWbCs7SoPVf/f8xOkVeMGK1aDdG4jKtBVFfGsE5OiN4Z93Z1BmEzUKDhVMdSnlgOI2SvHxfrqshVIC9aEoLltOKiB2p26L4nzTM1hJqfWb436ldHRJcK5llTAFUu6tfnLWc8xnPP77I40UCgYEA4XK8nF1zr0N7J8srNFhJB9hZydCo5Of832aXGxMQPvvX4xtM+YMNk5uZuHNgSuWoC1x6pq1IkGHx/7BVu2e9/b9lkysa0nrBnr1p32O/T3OvJ6fWxt8vPklh9I3RG11rjOVeY3Qhoa7Fin9DzI8VXHp5Dk/b1RzjonT7KRp/1GECgYBHSb9YkXtsG4zb6qOaZysQlDh7VGk4Ph+1SBmiJSljzopsxaVcCE3aLRuvMNqh/oxD8YX29v8UU5J7wC1VNHJ0dMwJx4PwpP2thrm5Pl77GgJ5tqLbuNMTT12HHX+MdjqYAosiNGhmrVRWHdlVdL3NTjZeF4mLi743soLJKpceXQKBgQDLMBlQOqqpCjQyBLnL2mVpHe2Dn7HklfpHmA6DC6i5wd8Oh7ZkkEIA89YsqbLUbwUX8m43xMoUL9NL8JTsTRD13xSzFh1HXYWxwEhxyQJoXQGr9L47hJf/53O2CR0mAsgz7E77c2cvZdizkH5vhdn+bpaoBWL7/BOq/9/RMcVTWQ==";
    public static final String RSA_PRIVATE  = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMWX0QCl6aGQBtjBHHNzlB5cFB7JabVnNk4gmwxujkoflx3cz3wheZJAHNtGRCyKNaGVnrn/QwgBmCdtghmFqzZXIvnYLK5cLPWBWWSvckYEeqBEZXj6VnTRLdq989VXPZCcGmp9Bn/ennXhZM5eVmU7vKPoTUqYk5+oshrEwxe5AgMBAAECgYEAurDbf3JDUSxE281xtT+RxjGXrRL+drAjJiQ2esOGW1CHtmZn/8+dDurWmPNF/JNFYvt78h7VVZ40BaS5FKYwAciVoqHg+E7JZbvaKrngUejWR1E6vDYUAerj3znRqWoq6GnWow4XBnJgje13rtm6SW4sCk+nbyhLs9a1e6K4/YECQQDyar2cRApzU4am2t6CJ9l/3gxKMhnKDt3YfpqJ/j493uiqhsEQHsQnNvlPeMXmMPfK87WMmsztk62IE5PpF8yxAkEA0Kodokm+9he9KQWD0Oja9PkNrThD2Fin+Nrd/4IIDIPO/0+Mtr1Fk77zWj0dGQgsK6QSYbFpoVBOyrth/d6diQJBALhggzK1dZVdtA54bcBk+sKMaqCMFp1eTVF6iaPnIgQA5Mm+kFzoaZzB7UGKxEpeeDl+v1jf7HnOVrAm49rn5SECQD0eVue+KgzzDxkA4IdEbT3r5TGjlVu9PBYMJZI9iBYCVZM6vcCY19RazfTUib1XQ3jU6f1rdcEmQK3pDN7LYeECQAp8CjeihDmXq91qB93gVYtsNnixC1i5pwNj5ekG9EJeBL1gCRpKshncFp4JwXYZ0Ve8s+4PVwrME58QOzxfGOw=";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayOnlineActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayOnlineActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayOnlineActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayOnlineActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);
        ButterKnife.inject(this);
        shopCartList = (List<GoodsInfo>) getIntent().getSerializableExtra("shopCartList");
        deliveryFee = getIntent().getStringExtra("deliveryFee");
        float totalPrice = getIntent().getFloatExtra("totalPrice",0.0f);
        tvPayMoney.setText(CountPriceFormater.format(totalPrice));

    }

    @OnClick({R.id.iv_triangle,R.id.bt_confirm_pay})
     public void onClick(View view) {
             switch (view.getId()) {
                 case R.id.iv_triangle:
                     initDetailShopCart();
                     break;
                 case R.id.bt_confirm_pay:
                     pay(view);
                     break;
             }
         }

    private void pay(View view) {
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayOnlineActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    private void initDetailShopCart() {

        int visibility = llOrderDetail.getVisibility();
        if (View.GONE==visibility) {
            llOrderDetail.setVisibility(View.VISIBLE);
            llOrderDetail.removeAllViews();
            if (shopCartList != null && shopCartList.size() > 0) {
                for (int i = 0; i < shopCartList.size(); i++) {
                    GoodsInfo goodsInfo = shopCartList.get(i);
                    if (goodsInfo.getCount() > 0) {
                        View confirmShopListView = View.inflate(getApplicationContext(), R.layout.item_confirm_order_goods, null);
                        TextView tvName = (TextView) confirmShopListView.findViewById(R.id.tv_name);
                        TextView tvCount = (TextView) confirmShopListView.findViewById(R.id.tv_count);
                        TextView tvPrice = (TextView) confirmShopListView.findViewById(R.id.tv_price);

                        int count = goodsInfo.getCount();
                        float newPrice = goodsInfo.getNewPrice();
                        String totalPrice = CountPriceFormater.format(count * newPrice);

                        tvName.setText(goodsInfo.getName());
                        tvCount.setText(goodsInfo.getCount() + "");
                        tvPrice.setText(totalPrice);
                        llOrderDetail.addView(confirmShopListView);
                    }

                }
            }
        }else {
            llOrderDetail.setVisibility(View.GONE);
        }
    }
}
