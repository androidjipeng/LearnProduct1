package com.wangyi.lelegou.maofengqi.ui.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.Config;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.AddCartInformation;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.CartInformationModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by jipeng on 2017/3/23.
 */

public class CartBaseAdapter extends BaseAdapter {
    private Context context;
    List<CartInformationModel.ObjListBean>  list;

    public List<CartInformationModel.ObjListBean> getList() {
        return list;
    }

    public void setList(List<CartInformationModel.ObjListBean> list) {
        this.list = list;
    }

    DataChange dataChange;
    int count=0;
    public void setdataChangeLinstener(DataChange dataChange)
    {
         this.dataChange=dataChange;
    }

    public interface DataChange
    {
        void data(boolean is ,int nums);

        void check(boolean is,int nums);

    }

    public CartBaseAdapter(Context context, List<CartInformationModel.ObjListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHoder hoder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_shop_item_layout, parent, false);
            hoder = new ViewHoder();
            hoder.tv_person_times = (TextView) convertView.findViewById(R.id.tv_person_times);
            hoder.title = (TextView) convertView.findViewById(R.id.title);
            hoder.tv_overplus = (TextView) convertView.findViewById(R.id.tv_overplus);//
            hoder.minus = (TextView) convertView.findViewById(R.id.minus);//减
            hoder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hoder.num.getText().toString().equals("1"))
                    {
                        ToastHelper.toast("不可再减");
                        list.get(position).setIscheck(false);
                        notifyDataSetChanged();

                    }else
                    {
                        int count=Integer.parseInt(hoder.num.getText().toString())-1;
                        list.get(position).setNum(count);
                        hoder.num.setText(count+"");
                        list.get(position).setIscheck(true);
//                        dataChange.check(true,0);
                        dataChange.data(false,1);
                        notifyDataSetChanged();

                    }

                }
            });
            hoder.add = (TextView) convertView.findViewById(R.id.add);//加
            hoder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int count=Integer.parseInt(hoder.num.getText().toString())+1;

                    list.get(position).setNum(count);
                    hoder.num.setText(count+"");

                    list.get(position).setIscheck(true);
//                    dataChange.check(true,0);
                    dataChange.data(true,1);
                    notifyDataSetChanged();

                }
            });
            hoder.shop_img= (ImageView) convertView.findViewById(R.id.shop_img);//商品图
            hoder.img_del= (ImageView) convertView.findViewById(R.id.img_del);//删除
            hoder.img_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkHttpUtils.post()
                            .url(Config.testurl+Config.delShopingCart)
                            .addParams("uid", JiaZhengApp.getInstance().getUserId())
                            .addParams("shopid",list.get(position).getShopid()+"")
                            .build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            MyLog.e("jp","CartBaseAdapter=====onError:"+e.getMessage());
                        }

                        @Override
                        public void onResponse(String s, int i) {
                          MyLog.e("jp","CartBaseAdapter=====onResponse:"+s);
                            Gson gson=new Gson();
                            AddCartInformation addCartInformation = gson.fromJson(s, AddCartInformation.class);
                            ToastHelper.toast(addCartInformation.getInfo());
                            list.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                }
            });
            hoder.check_box= (CheckBox) convertView.findViewById(R.id.check_box);//
            hoder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    MyLog.e("jp",""+isChecked);
                    if (isChecked)
                    {
                        list.get(position).setIscheck(isChecked);
                        dataChange.check(isChecked,1);

                    }else {
                        list.get(position).setIscheck(isChecked);
                        dataChange.check(isChecked,1);

                    }
                }
            });
            hoder.num= (EditText) convertView.findViewById(R.id.num);//数量
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHoder) convertView.getTag();
        }
        hoder.num.setText(list.get(position).getNum()+"");
        hoder.tv_overplus.setText("剩余"+list.get(position).getShenyurenshu()+"人次");
        hoder.title.setText(list.get(position).getTitle());
        hoder.tv_person_times.setText("1人次/￥"+list.get(position).getYunjiage());
        Glide.with(context)
                .load(list.get(position).getThumb())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.no_pic)
                .fitCenter()
                .centerCrop()
                .into(hoder.shop_img);//加载网络图片
        hoder.check_box.setChecked(list.get(position).getIscheck());
        return convertView;
    }

    class ViewHoder {
        TextView tv_person_times;
        CheckBox check_box;
        ImageView shop_img;
        TextView title;
        TextView tv_overplus;
        TextView minus;
        EditText num;
        TextView add;
        ImageView img_del;

    }
}
