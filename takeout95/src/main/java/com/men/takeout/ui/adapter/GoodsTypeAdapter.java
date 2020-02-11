package com.men.takeout.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.men.takeout.R;
import com.men.takeout.presenter.net.bean.GoodsTypeInfo;
import com.men.takeout.ui.fragment.GoodsFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GoodsTypeAdapter extends RecyclerView.Adapter<GoodsTypeAdapter.GoodsTypeHolder> {

    private GoodsFragment goodsFragment;
    private List<GoodsTypeInfo> list;

    private int currentPosition = 0;

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }


    public int getCurrentPosition() {
        return currentPosition;
    }

    public GoodsTypeAdapter(GoodsFragment goodsFragment) {
        this.goodsFragment = goodsFragment;
    }

    @Override
    public GoodsTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_type, null);
        GoodsTypeHolder holder = new GoodsTypeHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GoodsTypeHolder holder, int position) {
        holder.type.setText(list.get(position).getName());
        if (list.get(position).getCount() > 0) {
            holder.tvCount.setVisibility(View.VISIBLE);
            holder.tvCount.setText(String.valueOf(list.get(position).getCount()));
        } else {
            holder.tvCount.setVisibility(View.INVISIBLE);
        }

        if (position == currentPosition) {
            holder.type.setTextColor(Color.RED);
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.type.setTextColor(Color.BLACK);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0) {
            return list.size();
        }
        return 0;
    }


    public void setData(List<GoodsTypeInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<GoodsTypeInfo> getData() {
        return list;
    }


    class GoodsTypeHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tvCount)
        TextView tvCount;
        @InjectView(R.id.type)
        TextView type;
        private int position;

        public GoodsTypeHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = position;
                    notifyDataSetChanged();
                    goodsFragment.setGoodsTypeId(list.get(position).getId());
                }
            });
        }


        public void setPosition(int position) {
            this.position = position;
        }
    }

    public void refreshGoodsType(int typeId, int operate) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == typeId) {
                switch (operate) {

                    case GoodsAdapter.ADD:
                        int countAdd = list.get(i).getCount() + 1;
                        list.get(i).setCount(countAdd);
                        break;
                    case GoodsAdapter.DELETE:
                        if (list.get(i).getCount() > 0) {
                            int countDelete = list.get(i).getCount() - 1;
                            list.get(i).setCount(countDelete);

                        }
                        break;
                }
                notifyDataSetChanged();
                break;
            }

        }
    }
}
