package com.men.takeout.presenter.net;

import com.men.takeout.presenter.net.bean.ResponseInfo;
import com.men.takeout.utils.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ResponseInfoApi {

    @GET(Constant.HOME_URL)
    Call<ResponseInfo> getHomeResponseInfo(@Query("latitude") String latitude, @Query("longitude") String longitude);
    @GET(Constant.BUSINESS_URL)
    Call<ResponseInfo> getBusinessInfo(@Query("sellerId") long sellerId);
    @GET(Constant.LOGIN)
    Call<ResponseInfo> getLoginData(@Query("username") String username,@Query("password") String password,@Query("phone") String phone,@Query("type") int type);

    @GET(Constant.ORDER)
    Call<ResponseInfo> getOrderData(@Query("userId") int userId);
}
