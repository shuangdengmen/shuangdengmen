package com.men.takeout.ui.fragment;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.men.takeout.R;
import com.men.takeout.presenter.HomePresenter;
import com.men.takeout.ui.adapter.RecyclerViewHomeAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeFragment extends Fragment {

    @InjectView(R.id.rv_home)
    RecyclerView rvHome;
    @InjectView(R.id.home_tv_address)
    TextView homeTvAddress;
    @InjectView(R.id.ll_title_search)
    LinearLayout llTitleSearch;
    @InjectView(R.id.ll_title_container)
    LinearLayout llTitleContainer;
    private int sumY;
    private float duration = 300.0f;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerViewHomeAdapter rvHomeAdapter = new RecyclerViewHomeAdapter();
        rvHome.setAdapter(rvHomeAdapter);
        rvHome.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int bgColor=0x553190e8;
                ArgbEvaluator evaluator = new ArgbEvaluator();
                sumY += dy;
                if (sumY < 0) {
                    bgColor = 0x553190e8;
                } else if (sumY >= 300) {
                    bgColor = 0xff3190e8;
                } else {
                    bgColor = (int) evaluator.evaluate(sumY / duration, 0x553190e8, 0xff3190e8);
                }
                llTitleContainer.setBackgroundColor(bgColor);
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        HomePresenter presenter = new HomePresenter(rvHomeAdapter);
        presenter.getHomeData("","");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
