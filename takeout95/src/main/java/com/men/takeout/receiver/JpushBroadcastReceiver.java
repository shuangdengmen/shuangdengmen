package com.men.takeout.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.men.takeout.observer.OrderObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

public class JpushBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String orderId;
        String typeId;
        HashMap<String, String> map =new HashMap();
        if (extra == null) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(extra);
            if (jsonObject.has("orderId")) {
                orderId = (String) jsonObject.get("orderId");
                map.put("orderId", orderId);
            }
            if (jsonObject.has("typeId")) {
                typeId = (String) jsonObject.get("typeId");
                map.put("typeId", typeId);
            }

            if (jsonObject.has("lat")) {
                String lat = jsonObject.getString("lat");
                map.put("lat", lat);
            }
            if (jsonObject.has("lng")) {
                String lng = jsonObject.getString("lng");
                map.put("lng", lng);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OrderObserver instance = OrderObserver.getInstance();
        instance.changeUI(map);
//        Log.d("JpushBroadcastReceiver", "onReceive: 接收到消息 通知标题 "+title);

    }
}
