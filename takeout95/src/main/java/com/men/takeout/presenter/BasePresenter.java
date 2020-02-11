package com.men.takeout.presenter;

import com.men.takeout.presenter.net.ResponseInfoApi;
import com.men.takeout.presenter.net.bean.ResponseInfo;
import com.men.takeout.utils.Constant;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BasePresenter {

    private final Map<String, String> errorMap;
    public final ResponseInfoApi responseInfoApi;

    public BasePresenter(){
        errorMap = new HashMap<>();
        errorMap.put("1","具体的错误类型,此页数据没有更新");
        errorMap.put("2","具体的错误类型,服务器忙,请稍后重试");
        errorMap.put("3","具体的错误类型,服务器挂掉 ");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
        responseInfoApi = retrofit.create(ResponseInfoApi.class);
    }



    class  CallBackAdapter implements Callback<ResponseInfo>{

        @Override
        public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
            ResponseInfo body = response.body();
            if (body.getCode().equals("0")) {
                String data = body.getData();
                parseGson(data);
            } else {
                onFailure(call,new RuntimeException(errorMap.get(body.getCode())));
            }
        }

        @Override
        public void onFailure(Call<ResponseInfo> call, Throwable t) {
            showError(t.getMessage());
        }
    }

    protected abstract void showError(String message);

    public abstract void parseGson(String data) ;


}
