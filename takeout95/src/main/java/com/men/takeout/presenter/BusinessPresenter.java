package com.men.takeout.presenter;

import android.content.Context;
import android.util.Log;

import com.men.takeout.presenter.net.bean.GoodsInfo;
import com.men.takeout.presenter.net.bean.GoodsTypeInfo;
import com.men.takeout.ui.activity.BusinessActivity;
import com.men.takeout.ui.fragment.GoodsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HASEE on 2017/1/12.
 */
public class BusinessPresenter extends BasePresenter {
    private Context ctx;

    public BusinessPresenter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void showError(String message) {

    }

    @Override
    public void parseGson(String json) {

    }

    //更新购物车中数据方法
    public void updateShopCart() {
        //获取右侧商品列表中的每一件商品,判断其count值是否大于0,如果大于0,数量累加
        int totalCount = 0;
        //提供一个金额计数变量
        float totalPrice = 0.0f;
        GoodsFragment goodsFragment = ((BusinessActivity) ctx).getGoodsFragment();
        ArrayList<GoodsInfo> goodsInfoList = goodsFragment.getGoodsAdapter().getGoodsInfos();
        for (int i = 0; i < goodsInfoList.size(); i++) {
            GoodsInfo goodsInfo = goodsInfoList.get(i);
            if (goodsInfo.getCount() > 0) {
                //购物车中商品数量
                totalCount += goodsInfo.getCount();
                //购物车总金额  商品数量*单价 然后多个商品总金额进行累加
                totalPrice += goodsInfo.getCount() * goodsInfo.getNewPrice();
            }
        }
        //根据总数量和总金额更新购物车气泡,金钱字符串
        Log.i("", "totalCount = " + totalCount);
        Log.i("", "totalPrice = " + totalPrice);
        //更新钱和数量方法
        ((BusinessActivity) ctx).updateShopCartCount(totalCount, totalPrice);
    }

    public List<GoodsInfo> getShopCartList() {
        ArrayList<GoodsInfo> goodsInfos = new ArrayList<>();
        GoodsFragment goodsFragment = ((BusinessActivity) ctx).getGoodsFragment();
        ArrayList<GoodsInfo> goodsInfoList = goodsFragment.getGoodsAdapter().getGoodsInfos();
        for (int i = 0; i < goodsInfoList.size(); i++) {
            GoodsInfo goodsInfo = goodsInfoList.get(i);
            if (goodsInfo.getCount() > 0) {
                goodsInfos.add(goodsInfo);
            }
        }
        return goodsInfos;
    }

    public void clearShopCart() {
        clearGoodsInfo();
        clearGoodsTypeInfo();
        updateShopCart();

        ((BusinessActivity) ctx).dismissBottomSheetLayout();
    }

    private void clearGoodsTypeInfo() {
        GoodsFragment goodsFragment = ((BusinessActivity) ctx).getGoodsFragment();
        List<GoodsTypeInfo> goodsTypeInfos = goodsFragment.getGoodsTypeAdapter().getData();
        for (int i = 0; i < goodsTypeInfos.size(); i++) {
            GoodsTypeInfo goodsTypeInfo = goodsTypeInfos.get(i);
            goodsTypeInfo.setCount(0);
        }
        goodsFragment.getGoodsTypeAdapter().notifyDataSetChanged();
    }

    private void clearGoodsInfo() {
        GoodsFragment goodsFragment = ((BusinessActivity) ctx).getGoodsFragment();
        ArrayList<GoodsInfo> goodsInfoList = goodsFragment.getGoodsAdapter().getGoodsInfos();
        for (int i = 0; i < goodsInfoList.size(); i++) {
            GoodsInfo goodsInfo = goodsInfoList.get(i);
            goodsInfo.setCount(0);
        }
        goodsFragment.getGoodsAdapter().notifyDataSetChanged();
    }
}
