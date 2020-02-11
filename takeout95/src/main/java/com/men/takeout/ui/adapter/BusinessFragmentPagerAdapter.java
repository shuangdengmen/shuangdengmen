package com.men.takeout.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.men.takeout.presenter.net.bean.Seller;
import com.men.takeout.ui.fragment.BaseFragment;
import com.men.takeout.ui.fragment.GoodsFragment;
import com.men.takeout.ui.fragment.SellerFragment;
import com.men.takeout.ui.fragment.SuggestFragment;

import java.util.ArrayList;

public class BusinessFragmentPagerAdapter extends FragmentPagerAdapter {

    private Seller seller;
    private String[] arrayString;
    private ArrayList<Fragment> fragmentlist =null;
    public BusinessFragmentPagerAdapter(FragmentManager fm,String[] arrayString,Seller seller) {
        super(fm);
        this.arrayString = arrayString;
        fragmentlist = new ArrayList<>();
        this.seller = seller;
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment baseFragment =null;

        switch(position){
            case 0 :
                baseFragment = new GoodsFragment();
                break;
            case 1 :
                baseFragment = new SuggestFragment();
                break;
            case 2 :
                baseFragment = new SellerFragment();
                break;
        }
        if (!fragmentlist.contains(baseFragment)) {
            fragmentlist.add(baseFragment);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("seller",seller);
        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    @Override
    public int getCount() {
        return arrayString.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        return arrayString[position];
    }

    public ArrayList<Fragment> getFragmentList(){
        return fragmentlist;
    }
}
