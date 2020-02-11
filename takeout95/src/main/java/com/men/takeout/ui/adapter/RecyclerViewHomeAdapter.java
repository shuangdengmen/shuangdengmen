package com.men.takeout.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.men.takeout.R;
import com.men.takeout.presenter.net.bean.HomeInfo;
import com.men.takeout.presenter.net.bean.HomeItem;
import com.men.takeout.presenter.net.bean.Promotion;
import com.men.takeout.ui.activity.BusinessActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecyclerViewHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_HEAD = 0;
    public static final int ITEM_SELLER = 1;
    public static final int ITEM_DIV = 2;



    private HomeInfo homeInfo;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_HEAD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
            TitleViewHolder holder = new TitleViewHolder(view);
            return holder;
        } else if (viewType == ITEM_SELLER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller, parent, false);
            SellerViewHolder holer = new SellerViewHolder(view);
            return holer;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_division, parent, false);
            DivisionViewHolder holder = new DivisionViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case (ITEM_HEAD):
                break;
            case ITEM_SELLER:
                setSellerData(holder,homeInfo.getBody().get(position-1));
                ((SellerViewHolder) holder).setPosition(position - 1);
                break;
            case  ITEM_DIV:
                setDivisionData(holder, homeInfo.getBody().get(position - 1));
                break;
        }
    }

    private void setDivisionData(RecyclerView.ViewHolder holder, HomeItem homeItem) {
        ((DivisionViewHolder)holder).tv1.setText(homeItem.recommendInfos.get(0));
        ((DivisionViewHolder)holder).tv2.setText(homeItem.recommendInfos.get(1));
        ((DivisionViewHolder)holder).tv3.setText(homeItem.recommendInfos.get(2));
        ((DivisionViewHolder)holder).tv4.setText(homeItem.recommendInfos.get(3));
        ((DivisionViewHolder)holder).tv5.setText(homeItem.recommendInfos.get(4));
        ((DivisionViewHolder)holder).tv6.setText(homeItem.recommendInfos.get(5));
    }

    private void setSellerData(RecyclerView.ViewHolder holder, HomeItem homeItem) {
        ((SellerViewHolder) holder).tvTitle.setText(homeItem.seller.getName());
    }


    @Override
    public int getItemCount() {

        if (homeInfo != null && homeInfo.getBody() != null && homeInfo.getHead() != null && homeInfo.getBody().size() > 0) {
            return homeInfo.getBody().size() + 1;
        }
        return 0;
    }


    class TitleViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.slider)
        SliderLayout slider;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            ArrayList<Promotion> promotionList = homeInfo.getHead().getPromotionList();

            for (Promotion promotion : promotionList) {
                TextSliderView textSliderView = new TextSliderView(itemView.getContext());
                // initialize a SliderLayout
                textSliderView
                        .description(promotion.getInfo())
                        .image(promotion.getPic())
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                slider.addSlider(textSliderView);
            }
            slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slider.setCustomAnimation(new DescriptionAnimation());
            slider.setDuration(4000);
        }
    }

    class SellerViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tvCount)
        TextView tvCount;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.ratingBar)
        RatingBar ratingBar;
        private int position;

        public SellerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),BusinessActivity.class);
                    intent.putExtra("seller", homeInfo.getBody().get(position).seller);
                    v.getContext().startActivity(intent);
                }
            });

        }

        public void setPosition(int position) {
            this.position=position;
        }
    }

    class DivisionViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_division_title)
        TextView tvDivisionTitle;
        @InjectView(R.id.tv1)
        TextView tv1;
        @InjectView(R.id.tv2)
        TextView tv2;
        @InjectView(R.id.tv3)
        TextView tv3;
        @InjectView(R.id.tv4)
        TextView tv4;
        @InjectView(R.id.tv5)
        TextView tv5;
        @InjectView(R.id.tv6)
        TextView tv6;
        public DivisionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

        }
    }

    public void setData(HomeInfo homeInfo) {
        this.homeInfo = homeInfo;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_HEAD;
        } else if (homeInfo.getBody().get(position - 1).type == 0) {
            return ITEM_SELLER;
        } else {
            return ITEM_DIV;
        }
    }
}

