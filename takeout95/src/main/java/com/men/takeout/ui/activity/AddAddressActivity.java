package com.men.takeout.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.men.takeout.R;
import com.men.takeout.global.MyApplication;
import com.men.takeout.model.dao.ReceiptAddressDao;
import com.men.takeout.model.dao.bean.ReceiptAddressBean;
import com.men.takeout.utils.SMSUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddAddressActivity extends BaseActivity {
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ib_delete)
    ImageButton ibDelete;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.et_name)
    EditText etName;
    @InjectView(R.id.rb_man)
    RadioButton rbMan;
    @InjectView(R.id.rb_women)
    RadioButton rbWomen;
    @InjectView(R.id.rg_sex)
    RadioGroup rgSex;
    @InjectView(R.id.et_phone)
    EditText etPhone;
    @InjectView(R.id.ib_delete_phone)
    ImageButton ibDeletePhone;
    @InjectView(R.id.ib_add_phone_other)
    ImageButton ibAddPhoneOther;
    @InjectView(R.id.et_phone_other)
    EditText etPhoneOther;
    @InjectView(R.id.ib_delete_phone_other)
    ImageButton ibDeletePhoneOther;
    @InjectView(R.id.rl_phone_other)
    RelativeLayout rlPhoneOther;
    @InjectView(R.id.tv_receipt_address)
    TextView tvReceiptAddress;
    @InjectView(R.id.et_detail_address)
    EditText etDetailAddress;
    @InjectView(R.id.tv_label)
    TextView tvLabel;
    @InjectView(R.id.ib_select_label)
    ImageView ibSelectLabel;
    @InjectView(R.id.bt_ok)
    Button btOk;
    private String[] labelArr = {"家", "公司", "学校"};
    private int[] bgColorArray = {Color.parseColor("#ff888888"),
            Color.parseColor("#ff663322"),
            Color.parseColor("#ff882266")
    };
    private ReceiptAddressDao dao;
    private ReceiptAddressBean addressBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_receipt_address);
        ButterKnife.inject(this);
        dao = new ReceiptAddressDao(this);
        addressBean = (ReceiptAddressBean) getIntent().getSerializableExtra("addressBean");
        if (addressBean != null) {
            initAddress(addressBean);
        }

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String etPhoneStr = etPhone.getText().toString();
                if (!TextUtils.isEmpty(etPhoneStr)) {
                    ibDeletePhone.setVisibility(View.VISIBLE);
                } else {
                    ibDeletePhone.setVisibility(View.GONE);
                }
            }
        });
        etPhoneOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String etPhoneOtherStr = etPhoneOther.getText().toString();
                if (!TextUtils.isEmpty(etPhoneOtherStr)) {
                    ibDeletePhoneOther.setVisibility(View.VISIBLE);
                } else {
                    ibDeletePhoneOther.setVisibility(View.GONE);
                }
            }
        });
        etPhone.setOnFocusChangeListener(new MyOnFocusChangeListener());
        etPhoneOther.setOnFocusChangeListener(new MyOnFocusChangeListener());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 101 && data != null) {
            String  title = data.getStringExtra("title");
            String snippet =data.getStringExtra("snippet");
            tvReceiptAddress.setText(title);

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void initAddress(ReceiptAddressBean addressBean) {
        String name = addressBean.getName();
        String phone = addressBean.getPhone();
        String sex = addressBean.getSex();
        if (sex.equals("先生")) {
//            rbMan.isChecked();
            rgSex.check(R.id.rb_man);
        } else {
//            rbWomen.isChecked();
            rgSex.check(R.id.rb_women);

        }
        String label = addressBean.getLabel();
        String receiptAddress = addressBean.getReceiptAddress();
        String detailAddress = addressBean.getDetailAddress();
        String phoneOther = addressBean.getPhoneOther();

        etName.setText(name);
        etPhone.setText(phone);
        if (!TextUtils.isEmpty(label)) {
            tvLabel.setVisibility(View.VISIBLE);
            tvLabel.setText(label);
            tvLabel.setBackgroundColor(bgColorArray[getIndex(label)]);
        } else {
            tvLabel.setVisibility(View.GONE);
        }
        tvReceiptAddress.setText(receiptAddress);
        etDetailAddress.setText(detailAddress);
        etPhoneOther.setText(phoneOther);

    }

    class MyOnFocusChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.getId() == R.id.et_phone) {
                String etPhoneStr = etPhone.getText().toString();

                if (!TextUtils.isEmpty(etPhoneStr) && hasFocus) {
                    ibDeletePhone.setVisibility(View.VISIBLE);
                } else {
                    ibDeletePhone.setVisibility(View.GONE);
                }

            } else if (v.getId() == R.id.et_phone_other) {
                String etPhoneOtherStr = etPhoneOther.getText().toString();

                if (!TextUtils.isEmpty(etPhoneOtherStr) && hasFocus) {
                    ibDeletePhoneOther.setVisibility(View.VISIBLE);
                } else {
                    ibDeletePhoneOther.setVisibility(View.GONE);
                }

            }
        }
    }


    @OnClick({R.id.et_phone, R.id.ib_add_phone_other, R.id.ib_delete_phone, R.id.ib_delete_phone_other, R.id.ib_select_label,R.id.bt_ok,R.id.ib_delete,
            R.id.tv_receipt_address
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_phone:

                break;
            case R.id.ib_add_phone_other:
                int visibility = rlPhoneOther.getVisibility();
                if (visibility == View.GONE) {
                    rlPhoneOther.setVisibility(View.VISIBLE);
                } else {
                    rlPhoneOther.setVisibility(View.GONE);
                }
                break;
            case R.id.ib_delete_phone:
                etPhone.setText("");
                break;
            case R.id.ib_delete_phone_other:
                etPhoneOther.setText("");
                break;
            case R.id.ib_select_label:
                setLabel();
                break;
            case R.id.bt_ok:
                if (checkData()) {
                    if (addressBean != null) {
                        editAddress();
                    } else {
                        addAddress();
                    }
                }
                break;
            case R.id.ib_delete:
                deleteAddress();
                finish();
                break;
            case R.id.tv_receipt_address:
                Intent intent = new Intent(this, AddressLocationActivity.class);
                startActivityForResult(intent,100);
                break;

        }
    }

    private void deleteAddress() {
        if (addressBean != null) {
            dao.delete(addressBean);
        }
    }


    private void editAddress() {
        String name = etName.getText().toString().trim();
        int checkedRadioButtonId = rgSex.getCheckedRadioButtonId();
        String sex = "";
        if (checkedRadioButtonId == R.id.rb_man) {
            sex = "先生";
        } else {
            sex = "小姐";
        }
        String phone = etPhone.getText().toString().trim();
        String otherPhone = etPhoneOther.getText().toString().trim();
        String receiptAddress = tvReceiptAddress.getText().toString().trim();
        String receiptDetailAddress = etDetailAddress.getText().toString().trim();
        String label = tvLabel.getText().toString().trim();
        addressBean.setName(name);
        addressBean.setPhone(phone);
        addressBean.setSex(sex);
        addressBean.setPhoneOther(otherPhone);
        addressBean.setReceiptAddress(receiptAddress);
        addressBean.setDetailAddress(receiptDetailAddress);
        addressBean.setLabel(label);
        dao.update(addressBean);
        finish();
    }

    private void addAddress() {
        String name = etName.getText().toString().trim();
        int checkedRadioButtonId = rgSex.getCheckedRadioButtonId();
        String sex = "";
        if (checkedRadioButtonId == R.id.rb_man) {
            sex = "先生";
        } else {
            sex = "小姐";
        }
        String phone = etPhone.getText().toString().trim();
        String otherPhone = etPhoneOther.getText().toString().trim();
        String receiptAddress = tvReceiptAddress.getText().toString().trim();
        String receiptDetailAddress = etDetailAddress.getText().toString().trim();
        String label = tvLabel.getText().toString().trim();
        ReceiptAddressBean bean = new ReceiptAddressBean(MyApplication.userId, name, sex, phone, otherPhone, receiptAddress, receiptDetailAddress, label, 0);

        dao.insert(bean);

        finish();
    }

    private void setLabel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddAddressActivity.this);
        builder.setTitle("请选择您的地点");
        builder.setItems(labelArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                tvLabel.setText(labelArr[which]);
                tvLabel.setBackgroundColor(bgColorArray[which]);
            }
        });
        builder.show();

    }

    private boolean checkData() {
        String name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请填写联系人", Toast.LENGTH_SHORT).show();
            return false;
        }
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请填写手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!SMSUtil.isMobileNO(phone)) {
            Toast.makeText(this, "请填写合法的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        /*String receiptAddress = tvReceiptAddress.getText().toString().trim();
        if (TextUtils.isEmpty(receiptAddress)) {
            Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
            return false;
        }*/
/*        String address = etDetailAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        int checkedRadioButtonId = rgSex.getCheckedRadioButtonId();
        if (checkedRadioButtonId != R.id.rb_man && checkedRadioButtonId != R.id.rb_women) {
            //2个不相等，则说明没有选中任意一个
            Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
            return false;
        }

        String tvLableString = tvLabel.getText().toString();
        if (TextUtils.isEmpty(tvLableString)) {
            Toast.makeText(this, "请输入标签信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
