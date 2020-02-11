package com.men.takeout.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.men.takeout.R;
import com.men.takeout.presenter.net.bean.GoodsInfo;
import com.men.takeout.presenter.net.bean.GoodsTypeInfo;
import com.men.takeout.ui.activity.BusinessActivity;
import com.men.takeout.utils.CountPriceFormater;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.ShopCartHolder> {


    private List<GoodsInfo> shopCartList;
    private Context mCtx;

    public ShopCartAdapter(Context mCtx, List<GoodsInfo> shopCartList) {
        this.mCtx = mCtx;
        this.shopCartList = shopCartList;
    }

    @Override
    public ShopCartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mCtx, R.layout.item_cart, null);
        ShopCartHolder shopCartHolder = new ShopCartHolder(view);
        return shopCartHolder;
    }

    @Override
    public void onBindViewHolder(ShopCartHolder holder, int position) {
        holder.tvName.setText(shopCartList.get(position).getName());
        int count = shopCartList.get(position).getCount();
        float newPrice = shopCartList.get(position).getNewPrice();
        String totalNewPrice = CountPriceFormater.format(newPrice * count);
        holder.tvTypeAllPrice.setText(totalNewPrice);
        holder.tvCount.setText(count + "");
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        if (shopCartList != null && shopCartList.size() > 0) {
            return shopCartList.size();
        }
        return 0;
    }

    public void setData(List<GoodsInfo> shopCartList) {
        this.shopCartList = shopCartList;
        notifyDataSetChanged();
    }


    class ShopCartHolder extends RecyclerView.ViewHolder {
        private static final int MINUS = 100;
        private static final int ADD = 101;

        @InjectView(R.id.tv_name)
        TextView tvName;
        @InjectView(R.id.tv_type_all_price)
        TextView tvTypeAllPrice;
        @InjectView(R.id.ib_minus)
        ImageButton ibMinus;
        @InjectView(R.id.tv_count)
        TextView tvCount;
        @InjectView(R.id.ib_add)
        ImageButton ibAdd;
        @InjectView(R.id.ll)
        LinearLayout ll;
        private int position;

        public ShopCartHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        @OnClick({R.id.ib_minus, R.id.ib_add})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ib_minus:
                    minus(MINUS);
                    break;
                case R.id.ib_add:
                    addGoods(ADD);
                    break;
            }
        }

        private void minus(int minus) {
            //右侧
            updateGoodsInfos(minus);

            //左侧
            updateGoodTypeInfos(minus);
            //购物车列表
            notifyDataSetChanged();
            //购物车数量
            ((BusinessActivity) mCtx).getBusinessPresenter().updateShopCart();
            int count = shopCartList.get(position).getCount();

            if (count == 0) {
                shopCartList.remove(position);
            }
            if (shopCartList.size() == 0) {
                ((BusinessActivity)mCtx).dismissBottomSheetLayout();
            }
        }

        private void addGoods(int add) {
            //右侧
            updateGoodsInfos(add);

            //左侧
            updateGoodTypeInfos(add);

            //购物车列表
            notifyDataSetChanged();
            //购物车数量
            ((BusinessActivity) mCtx).getBusinessPresenter().updateShopCart();
        }

        private void updateGoodTypeInfos(int type) {
            GoodsTypeAdapter goodsTypeAdapter = ((BusinessActivity) mCtx).getGoodsFragment().getGoodsTypeAdapter();
            GoodsInfo goodsInfo = shopCartList.get(position);
            int typeId = goodsInfo.getTypeId();
            List<GoodsTypeInfo> goodsTypeInfos = goodsTypeAdapter.getData();
            if (goodsTypeInfos != null && goodsTypeInfos.size() > 0) {
                for (int i = 0; i < goodsTypeInfos.size(); i++) {
                    if (typeId == goodsTypeInfos.get(i).getId()) {
                        int count = goodsTypeInfos.get(i).getCount();
                        if (type == ADD) {
                            goodsTypeInfos.get(i).setCount(count + 1);
                        } else {
                            if (count > 0) {
                                goodsTypeInfos.get(i).setCount(count - 1);
                            }
                        }

                        goodsTypeAdapter.notifyDataSetChanged();
                    }
                }
            }
        }

        private void updateGoodsInfos(int type) {
            GoodsAdapter goodsAdapter = ((BusinessActivity) mCtx).getGoodsFragment().getGoodsAdapter();
            ArrayList<GoodsInfo> goodsInfos = goodsAdapter.getGoodsInfos();
            int id = shopCartList.get(position).getId();
            if (goodsInfos != null && goodsInfos.size() > 0) {
                for (int i = 0; i < goodsInfos.size(); i++) {
                    if (id == goodsInfos.get(i).getId()) {
                        int count = goodsInfos.get(i).getCount();
                        if (type == ADD) {
                            goodsInfos.get(i).setCount(count + 1);
                        } else {
                            if (count > 0) {
                                goodsInfos.get(i).setCount(count - 1);
                            }
                        }
                        goodsAdapter.notifyDataSetChanged();
                    }
                }
            }
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}

