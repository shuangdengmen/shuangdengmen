package com.men.takeout.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.men.takeout.R;
import com.men.takeout.presenter.OrderPresenter;
import com.men.takeout.ui.adapter.OrderAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by HASEE on 2017/1/9.
 */
public class OrderFragment extends BaseFragment  {

    @InjectView(R.id.rv_order_list)
    RecyclerView rvOrderList;
    @InjectView(R.id.srl_order)
    SwipeRefreshLayout srlOrder;
    private OrderPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new OrderPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        presenter.getOrderList(MyApplication.userId);
        OrderAdapter orderAdapter = new OrderAdapter(getActivity());
        rvOrderList.setAdapter(orderAdapter);
        rvOrderList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        presenter.setOrderAdapter(orderAdapter);
        presenter.getOrderList(1);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
