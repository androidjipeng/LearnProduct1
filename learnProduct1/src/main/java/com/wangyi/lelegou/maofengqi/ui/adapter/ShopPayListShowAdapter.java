package com.wangyi.lelegou.maofengqi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.PayMoneyModel;

import java.util.List;

import static com.learn.soft.product1.R.id.shop_item_information;

/**
 * Created by jipeng on 2017/3/28.
 */

public class ShopPayListShowAdapter extends BaseAdapter{

    private Context context;
    private List<PayMoneyModel.ObjListBean> list;

    public ShopPayListShowAdapter(Context context, List<PayMoneyModel.ObjListBean> list) {
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view==null)
        {
            view= LayoutInflater.from(context).inflate(R.layout.shop_pay_list_item_layout,parent,false);
            holder=new ViewHolder();
            holder.item_person_times= (TextView) view.findViewById(R.id.item_person_times);
            holder.shop_item_information= (TextView) view.findViewById(shop_item_information);
            view.setTag(holder);
        }else
        {
           holder= (ViewHolder) view.getTag();
        }

        holder.shop_item_information.setText(list.get(position).getTitle());
        if (list.get(position).getNum()==0)
        {
            holder.item_person_times.setText("");
        }else
        {
            holder.item_person_times.setText(list.get(position).getNum()+"人次");
        }

        return view;
    }

    class ViewHolder
    {
        private TextView shop_item_information;
        private TextView item_person_times;
    }
}
