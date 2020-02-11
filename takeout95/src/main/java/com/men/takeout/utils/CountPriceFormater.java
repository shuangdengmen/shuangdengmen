package com.men.takeout.utils;

import java.text.NumberFormat;

/**
 * 格式化总价格的工具类  自带￥   保留两位小数
 */
public class CountPriceFormater {

    private CountPriceFormater(){

    }

    public static String format(float countPrice){
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        return format.format(countPrice);
    }

}
