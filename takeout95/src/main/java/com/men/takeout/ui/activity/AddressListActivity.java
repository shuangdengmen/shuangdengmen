package com.men.takeout.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.men.takeout.R;
import com.men.takeout.global.MyApplication;
import com.men.takeout.model.dao.ReceiptAddressDao;
import com.men.takeout.model.dao.bean.ReceiptAddressBean;
import com.men.takeout.ui.adapter.AddressListAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddressListActivity extends BaseActivity {

    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.rv_receipt_address)
    RecyclerView rvReceiptAddress;
    @InjectView(R.id.tv_add_address)
    TextView tvAddAddress;
    private ReceiptAddressDao dao;
    private ReceiptAddressBean addressBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_address);
        ButterKnife.inject(this);
        dao = new ReceiptAddressDao(this);


    }



    @Override
    protected void onResume() {
        super.onResume();
        List<ReceiptAddressBean> addressList = dao.queryAll(MyApplication.userId);
        rvReceiptAddress.setAdapter(new AddressListAdapter(this,addressList));
        rvReceiptAddress.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

    }


    @OnClick(R.id.tv_add_address)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_address:
                Intent intent = new Intent(AddressListActivity.this, AddAddressActivity.class);
                startActivity(intent);
                break;
        }
    }


}
