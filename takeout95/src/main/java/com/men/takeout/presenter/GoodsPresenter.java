package com.men.takeout.presenter;

import com.google.gson.Gson;
import com.men.takeout.presenter.net.bean.BusinessInfo;
import com.men.takeout.presenter.net.bean.GoodsInfo;
import com.men.takeout.presenter.net.bean.GoodsTypeInfo;
import com.men.takeout.presenter.net.bean.ResponseInfo;
import com.men.takeout.presenter.net.bean.Seller;
import com.men.takeout.ui.adapter.GoodsAdapter;
import com.men.takeout.ui.adapter.GoodsTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class GoodsPresenter extends BasePresenter {

    private  GoodsAdapter goodsAdapter;
    private GoodsTypeAdapter goodsTypeAdapter;
    private Seller seller;
    private BusinessInfo businessInfo;
    private ArrayList<GoodsInfo> goodsInfos;

    public GoodsPresenter(GoodsTypeAdapter goodsTypeAdapter, GoodsAdapter goodsAdapter, Seller seller) {
        this.goodsTypeAdapter = goodsTypeAdapter;
        this.seller=seller;
        this.goodsAdapter=goodsAdapter;
    }

    @Override
    protected void showError(String message) {

    }

    @Override
    public void parseGson(String data) {
        businessInfo = new Gson().fromJson(data, BusinessInfo.class);
        List<GoodsTypeInfo> list = businessInfo.getList();
        goodsTypeAdapter.setData(list);
        initGoodInfo();
        goodsAdapter.setData(goodsInfos);
    }

    private void initGoodInfo() {
        goodsInfos = new ArrayList<>();
        for (int i = 0; i < businessInfo.getList().size(); i++) {
            GoodsTypeInfo goodsTypeInfo = businessInfo.getList().get(i);
            for (int j = 0; j < goodsTypeInfo.getList().size(); j++) {
                GoodsInfo goodsInfo = goodsTypeInfo.getList().get(j);
                goodsInfo.setTypeName(goodsTypeInfo.getName());
                goodsInfo.setTypeId(goodsTypeInfo.getId());
                goodsInfo.setSellerId((int) seller.getId());
                goodsInfos.add(goodsInfo);
            }
        }
    }

    public void getBusinessData(Seller seller){
        Call<ResponseInfo> businessInfo = responseInfoApi.getBusinessInfo(seller.getId());
        businessInfo.enqueue(new CallBackAdapter());
    }
}
