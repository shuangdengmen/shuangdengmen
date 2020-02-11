package com.men.takeout.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.men.takeout.R;
import com.men.takeout.presenter.GoodsPresenter;
import com.men.takeout.presenter.net.bean.GoodsInfo;
import com.men.takeout.presenter.net.bean.GoodsTypeInfo;
import com.men.takeout.presenter.net.bean.Seller;
import com.men.takeout.ui.adapter.GoodsAdapter;
import com.men.takeout.ui.adapter.GoodsTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class GoodsFragment extends BaseFragment {

    @InjectView(R.id.rv_goods_type)
    RecyclerView rvGoodsType;
    @InjectView(R.id.slhlv)
    StickyListHeadersListView slhlv;
    private Seller seller;

    public GoodsAdapter getGoodsAdapter() {
        return goodsAdapter;
    }

    private GoodsAdapter goodsAdapter;
    private GoodsTypeAdapter goodsTypeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        goodsTypeAdapter = new GoodsTypeAdapter(this);
        rvGoodsType.setAdapter(goodsTypeAdapter);
        rvGoodsType.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        goodsAdapter = new GoodsAdapter(getActivity());
        slhlv.setAdapter(goodsAdapter);
        slhlv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ArrayList<GoodsInfo> goodsInfos = goodsAdapter.getGoodsInfos();


                List<GoodsTypeInfo> goodsTypeInfos = goodsTypeAdapter.getData();

                if (goodsInfos != null && goodsTypeInfos != null && goodsInfos.size() > 0 && goodsTypeInfos.size() > 0) {
                    GoodsInfo goodsInfo = goodsInfos.get(firstVisibleItem);
                    for (int i = 0; i <  goodsTypeInfos.size(); i++) {
                        if (goodsTypeInfos.get(i).getId()==goodsInfo.getTypeId()){
                            goodsTypeAdapter.setCurrentPosition(i);
                            goodsTypeAdapter.notifyDataSetChanged();
                            rvGoodsType.smoothScrollToPosition(i);
                            break;
                        }
                    }
                }



            }
        });

        GoodsPresenter presenter = new GoodsPresenter(goodsTypeAdapter, goodsAdapter,seller);
        presenter.getBusinessData(seller);
//        presenter.set
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        seller = (Seller) bundle.getSerializable("seller");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void setGoodsTypeId(int goodsTypeId) {
        ArrayList<GoodsInfo> goodsInfos = goodsAdapter.getGoodsInfos();

        if (goodsInfos!=null&&goodsInfos.size()>0) {
            for (int i = 0; i < goodsInfos.size(); i++) {
                if (goodsInfos.get(i).getTypeId() == goodsTypeId) {
                    slhlv.setSelection(i);
                    break;
                }
            }
        }


    }

    public GoodsTypeAdapter getGoodsTypeAdapter() {
        return goodsTypeAdapter;
    }
}
