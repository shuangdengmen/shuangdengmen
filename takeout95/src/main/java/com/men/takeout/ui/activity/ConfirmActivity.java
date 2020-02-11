package com.men.takeout.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.men.takeout.R;
import com.men.takeout.global.MyApplication;
import com.men.takeout.model.dao.ReceiptAddressDao;
import com.men.takeout.model.dao.bean.ReceiptAddressBean;
import com.men.takeout.presenter.net.bean.GoodsInfo;
import com.men.takeout.utils.CountPriceFormater;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ConfirmActivity extends BaseActivity {
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_login)
    TextView tvLogin;
    @InjectView(R.id.iv_location)
    ImageView ivLocation;
    @InjectView(R.id.tv_hint_select_receipt_address)
    TextView tvHintSelectReceiptAddress;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_sex)
    TextView tvSex;
    @InjectView(R.id.tv_phone)
    TextView tvPhone;
    @InjectView(R.id.tv_label)
    TextView tvLabel;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.ll_receipt_address)
    LinearLayout llReceiptAddress;
    @InjectView(R.id.iv_arrow)
    ImageView ivArrow;
    @InjectView(R.id.rl_location)
    RelativeLayout rlLocation;
    @InjectView(R.id.iv_icon)
    ImageView ivIcon;
    @InjectView(R.id.tv_seller_name)
    TextView tvSellerName;
    @InjectView(R.id.ll_select_goods)
    LinearLayout llSelectGoods;
    @InjectView(R.id.tv_deliveryFee)
    TextView tvDeliveryFee;
    @InjectView(R.id.tv_CountPrice)
    TextView tvCountPrice;
    @InjectView(R.id.tvSubmit)
    TextView tvSubmit;
    private List<GoodsInfo> shopCartList;
    private String deliveryFee;
    private float totalPrice;
    private String[] labelArr = {"家", "公司", "学校"};
    private int[] bgColorArray = {Color.parseColor("#ff888888"),
            Color.parseColor("#ff663322"),
            Color.parseColor("#ff882266")
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.inject(this);
        shopCartList = (List<GoodsInfo>) getIntent().getSerializableExtra("shopCartList");
        deliveryFee = getIntent().getStringExtra("deliveryFee");
        tvDeliveryFee.setText(deliveryFee);

        totalPrice = 0.0f;
        for (int i = 0; i <shopCartList.size() ; i++) {
            totalPrice += shopCartList.get(i).getNewPrice();
        }
        totalPrice += Float.valueOf(deliveryFee);
        tvCountPrice.setText("金额："+CountPriceFormater.format(totalPrice));
        initConfirmShopList();
    }

    @Override
    protected void onResume() {
        ReceiptAddressDao dao = new ReceiptAddressDao(this);
        List<ReceiptAddressBean> addressBeanList = dao.querySelection(MyApplication.userId);
        if (addressBeanList != null && addressBeanList.size() > 0) {
            ReceiptAddressBean addressBean = addressBeanList.get(0);
            showAddressList(addressBean);
        }
        super.onResume();
    }

    @OnClick({R.id.tvSubmit,R.id.rl_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                Intent intent = new Intent(ConfirmActivity.this, PayOnlineActivity.class);
                intent.putExtra("shopCartList", (Serializable) shopCartList);
                intent.putExtra("deliveryFee", deliveryFee);
                intent.putExtra("totalPrice",totalPrice);
                startActivity(intent);
                break;
            case R.id.rl_location:
                Intent intent1 = new Intent(ConfirmActivity.this, AddressListActivity.class);
                startActivityForResult(intent1,100);

        }
    }

    private void initConfirmShopList() {
        llSelectGoods.removeAllViews();
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
                    llSelectGoods.addView(confirmShopListView);
                }

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 101 && data != null) {
            ReceiptAddressBean addressBean = (ReceiptAddressBean) data.getSerializableExtra("addressBean");
            showAddressList(addressBean);
        }
    }

    private void showAddressList(ReceiptAddressBean addressBean) {
        tvName.setText(addressBean.getName());
        tvSex.setText(addressBean.getSex());
        tvAddress.setText(addressBean.getReceiptAddress() + addressBean.getDetailAddress());
        tvPhone.setText(addressBean.getPhone());
        String label = addressBean.getLabel();
        tvLabel.setText(label);
        int index = getIndex(label);
        if (!TextUtils.isEmpty(label)) {
            tvLabel.setVisibility(View.VISIBLE);
            tvLabel.setBackgroundColor(bgColorArray[index]);
        } else {
            tvLabel.setVisibility(View.GONE);
        }
    }

    private int getIndex(String label) {
        for (int i = 0; i < labelArr.length; i++) {
            if (labelArr[i].equals(label)) {
                return i;
            }
        }
        return 0;
    }
}
