package com.men.takeout.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.men.takeout.R;
import com.men.takeout.presenter.net.bean.GoodsInfo;
import com.men.takeout.ui.activity.BusinessActivity;
import com.men.takeout.ui.fragment.GoodsFragment;
import com.men.takeout.utils.CountPriceFormater;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;



public class GoodsAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    public static final int DELETE = 101;
    public static final int ADD = 100;
    private Context mCtt = null;
    private ArrayList<GoodsInfo> goodsInfos;
    private int operate =ADD;


    public GoodsAdapter(Context context) {
        this.mCtt = context;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(parent.getContext(), R.layout.item_type_header, null);
        textView.setText(goodsInfos.get(position).getTypeName());
        return textView;
    }

    @Override
    public long getHeaderId(int position) {
        return goodsInfos.get(position).getTypeId();
    }

    @Override
    public int getCount() {
        if (goodsInfos != null && goodsInfos.size() > 0) {
            return goodsInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return goodsInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_goods, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(goodsInfos.get(position).getName());
        viewHolder.tvOldprice.setText(CountPriceFormater.format(goodsInfos.get(position).getOldPrice()));
        viewHolder.tvNewprice.setText(CountPriceFormater.format(goodsInfos.get(position).getNewPrice()));
        Picasso.with(parent.getContext()).load(goodsInfos.get(position).getIcon()).into(viewHolder.ivIcon);
        if (goodsInfos.get(position).getCount() > 0) {
            viewHolder.ibMinus.setVisibility(View.VISIBLE);
            viewHolder.tvCount.setVisibility(View.VISIBLE);
            viewHolder.tvCount.setText(goodsInfos.get(position).getCount()+"");
        } else {
            viewHolder.ibMinus.setVisibility(View.GONE);
            viewHolder.tvCount.setVisibility(View.GONE);

        }
        viewHolder.setPosition(position);
        return convertView;
    }

    public void setData(ArrayList<GoodsInfo> goodsInfos) {
        this.goodsInfos = goodsInfos;
        notifyDataSetChanged();
    }

    public ArrayList<GoodsInfo> getGoodsInfos() {
        return goodsInfos;
    }

    class ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_name)
        TextView tvName;
        @InjectView(R.id.tv_zucheng)
        TextView tvZucheng;
        @InjectView(R.id.tv_yueshaoshounum)
        TextView tvYueshaoshounum;
        @InjectView(R.id.tv_newprice)
        TextView tvNewprice;
        @InjectView(R.id.tv_oldprice)
        TextView tvOldprice;
        @InjectView(R.id.ib_minus)
        ImageButton ibMinus;
        @InjectView(R.id.tv_count)
        TextView tvCount;
        @InjectView(R.id.ib_add)
        ImageButton ibAdd;
        private int position;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        @OnClick({R.id.ib_add,R.id.ib_minus})
        public void onClick(View view) {
            view.setEnabled(false);
            switch(view.getId()){
                case R.id.ib_add :
                    operate =ADD;
                    addGoods(view);
                    break;
                case R.id.ib_minus :
                    operate=DELETE;
                    deleteGoods(view);
                    break;
                default:
                    break;
            }
            GoodsFragment goodsFragment = ((BusinessActivity) mCtt).getGoodsFragment();
            if (goodsFragment != null) {
                int typeId = goodsInfos.get(position).getTypeId();
                GoodsTypeAdapter goodsTypeAdapter = goodsFragment.getGoodsTypeAdapter();
                goodsTypeAdapter.refreshGoodsType(typeId, operate);
            }
            ((BusinessActivity) mCtt).getBusinessPresenter().updateShopCart();

        }


        private void deleteGoods(final View view) {
            if (goodsInfos.get(position).getCount() == 1) {
                RotateAnimation rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 2,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(rotateAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.addAnimation(alphaAnimation);
                animationSet.setDuration(500);
            ibMinus.startAnimation(animationSet);
//                ibMinus.setAnimation(animationSet);
//                animationSet.start();
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        goodsInfos.get(position).setCount(0);
                        ibMinus.setVisibility(View.GONE);
                        tvCount.setVisibility(View.GONE);
                        ((BusinessActivity) mCtt).getBusinessPresenter().updateShopCart();
                        notifyDataSetChanged();
                        view.setEnabled(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

//                notifyDataSetChanged();
            } else if (goodsInfos.get(position).getCount() > 1) {
                 int count = goodsInfos.get(position).getCount()-1;
                 goodsInfos.get(position).setCount(count);
                 notifyDataSetChanged();
                 view.setEnabled(true);
            }

        }
        private void addGoods(View v) {

            if (goodsInfos.get(position).getCount() == 0) {


                RotateAnimation rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 2, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(rotateAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.addAnimation(alphaAnimation);
                animationSet.setDuration(1000);
                ibMinus.setAnimation(animationSet);
//            ibMinus.startAnimation(animationSet);
                animationSet.start();
                ibMinus.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.VISIBLE);
            }

            int[] sourceLocation = new int[2];
            v.getLocationInWindow(sourceLocation);
            ImageView imageView = new ImageView(v.getContext());
            imageView.setImageResource(R.drawable.button_add);
            imageView.setX(sourceLocation[0]);
//            imageView.setY(sourceLocation[1]- MyApplication.statusBarHeight);
            imageView.setY(sourceLocation[1]);

            ((BusinessActivity) mCtt).setImageView(imageView, v.getWidth(), v.getHeight());
            int[] desLocation = ((BusinessActivity) mCtt).getShopCartLocation();
            move(imageView, sourceLocation, desLocation, v);
            int count = goodsInfos.get(position).getCount();
            goodsInfos.get(position).setCount(count+1);
            notifyDataSetChanged();
        }

        private void move(final ImageView imageView, int[] sourceLocation, int[] desLocation, final View v) {
            //起点x坐标
            int startX = sourceLocation[0];
            //起点y坐标
            int startY = sourceLocation[1];

            //终点x坐标
            int endX = desLocation[0];
            //终点y坐标
            int endY = desLocation[1];

            //构建x轴的平移动画,匀速
            TranslateAnimation translateAnimationX = new TranslateAnimation(
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, endX - startX,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0
            );
            //x轴匀速运动
            translateAnimationX.setInterpolator(new LinearInterpolator());

            //构建y轴的平移动画,加速
            TranslateAnimation translateAnimationY = new TranslateAnimation(
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, endY - startY
            );
            //y轴做加速运动
            translateAnimationY.setInterpolator(new AccelerateInterpolator());

            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(translateAnimationX);
            animationSet.addAnimation(translateAnimationY);
            animationSet.setDuration(500);

            imageView.startAnimation(animationSet);

            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setEnabled(true);
                    ((BusinessActivity) mCtt).removeImageView(imageView);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }


}
