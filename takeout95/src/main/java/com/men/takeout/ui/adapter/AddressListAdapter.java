package com.men.takeout.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.men.takeout.R;
import com.men.takeout.model.dao.ReceiptAddressDao;
import com.men.takeout.model.dao.bean.ReceiptAddressBean;
import com.men.takeout.ui.activity.AddAddressActivity;
import com.men.takeout.ui.activity.AddressListActivity;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressListHolder> {
    private final List<ReceiptAddressBean> addressList;
    private final Context ctx;
    private String[] labelArr = {"家", "公司", "学校"};
    private int[] bgColorArray = {Color.parseColor("#ff888888"),
            Color.parseColor("#ff663322"),
            Color.parseColor("#ff882266")
    };
    private ReceiptAddressDao dao;

    public AddressListAdapter(Context ctx, List<ReceiptAddressBean> addressList) {
        this.addressList = addressList;
        this.ctx = ctx;
    }

    @Override
    public AddressListAdapter.AddressListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_receipt_address, null);
        AddressListHolder holder = new AddressListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AddressListAdapter.AddressListHolder holder, int position) {
        ReceiptAddressBean addressBean = addressList.get(position);

        holder.tvName.setText(addressBean.getName());
        holder.tvSex.setText(addressBean.getSex());
        holder.tvAddress.setText(addressBean.getReceiptAddress() + addressBean.getDetailAddress());
        holder.tvPhone.setText(addressBean.getPhone());
        String label = addressBean.getLabel();
        holder.tvLabel.setText(label);
        int index = getIndex(label);
        if (!TextUtils.isEmpty(label)) {
            holder.tvLabel.setVisibility(View.VISIBLE);
            holder.tvLabel.setBackgroundColor(bgColorArray[index]);
        } else {
            holder.tvLabel.setVisibility(View.GONE);
        }

        ReceiptAddressBean address = addressList.get(position);
        if (address.isSelect() == 1) {
            holder.cb.setVisibility(View.VISIBLE);
            holder.cb.setChecked(true);
        } else {
            holder.cb.setVisibility(View.INVISIBLE);
            holder.cb.setChecked(false);

        }
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        if (addressList != null && addressList.size() > 0) {
            return addressList.size();
        }
        return 0;
    }

    class AddressListHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.cb)
        CheckBox cb;
        @InjectView(R.id.tv_name)
        TextView tvName;
        @InjectView(R.id.tv_sex)
        TextView tvSex;
        @InjectView(R.id.tv_phone)
        TextView tvPhone;
        @InjectView(R.id.tv_label)
        TextView tvLabel;
        @InjectView(R.id.tv_address)
        TextView tvAddress;
        @InjectView(R.id.iv_edit)
        ImageView ivEdit;
        private int position;

        public AddressListHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            if (dao == null) {
                dao = new ReceiptAddressDao(itemView.getContext());
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < addressList.size(); i++) {
                        ReceiptAddressBean addressBean = addressList.get(i);

                        if (position == i) {
                            addressBean.setSelect(1);
                            dao.update(addressBean);
                        } else {
                            addressBean.setSelect(0);
                            dao.update(addressBean);
                        }
                    }
                    notifyDataSetChanged();
                    Intent intent = new Intent();
                    intent.putExtra("addressBean", (Serializable) addressList.get(position));
                    ((AddressListActivity) ctx).setResult(101, intent);
                    ((AddressListActivity) ctx).finish();
                }
            });
            ivEdit.setVisibility(View.VISIBLE);
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @OnClick(R.id.iv_edit)
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.iv_edit:
                    Intent intent = new Intent(ctx, AddAddressActivity.class);
                    ReceiptAddressBean addressBean = addressList.get(position);

                    intent.putExtra("addressBean", addressBean);
                    ((AddressListActivity)ctx).startActivity(intent);
                    break;
            }
        }
    }

    private int getIndex(String label) {
        for (int i = 0; i < labelArr.length; i++) {
            if (labelArr[i].equals(label)) {
                return i;
            }
        }

        return 0;
    }

}
