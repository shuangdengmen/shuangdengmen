package com.men.takeout.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.men.takeout.R;
import com.men.takeout.observer.OrderObserver;
import com.men.takeout.presenter.net.bean.GoodsInfo;
import com.men.takeout.presenter.net.bean.Order;
import com.men.takeout.presenter.net.bean.Seller;
import com.men.takeout.utils.CountPriceFormater;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> implements Observer {


    private ArrayList<Order> orders;
    private Context ctx;


    public OrderAdapter(Context ctx) {
        this.ctx = ctx;
        OrderObserver.getInstance().addObserver(this);
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(ctx, R.layout.item_order_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        OrderViewHolder holder = new OrderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        Seller seller = order.getSeller();
        String pic = seller.getPic();
        Picasso.with(ctx).load(pic).into(holder.ivOrderItemSellerLogo);
        holder.tvOrderItemSellerName.setText(seller.getName());
        float totalPrice=0.0f;
        String firstGoodsInfoName="";
        int count = order.getGoodsInfos().size();
        for (int i = 0; i <order.getGoodsInfos().size() ; i++) {
            if (i == 0) {
                firstGoodsInfoName = order.getGoodsInfos().get(0).getName();
            }
            GoodsInfo goodsInfo = order.getGoodsInfos().get(i);
            totalPrice += goodsInfo.getNewPrice();

        }
        holder.tvOrderItemMoney.setText(CountPriceFormater.format(totalPrice));
        holder.tvOrderItemFoods.setText(firstGoodsInfoName+"等"+count+"件商品");
        holder.tvOrderItemType.setText(getIndex(order.getType()));

        holder.setPosition(position);
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
     public String getIndex(String type) {
         String state = "";
         switch (type) {
             case ORDERTYPE_UNPAYMENT:
                 state= "未支付";
                 break;
             case ORDERTYPE_SUBMIT :
                 state= "已提交订单";
                 break;
             case ORDERTYPE_RECEIVEORDER :
                 state= "商家接单";
                 break;
             case ORDERTYPE_DISTRIBUTION :
                 state = "配送中";
                 break;
             case ORDERTYPE_SERVED :
                 state = "已送达";
                 break;
             case ORDERTYPE_CANCELLEDORDER :
                 state="取消的订单";
                 break;

         }

         return state;

    }

    @Override
    public int getItemCount() {
        if (orders != null && orders.size() > 0) {
            return orders.size();
        }
        return 0;
    }

    public void setData(ArrayList<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @Override
    public void update(Observable o, Object arg) {
        String orderId = (String) ((HashMap) arg).get("orderId");
        String typeId = (String) ((HashMap) arg).get("typeId");
        int position=-1;
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            String id = order.getId();
            if (id.equals(orderId)) {
                position = i;
                order.setType(typeId);
                break;
            }
        }
        notifyItemChanged(position);
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_order_item_seller_logo)
        ImageView ivOrderItemSellerLogo;
        @InjectView(R.id.tv_order_item_seller_name)
        TextView tvOrderItemSellerName;
        @InjectView(R.id.tv_order_item_type)
        TextView tvOrderItemType;
        @InjectView(R.id.tv_order_item_time)
        TextView tvOrderItemTime;
        @InjectView(R.id.tv_order_item_foods)
        TextView tvOrderItemFoods;
        @InjectView(R.id.tv_order_item_money)
        TextView tvOrderItemMoney;
        @InjectView(R.id.tv_order_item_multi_function)
        TextView tvOrderItemMultiFunction;
        private int position;

        public OrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, OrderDetailActivity.class);
                    intent.putExtra("orderId", orders.get(position).getId());
                    intent.putExtra("typeId", orders.get(position).getType());
                    ctx.startActivity(intent);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

}


