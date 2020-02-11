package com.men.takeout.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.men.takeout.presenter.net.bean.Order;
import com.men.takeout.presenter.net.bean.ResponseInfo;
import com.men.takeout.ui.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class OrderPresenter extends BasePresenter {
    private OrderAdapter orderAdapter;

    public void setOrderAdapter(OrderAdapter orderAdapter) {
        this.orderAdapter = orderAdapter;
    }

    @Override
    protected void showError(String message) {

    }

    @Override
    public void parseGson(String data) {
        ArrayList<Order> orders= new Gson().fromJson(data,new TypeToken<List<Order>>(){}.getType());
        orders.get(0);
        orderAdapter.setData(orders);
    }

    public void getOrderList(int userId) {
        Call<ResponseInfo> orderData = responseInfoApi.getOrderData(userId);
        orderData.enqueue(new CallBackAdapter());
    }
}
