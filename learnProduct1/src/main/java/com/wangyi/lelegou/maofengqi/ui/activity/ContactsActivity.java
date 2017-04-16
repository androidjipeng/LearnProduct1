package com.wangyi.lelegou.maofengqi.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.ContactBean;
import com.wangyi.lelegou.maofengqi.view.adapter.CommonAdapter;
import com.wangyi.lelegou.maofengqi.view.adapter.ViewHolder;

/**
 * 通讯录
 * 
 * @author Doc.March
 * 
 */
public class ContactsActivity extends BaseActivity {

	private String body;

	private ListView listView;
	private List<ContactBean> list = new ArrayList<ContactBean>();
	private CommonAdapter<ContactBean> adapter;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_contacts;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		body = getIntent().getStringExtra("body");

		tvContent.setText("通讯录");

		listView = (ListView) findViewById(R.id.list_view);

		adapter = new CommonAdapter<ContactBean>(this, list,
				R.layout.item_contacts) {

			@Override
			public void convert(ViewHolder holder, int position,
					ContactBean item) {
				holder.setText(R.id.tv_name, item.getName());
				holder.setText(R.id.tv_number, item.getNumber());

				TextView tvInvite = holder.getViewById(R.id.tv_invite);
				tvInvite.setTag(item);
				tvInvite.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ContactBean bean = (ContactBean) v.getTag();
						send(bean.getNumber(), body);
					}
				});
			}
		};
		listView.setAdapter(adapter);

		getContacts();
	}

	// 查询得到联系人表数据
	private void getContacts() {
		ContentResolver resolver = getContentResolver();
		String[] projection = { Phone.DISPLAY_NAME, Phone.NUMBER };
		Cursor cursor = resolver.query(Phone.CONTENT_URI, projection, null,
				null, null);
		list.clear();
		while (cursor.moveToNext()) {
			String name = cursor.getString(0);
			String number = cursor.getString(1);
			ContactBean bean = new ContactBean();
			bean.setName(name);
			bean.setNumber(number);
			list.add(bean);
		}
		adapter.notifyDataSetChanged();
	}

	// 发送信息
	private void send(String number, String body) {
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setData(Uri.parse("smsto:" + number));
		intent.putExtra("sms_body", body);
		startActivity(intent);
	}

	// 启动ContactsActivity
	public static void start(Context context, String body) {
		Intent intent = new Intent(context, ContactsActivity.class);
		intent.putExtra("body", body);
		context.startActivity(intent);
	}
}