package com.wangyi.lelegou.maofengqi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.BalanceActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.MyGetShopInformation;

import java.util.List;

/**
 * Created by jipeng on 2017/3/28.
 */

public class GetAwardShopInforAdapter extends BaseAdapter {
    private Context context;
    private List<MyGetShopInformation.ObjListBean> list;

    public GetAwardShopInforAdapter(Context context, List<MyGetShopInformation.ObjListBean> list) {
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
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHodler hodler;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.award_shopinfor_item_layout, parent, false);
            hodler = new ViewHodler();
            hodler.award_shop_img = (ImageView) view.findViewById(R.id.award_shop_img);
            hodler.tv_luck_code = (TextView) view.findViewById(R.id.tv_luck_code);
            hodler.award_shop_information = (TextView) view.findViewById(R.id.award_shop_information);
            hodler.openWardtime = (TextView) view.findViewById(R.id.openWardtime);
            hodler.btn_getAward = (Button) view.findViewById(R.id.btn_getAward);
            hodler.btn_getAward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, BalanceActivity.class);
                    boolean iscircle= list.get(position).isIsCircle();
                    if (iscircle)//圈子的
                    {
                        intent.putExtra("tag","0");
                        intent.putExtra("circleId",list.get(position).getCircleId()+"");
                        intent.putExtra("ispaycircle",list.get(position).isIsPayCircle());

                    } else //非圈子的
                    {

                        intent.putExtra("tag","1");
                        intent.putExtra("shopid",list.get(position).getShopid()+"");

                    }

                    intent.putExtra("isvirtual",list.get(position).isIsVirtual());
                    context.startActivity(intent);
                }
            });
            view.setTag(hodler);
        } else {
            hodler = (ViewHodler) view.getTag();
        }
        hodler.tv_luck_code.setText(list.get(position).getUserCode() + "");
        hodler.award_shop_information.setText(list.get(position).getShopname());
        hodler.openWardtime.setText(list.get(position).getEndTime());
        Glide.with(context)
                .load(list.get(position).getThumb())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.no_pic)
                .fitCenter()
                .centerCrop()
                .into(hodler.award_shop_img);//加载网络图片
        return view;
    }

    private class ViewHodler {
        private ImageView award_shop_img;
        private TextView tv_luck_code;
        private TextView award_shop_information;
        private TextView openWardtime;
        private Button btn_getAward;
    }
}
