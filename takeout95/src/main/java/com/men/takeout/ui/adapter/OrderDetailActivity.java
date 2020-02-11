package com.men.takeout.ui.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.men.takeout.R;
import com.men.takeout.observer.OrderObserver;
import com.men.takeout.ui.activity.BaseActivity;
import com.men.takeout.utils.Constant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OrderDetailActivity extends BaseActivity implements Observer {
    @InjectView(R.id.iv_order_detail_back)
    ImageView ivOrderDetailBack;
    @InjectView(R.id.tv_seller_name)
    TextView tvSellerName;
    @InjectView(R.id.tv_order_detail_time)
    TextView tvOrderDetailTime;
    @InjectView(R.id.map)
    MapView map;
    @InjectView(R.id.ll_order_detail_type_container)
    LinearLayout llOrderDetailTypeContainer;
    @InjectView(R.id.ll_order_detail_type_point_container)
    LinearLayout llOrderDetailTypePointContainer;
    private String orderId;
    private AMap aMap;
    private ArrayList<LatLng> riderPosList = new ArrayList();
    private Marker markerRider;
    private LatLng latlngBuyer;
    private LatLng riderPos;
    private LatLng latlngSeller;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.inject(this);
        map.onCreate(savedInstanceState);
        aMap = map.getMap();
        OrderObserver.getInstance().addObserver(this);
        orderId = getIntent().getStringExtra("orderId");
        String typeId = getIntent().getStringExtra("typeId");
        changeUI(getIndex(typeId));
    }






    private void changeUI(int index) {
        for (int i = 0; i < llOrderDetailTypeContainer.getChildCount(); i++) {
            TextView textView = (TextView) llOrderDetailTypeContainer.getChildAt(i);
            textView.setTextColor(Color.GRAY);
            ImageView imageView = (ImageView) llOrderDetailTypePointContainer.getChildAt(i);
            imageView.setImageResource(R.drawable.order_time_node_normal);
        }
        if (index != -1) {
            TextView textView = (TextView) llOrderDetailTypeContainer.getChildAt(index);
            textView.setTextColor(Color.BLUE);
            ImageView imageView = (ImageView) llOrderDetailTypePointContainer.getChildAt(index);
            imageView.setImageResource(R.drawable.order_time_node_disabled);
        }

    }

    /* 订单状态
     * 1 未支付 2 已提交订单 3 商家接单  4 配送中,等待送达 5已送达 6 取消的订单*/
    public static final String ORDERTYPE_UNPAYMENT = "10";
    public static final String ORDERTYPE_SUBMIT = "20";
    public static final String ORDERTYPE_RECEIVEORDER = "30";
    public static final String ORDERTYPE_DISTRIBUTION = "40";
    // 骑手状态：接单、取餐、送餐
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE = "43";
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL = "46";
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL = "48";

    public static final String ORDERTYPE_SERVED = "50";
    public static final String ORDERTYPE_CANCELLEDORDER = "60";

    private void switchState(String type,Object arg) {
        int state=-1;
        switch (type) {
            case ORDERTYPE_DISTRIBUTION:
                initMap();
                break;
            case ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE:
                initRider(arg);
                break;
            case ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL:

            case ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL:
                changeRider(arg);
                break;
        }
    }

    public int getIndex(String type) {
        int index = -1;
        switch (type) {
            case ORDERTYPE_UNPAYMENT:
                break;
            case ORDERTYPE_SUBMIT :
                index= 0;
                break;
            case ORDERTYPE_RECEIVEORDER :
                index= 1;
                break;
            case ORDERTYPE_DISTRIBUTION :
                index = 2;
                break;
            case ORDERTYPE_SERVED :
                index = 3;
                break;
            case ORDERTYPE_CANCELLEDORDER :
                break;

        }

        return index;

    }

    @Override
    public void update(Observable o, Object arg) {
        String jpushOrderId = (String) ((HashMap) arg).get("orderId");
        String typeId = (String) ((HashMap) arg).get("typeId");

        if (jpushOrderId.equals(orderId)) {
            changeUI(getIndex(typeId));
        }


            switchState(typeId,arg);


    }

    private void initRider(Object object) {
        riderPosList.clear();
        HashMap<String, String> hashMap = (HashMap<String, String>) object;
        String lat = hashMap.get(Constant.LAT);
        String lng = hashMap.get(Constant.LNG);

        if(TextUtils.isEmpty(lat)||TextUtils.isEmpty(lng)){
            return;
        }
        riderPos = new LatLng(Double.valueOf(lat),Double.valueOf(lng));
        //初始化图片
        ImageView markerRiderIcon = new ImageView(this);
        markerRiderIcon.setImageResource(R.mipmap.order_rider_icon);
        //指定骑手所在的经纬度，以及在地图的中心点显示
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(riderPos));
        //地图缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        //骑手图片的显示

        //指定锚点，以及描述文本
        markerRider = aMap.addMarker(new MarkerOptions().anchor(0.5f,1).position(riderPos));
        markerRider.setSnippet("骑手已接单");
        //显示骑手
        markerRider.showInfoWindow();
        markerRider.setIcon(BitmapDescriptorFactory.fromView(markerRiderIcon));

        //记录点的位置
        riderPosList.add(riderPos);
    }

    private void initMap() {
        map.setVisibility(View.VISIBLE);
        //让地图进行缩放
        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));

        // 添加买家marker
        latlngBuyer = new LatLng(40.100519, 116.365828);
        //让地图以买家为显示的中心点
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlngBuyer));

        Marker markerLatlngBuye=aMap.addMarker(new MarkerOptions().anchor(0.5f,1).position(latlngBuyer));
        ImageView markerBuyerIcon = new ImageView(this);
        markerBuyerIcon.setImageResource(R.mipmap.order_buyer_icon);
        markerLatlngBuye.setIcon(BitmapDescriptorFactory.fromView(markerBuyerIcon));

        // 添加卖家marker
        latlngSeller = new LatLng(40.060244, 116.343513);

        Marker markerLatlngSeller=aMap.addMarker(new MarkerOptions().anchor(0.5f,1).position(latlngSeller));
        ImageView markerSellerIcon = new ImageView(this);
        markerSellerIcon.setImageResource(R.mipmap.order_seller_icon);
        markerLatlngSeller.setIcon(BitmapDescriptorFactory.fromView(markerSellerIcon));
    }

    private void changeRider(Object data) {
        HashMap<String, String> hashMap = (HashMap<String, String>) data;

        String type = hashMap.get("type");

        String lat = hashMap.get(Constant.LAT);
        String lng = hashMap.get(Constant.LNG);

        //获取经纬度信息,定位骑手的位置
        LatLng currentPos=new LatLng(Double.valueOf(lat),Double.valueOf(lng));
        //将经纬度添加在经纬度集合中
        riderPosList.add(currentPos);
        //设置骑手的所在位置
        markerRider.setPosition(currentPos);
        //地图定位焦点
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(currentPos));

        String info="";
        DecimalFormat format=new DecimalFormat(".00");

        switch (type) {
            case OrderObserver.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL:
                // 取餐,距离卖家的距离
                float ds = AMapUtils.calculateLineDistance(currentPos, latlngSeller);
                info="距离商家"+format.format(ds)+"米";
                break;
            case OrderObserver.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL:
                // 送餐,距离买家的距离
                float db = AMapUtils.calculateLineDistance(currentPos, latlngBuyer);
                info="距离买家"+format.format(db)+"米";
                break;
        }

        markerRider.setSnippet(info);
        markerRider.showInfoWindow();
        //参数一:当前所处位置
        //参数二:历史的点
        drawLine(currentPos,riderPosList.get(riderPosList.size()-2));
    }

    private void drawLine(LatLng currentPos, LatLng pos) {
        aMap.addPolyline(new PolylineOptions().add(pos,currentPos).width(2).color(Color.GREEN));
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }
}
