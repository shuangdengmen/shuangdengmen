package com.men.takeout.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.men.takeout.R;
import com.men.takeout.presenter.BusinessPresenter;
import com.men.takeout.presenter.net.bean.GoodsInfo;
import com.men.takeout.presenter.net.bean.Seller;
import com.men.takeout.ui.adapter.BusinessFragmentPagerAdapter;
import com.men.takeout.ui.adapter.ShopCartAdapter;
import com.men.takeout.ui.fragment.GoodsFragment;
import com.men.takeout.utils.CountPriceFormater;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BusinessActivity extends BaseActivity {
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ib_menu)
    ImageButton ibMenu;
    @InjectView(R.id.tabs)
    TabLayout tabs;
    @InjectView(R.id.vp)
    ViewPager vp;
    @InjectView(R.id.bottomSheetLayout)
    BottomSheetLayout bottomSheetLayout;
    @InjectView(R.id.imgCart)
    ImageView imgCart;
    @InjectView(R.id.tvSelectNum)
    TextView tvSelectNum;
    @InjectView(R.id.tvCountPrice)
    TextView tvCountPrice;
    @InjectView(R.id.tvDeliveryFee)
    TextView tvDeliveryFee;
    @InjectView(R.id.tvSendPrice)
    TextView tvSendPrice;
    @InjectView(R.id.tvSubmit)
    TextView tvSubmit;
    @InjectView(R.id.bottom)
    LinearLayout bottom;
    @InjectView(R.id.fl_Container)
    FrameLayout flContainer;


    private String[] arrayString = {"商品", "评价", "商家"};
    private Seller seller;
    private BusinessFragmentPagerAdapter adapter;
    private TextView tvClear;
    private RecyclerView rvCart;
    private ShopCartAdapter shopCartAdapter;
    private List<GoodsInfo> shopCartList;

    public BusinessPresenter getBusinessPresenter() {
        return businessPresenter;
    }

    private BusinessPresenter businessPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness);
        ButterKnife.inject(this);
        seller = (Seller) getIntent().getSerializableExtra("seller");
        businessPresenter = new BusinessPresenter(this);
        initTab();
        initVp();
        tabs.setupWithViewPager(vp);

        String deliveryFee = CountPriceFormater.format(Float.valueOf(seller.getDeliveryFee()));
        String sendPrice = CountPriceFormater.format(Float.valueOf(seller.getSendPrice()));
        tvDeliveryFee.setText(deliveryFee);
        tvSendPrice.setText(sendPrice);
    }

    private void initVp() {
        adapter = new BusinessFragmentPagerAdapter(getSupportFragmentManager(), arrayString, seller);

        vp.setAdapter(adapter);
    }

    private void initTab() {
        for (int i = 0; i < arrayString.length; i++) {
            tabs.addTab(tabs.newTab().setText(arrayString[i]));
        }
    }

    public void setImageView(ImageView imageView, int width, int height) {
        Log.d("setImageView", "setImageView");
        flContainer.addView(imageView, width, height);
    }

    public int[] getShopCartLocation() {
        int[] shopCart = new int[2];
        imgCart.getLocationInWindow(shopCart);
        return shopCart;
    }

    public void removeImageView(ImageView imageView) {
        if (imageView != null) {
            flContainer.removeView(imageView);
        }
    }

    public GoodsFragment getGoodsFragment() {
        ArrayList<Fragment> fragmentList = adapter.getFragmentList();
        if (fragmentList != null && fragmentList.size() > 0) {
            GoodsFragment goodsFragment = (GoodsFragment) fragmentList.get(0);
            return goodsFragment;
        }
        return null;
    }


    public void updateShopCartCount(int totalCount, float totalPrice) {
        if (totalCount == 0) {
            tvSelectNum.setVisibility(View.GONE);
            tvCountPrice.setText("0");
        } else {
            tvSelectNum.setVisibility(View.VISIBLE);
            tvSelectNum.setText(totalCount + "");
            tvCountPrice.setText(CountPriceFormater.format(totalPrice));
        }
        tvSubmit.setText(CountPriceFormater.format(totalPrice));
        if (totalPrice > Float.valueOf(seller.getSendPrice())) {
            tvSendPrice.setVisibility(View.GONE);
            tvSubmit.setVisibility(View.VISIBLE);
        } else {
            tvSendPrice.setVisibility(View.VISIBLE);
            tvSubmit.setVisibility(View.GONE);

        }
    }


    public List<GoodsInfo> getShopCartList() {
        return shopCartList;
    }

    @OnClick({R.id.bottom, R.id.tvSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom:
                View cartListView = getCartListView();

                if (bottomSheetLayout.isSheetShowing()) {
                    bottomSheetLayout.dismissSheet();
                } else {
                    shopCartList = businessPresenter.getShopCartList();
                    shopCartAdapter.setData(shopCartList);
                    bottomSheetLayout.showWithSheetView(getCartListView());
                }

                break;
            case R.id.tvSubmit:
                Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
                intent.putExtra("shopCartList", (Serializable) businessPresenter.getShopCartList());
                intent.putExtra("deliveryFee", seller.getDeliveryFee());
                startActivity(intent);
                break;
        }

    }

    private View getCartListView() {

        View cartListView = View.inflate(getApplicationContext(), R.layout.cart_list, null);
        tvClear = (TextView) cartListView.findViewById(R.id.tvClear);
        rvCart = (RecyclerView) cartListView.findViewById(R.id.rvCart);
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BusinessActivity.this);
                builder.setTitle("是否清除购物车");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        businessPresenter.clearShopCart();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        List<GoodsInfo> shopCartList = businessPresenter.getShopCartList();
        shopCartAdapter = new ShopCartAdapter(this, shopCartList);
        rvCart.setAdapter(shopCartAdapter);
        rvCart.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        return cartListView;
    }

    public void dismissBottomSheetLayout() {
        bottomSheetLayout.dismissSheet();
    }
}
