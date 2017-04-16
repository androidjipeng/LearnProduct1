package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.AddressBean;
import com.wangyi.lelegou.maofengqi.bean.CityBean;
import com.wangyi.lelegou.maofengqi.bean.DistrictBean;
import com.wangyi.lelegou.maofengqi.bean.JsonMsgBean;
import com.wangyi.lelegou.maofengqi.bean.ProvinceBean;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.ui.activity.AddressManagerActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.EditAddressActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.SettingAddressModel;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.utils.VerificationUtils;
import com.wangyi.lelegou.maofengqi.view.CustomClearEditText;
import com.wangyi.lelegou.maofengqi.view.adapter.CommonAdapter;
import com.wangyi.lelegou.maofengqi.view.adapter.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class AddressActivity extends BaseActivity implements
        View.OnClickListener {

    private int actionType;
    // 传递过来的地址信息
    private AddressBean addressBean;

    private ScrollView scrollView;
    private CustomClearEditText etConsignee;
    private CustomClearEditText etPhoneNumber;
    private TextView tvProvince;
    private TextView tvCity;
    private TextView tvDistrict;
    private EditText etAddress;
    private CheckBox checkBox;
    private LinearLayout layoutDeleteAddress;

    private List<ProvinceBean> provinceBeans = new ArrayList<ProvinceBean>();
    private List<CityBean> cityBeans = new ArrayList<CityBean>();
    private List<DistrictBean> districtBeans = new ArrayList<DistrictBean>();

    private ListView provinceListView, cityListView, districtListView;
    private AlertDialog provinceDialog, cityDialog, districtDialog;

    private CommonAdapter<ProvinceBean> provinceAdapter;
    private CommonAdapter<CityBean> cityAdapter;
    private CommonAdapter<DistrictBean> districtAdapter;

    private int provincedId = -1, cityId = -1, districtId = -1;

    // type (添加type=1,编辑type=2,删除type=3)
    private int type = 1;
    private int addressId = -1;

    // 默认显示位置
    private int provincePosition = 0, cityPosition = 0, districtPosition = 0;

    Context context;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        context=this;
        tvRight.setText(R.string.save);
        tvRight.setOnClickListener(this);

        scrollView = (ScrollView) findViewById(R.id.sl);
        etConsignee = (CustomClearEditText) findViewById(R.id.et_consignee);
        etPhoneNumber = (CustomClearEditText) findViewById(R.id.et_phone_number);
        tvProvince = (TextView) findViewById(R.id.tv_province);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvDistrict = (TextView) findViewById(R.id.tv_district);
        etAddress = (EditText) findViewById(R.id.et_address);
        checkBox = (CheckBox) findViewById(R.id.check_box);
        layoutDeleteAddress = (LinearLayout) findViewById(R.id.layout_delete_address);

//        // 编辑地址
//        if (actionType == 2) {
//            addressId = addressBean.getAddressId();
//            etConsignee.setText(addressBean.getConsignee());
//            etConsignee.setSelection(etConsignee.getText().length());
//            etPhoneNumber.setText(addressBean.getPhoneNumber());
//
//            provincedId = addressBean.getProvinceBean().getProvinceId();
//            tvProvince.setText(addressBean.getProvinceBean().getProvinceName());
//
//            cityId = addressBean.getCityBean().getCityId();
//            tvCity.setText(addressBean.getCityBean().getCityName());
//
//            districtId = addressBean.getDistrictBean().getDistrictId();
//            tvDistrict.setText(addressBean.getDistrictBean().getDistrictName());
//
//            etAddress.setText(addressBean.getAddress());
//            checkBox.setChecked(addressBean.getIsDefault() == 1);
//            layoutDeleteAddress.setVisibility(View.VISIBLE);
//        }

        tvProvince.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        tvDistrict.setOnClickListener(this);
        layoutDeleteAddress.setOnClickListener(this);

        //获得省市区的
        showProgressInfo(null);
        ApiClass.getProvinceCityDistrict(null, getProvinceCityDistrictCallback);

    }

    // 获取地址信息回调
    private ResultCallback<JsonMsgBean> getProvinceCityDistrictCallback = new ResultCallback<JsonMsgBean>() {

        @Override
        public void onSuccess(ResultBean<JsonMsgBean> resultBean, int id) {
            dismissProgress();
            scrollView.setVisibility(View.VISIBLE);
            provinceBeans = resultBean.getJsonMsg().getProvinceBeans();
            if (actionType == 2) {
                for (int i = 0; i < provinceBeans.size(); i++) {
                    if (provincedId != -1
                            && provinceBeans.get(i).getProvinceId() == provincedId) {
                        provincePosition = i;
                        cityBeans = provinceBeans.get(i).getCityBeans();
                        break;
                    }
                }
                for (int j = 0; j < cityBeans.size(); j++) {
                    if (cityId != -1 && cityId == cityBeans.get(j).getCityId()) {
                        cityPosition = j;
                        districtBeans = cityBeans.get(j).getDistrictBeans();
                        break;
                    }
                }
                for (int k = 0; k < districtBeans.size(); k++) {
                    if (districtId != -1
                            && districtId == districtBeans.get(k)
                            .getDistrictId()) {
                        districtPosition = k;
                        break;
                    }
                }
            }
        }

        public void onError(okhttp3.Call call, Exception e, int id) {
            super.onError(call, e, id);
            dismissProgress();
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                break;
            case R.id.tv_right:
                editAddress();
                break;
            case R.id.tv_province:
                showProvince();
                break;
            case R.id.tv_city:
                showCity();
                break;
            case R.id.tv_district:
                showDistrict();
                break;
            case R.id.layout_delete_address:
//                type = 3;
//                editAddress();
                break;
            default:
                break;
        }
    }

    // 显示省
    private void showProvince() {
        if (provinceDialog == null) {
            provinceAdapter = new CommonAdapter<ProvinceBean>(this,
                    provinceBeans, R.layout.item_select_address) {

                @Override
                public void convert(ViewHolder holder, int position,
                                    ProvinceBean item) {
                    holder.setText(R.id.tv, item.getProvinceName());
                }
            };
            provinceListView = (ListView) View.inflate(mActivity,
                    R.layout.layout_list_view, null);
            provinceListView.setAdapter(provinceAdapter);
            provinceListView.setSelection(provincePosition);
            provinceDialog = new AlertDialog.Builder(mActivity)
                    .setTitle("请选择省份").setView(provinceListView).show();
            provinceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ProvinceBean t = provinceBeans.get(position);
                    if (provincedId != t.getProvinceId()) {
                        provincedId = t.getProvinceId();
                        tvCity.setText("");
                        tvDistrict.setText("");
                        cityId = -1;
                        districtId = -1;
                    }
                    tvProvince.setText(t.getProvinceName());
                    cityBeans.clear();
                    cityBeans.addAll(t.getCityBeans());
                    if (cityAdapter != null) {
                        cityAdapter.setData(cityBeans);
                        cityAdapter.notifyDataSetChanged();
                    }
                    if (provinceDialog != null && provinceDialog.isShowing()) {
                        provinceDialog.dismiss();
                    }
                }
            });
        } else {
            provinceDialog.show();
        }
    }

    // 显示市
    private void showCity() {
        if (cityBeans == null || cityBeans.size() == 0) {
            Toast.makeText(mActivity, "请先选择省份", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cityDialog == null) {
            cityAdapter = new CommonAdapter<CityBean>(this, cityBeans,
                    R.layout.item_select_address) {

                @Override
                public void convert(ViewHolder holder, int position,
                                    CityBean item) {
                    holder.setText(R.id.tv, item.getCityName());
                }
            };
            cityListView = (ListView) View.inflate(mActivity,
                    R.layout.layout_list_view, null);
            cityListView.setAdapter(cityAdapter);
            cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    CityBean t = cityBeans.get(position);
                    if (cityId != t.getCityId()) {
                        cityId = t.getCityId();
                        tvDistrict.setText("");
                        districtId = -1;
                    }
                    tvCity.setText(t.getCityName());
                    districtBeans.clear();
                    districtBeans.addAll(t.getDistrictBeans());
                    if (districtAdapter != null) {
                        districtAdapter.setData(districtBeans);
                        districtAdapter.notifyDataSetChanged();
                    }
                    if (cityDialog != null && cityDialog.isShowing()) {
                        cityDialog.dismiss();
                    }
                }
            });
            cityListView.setSelection(cityPosition);
            cityDialog = new AlertDialog.Builder(mActivity).setTitle("请选择城市")
                    .setView(cityListView).show();
        } else {
            cityDialog.show();
        }
    }

    // 显示区
    private void showDistrict() {
        if (districtBeans == null || districtBeans.size() == 0) {
            Toast.makeText(mActivity, "请先选择城市", Toast.LENGTH_SHORT).show();
            return;
        }
        if (districtDialog == null) {
            districtAdapter = new CommonAdapter<DistrictBean>(this,
                    districtBeans, R.layout.item_select_address) {

                @Override
                public void convert(ViewHolder holder, int position,
                                    DistrictBean item) {
                    holder.setText(R.id.tv, item.getDistrictName());
                }
            };

            districtListView = (ListView) View.inflate(mActivity,
                    R.layout.layout_list_view, null);
            districtListView.setAdapter(districtAdapter);
            districtListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    DistrictBean t = districtBeans.get(position);
                    districtId = t.getDistrictId();
                    tvDistrict.setText(t.getDistrictName());
                    if (districtDialog != null && districtDialog.isShowing()) {
                        districtDialog.dismiss();
                    }
                }
            });
            districtListView.setSelection(districtPosition);
            districtDialog = new AlertDialog.Builder(mActivity)
                    .setTitle("请选择地区").setView(districtListView).show();
        } else {
            districtDialog.show();
        }
    }

    private void editAddress() {
        String consignee = etConsignee.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String address = etAddress.getText().toString();
        int isDefault = checkBox.isChecked() ? 1 : 0;

//        if (type != 3
//                && !VerificationUtils.editAddressVerification(this, consignee,
//                phoneNumber, provincedId, cityId, districtId, address)) {
//            return;
//        }

        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
        paramsMap.put("type", type);
        paramsMap.put("addressId", addressId);
        paramsMap.put("consignee", consignee);
        paramsMap.put("phoneNumber", phoneNumber);
        paramsMap.put("provinceId", provincedId);
        paramsMap.put("cityId", cityId);
        paramsMap.put("districtId", districtId);
        paramsMap.put("address", address);
        paramsMap.put("isDefault", isDefault);
        ApiClass.editAddress(paramsMap, editAddressCallback);



    }



    // 编辑地址回调
    private ResultCallback<String> editAddressCallback = new ResultCallback<String>() {

        @Override
        public void onSuccess(ResultBean<String> resultBean, int id) {
            dismissProgress();
            //// TODO: 2017/3/31
            if (resultBean.getStatus() == 1) {

//                MyLog.e("jp","====>地址回调:"+resultBean.getJsonMsg().toString());
//
//                Intent intent = new Intent(mActivity,
//                        AddressActivity.class);
//                intent.putExtra("updateAddressData", true);
//                setResult(RESULT_OK, intent);
//                finish();

                OkHttpUtils
                        .post()
                        .url(Constant.IP + Constant.GET_ALL_ADDRESS)
                        .addParams("id", JiaZhengApp.getInstance().getUserId())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                MyLog.e("jp","===onError:"+e.getMessage());
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                MyLog.e("jp","===onResponse:"+s);
                                Gson gson=new Gson();
                                SettingAddressModel settingAddressModel = gson.fromJson(s, SettingAddressModel.class);
                                List<SettingAddressModel.JsonMsgBean> jsonMsg = settingAddressModel.getJsonMsg();
                                for (int j = 0; j <jsonMsg.size(); j++) {
                                     if (jsonMsg.get(j).getIsDefault()==1)
                                     {
                                         SettingAddressModel.JsonMsgBean jsonMsgBean = jsonMsg.get(i);
                                         String address=jsonMsgBean.getProvinceBean().getProvinceName()+jsonMsgBean.getCityBean().getCityName()+jsonMsgBean.getDistrictBean().getDistrictName()+jsonMsgBean.getAddress();
                                         String name=jsonMsgBean.getConsignee()+"";
                                         String addressId = jsonMsg.get(j).getAddressId();
                                         Intent intent=new Intent(context,BalanceActivity.class);
                                         intent.putExtra("address",address);
                                         intent.putExtra("name",name);
                                         intent.putExtra("addressId",addressId);
                                         setResult(RESULT_OK, intent);
                                         finish();
                                     }
                                }

                            }
                        });



            } else {
                Toast.makeText(mActivity, resultBean.getInfo(),
                        Toast.LENGTH_SHORT).show();
            }
        }

        public void onError(okhttp3.Call call, Exception e, int id) {
            super.onError(call, e, id);
            dismissProgress();
        }
    };

}
