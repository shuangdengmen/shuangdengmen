package com.men.takeout.model.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.men.takeout.model.dao.bean.ReceiptAddressBean;

import java.sql.SQLException;
import java.util.List;

public class ReceiptAddressDao {

    private  Dao dao;

    public ReceiptAddressDao(Context ctx) {
        if (dao == null) {
            dao = DBHelper.getInstance(ctx).getDao(ReceiptAddressBean.class);
        }
    }

    public void insert(ReceiptAddressBean receiptAddressBean) {
        try {
            dao.create(receiptAddressBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(ReceiptAddressBean receiptAddressBean) {
        try {
            dao.delete(receiptAddressBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ReceiptAddressBean receiptAddressBean) {
        try {
            dao.update(receiptAddressBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  List<ReceiptAddressBean>  queryAll(int userId) {
        List<ReceiptAddressBean> list =null;
        try {
            list = dao.queryBuilder().where().eq("uid", userId).query();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ReceiptAddressBean> querySelection(int userId) {
        List<ReceiptAddressBean> select=null;
        QueryBuilder queryBuilder = dao.queryBuilder();
        try {
            select = queryBuilder.where().eq("isSelect", 1).and().eq("uid",userId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return select;
    }
}
