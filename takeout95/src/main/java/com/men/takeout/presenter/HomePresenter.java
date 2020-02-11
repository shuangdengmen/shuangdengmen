package com.men.takeout.presenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.men.takeout.presenter.net.bean.HomeInfo;
import com.men.takeout.presenter.net.bean.ResponseInfo;
import com.men.takeout.ui.adapter.RecyclerViewHomeAdapter;

import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePresenter extends BasePresenter {

    private final RecyclerViewHomeAdapter adapter;

    public HomePresenter(RecyclerViewHomeAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void showError(String message) {

    }

    @Override
    public void parseGson(String data) {
//        Log.d("parseGson", data);
        HomeInfo homeInfo = new Gson().fromJson(data, HomeInfo.class);
        adapter.setData(homeInfo);
    }

    public void getHomeData(String lat,String lon) {
        Call<ResponseInfo> homeResponseInfo = responseInfoApi.getHomeResponseInfo(lat, lon);
        homeResponseInfo.enqueue(new CallBackAdapter());
    }
}
