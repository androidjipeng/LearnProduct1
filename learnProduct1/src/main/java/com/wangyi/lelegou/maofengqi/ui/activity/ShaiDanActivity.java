package com.wangyi.lelegou.maofengqi.ui.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.bean.UploadImageBean;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.PhotoClipperUtil;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.view.NoScrollGridView;
import com.wangyi.lelegou.maofengqi.view.adapter.CommonAdapter;
import com.wangyi.lelegou.maofengqi.view.adapter.ViewHolder;

/**
 * **********************************************************
 * <p/>
 * 说明:晒单
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class ShaiDanActivity extends BaseActivity implements
		View.OnClickListener {

	private int id;

	private EditText etContent;
	private NoScrollGridView gridView;
	private List<String> list;
	private CommonAdapter<String> adapter;

	private Uri tempUri;

	private String content;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_shai_dan;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			id = bundle.getInt("id");
		}

		tvContent.setText(R.string.shai_dan);

		tvRight.setText(R.string.save);
		tvRight.setOnClickListener(this);

		etContent = (EditText) findViewById(R.id.et_content);
		gridView = (NoScrollGridView) findViewById(R.id.gv_shai_dan);

		list = new ArrayList<String>();
		list.add("");

		adapter = new CommonAdapter<String>(this, list, R.layout.item_shai_dan) {

			@Override
			public void convert(ViewHolder holder, int position, String item) {
				ImageView iv = holder.getViewById(R.id.iv);
				if (item.equals("")) {
					ImageLoader.getInstance().displayImage(
							"drawable://" + R.drawable.ic_add, iv);
				} else {
					ImageLoader.getInstance()
							.displayImage("file://" + item, iv);
				}
			}
		};
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showDialog();
			}
		});
	}

	// 弹出选择修改头像方式对话框
	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("设置头像");
		String[] items = { "选择本地照片", "拍照" };
		builder.setNegativeButton("取消", null);
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					// 选择本地照片
					choosePicture();
					break;
				case 1:
					// 拍照
					takephoto();
					break;
				}
			}
		});
		builder.create().show();
	}

	// 选择本地照片
	private void choosePicture() {
		Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
		openAlbumIntent.setType("image/*");
		startActivityForResult(openAlbumIntent,
				Constant.REQUESTCODE_CHOOSE_PICTURE);
	}

	// 拍照
	private void takephoto() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File fileDir = new File(Constant.DIRECTORY + Constant.HEAD_IMAGE_DIR);
		if (!fileDir.exists())
			fileDir.mkdirs();
		tempUri = Uri.fromFile(new File(fileDir, "shaidan.jpg"));
		// 指定照片保存路径（SD卡）,image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
		startActivityForResult(openCameraIntent,
				Constant.REQUESTCODE_TAKE_PHONO);
	}

	// 返回结果处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) { // 如果返回码是可以用的
			switch (requestCode) {
			case Constant.REQUESTCODE_CHOOSE_PICTURE:
				if (data != null) {
					// content://com.android.providers.media.documents/document/image%3A191891
					tempUri = data.getData();
					String url = PhotoClipperUtil.getPath(ShaiDanActivity.this, tempUri);
					list.clear();
					list.add(url);
					adapter.notifyDataSetChanged();
				}
				break;
			case Constant.REQUESTCODE_TAKE_PHONO:
				list.clear();
				list.add(tempUri.getPath());
				adapter.notifyDataSetChanged();
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right:
			content = etContent.getText().toString();
			if (content.trim().length() == 0) {
				Toast.makeText(mActivity, "请输入内容", Toast.LENGTH_SHORT).show();
				return;
			}
			if (list.get(0).equals("")) {
				Toast.makeText(mActivity, "请选择图片", Toast.LENGTH_SHORT).show();
				return;
			}
			showProgressInfo(null);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
			ApiClass.uploadImage(paramsMap, new File(list.get(0)), "data",
					uploadImageCallback);
			break;
		default:
			break;
		}
	}

	// 上传图片
	private ResultCallback<UploadImageBean> uploadImageCallback = new ResultCallback<UploadImageBean>() {

		@Override
		public void onSuccess(ResultBean<UploadImageBean> resultBean, int id) {
			if (resultBean.getStatus() == 1) {
				//"http://headsimage.91lelegou.com/"+
				shaidan(resultBean.getJsonMsg().getImageUrl());
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

	private void shaidan(String photo) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userid", JiaZhengApp.getInstance().getUserId());
		paramsMap.put("shopid", id);
		paramsMap.put("content", content);
		paramsMap.put("photo", photo);
		ApiClass.shaidan(paramsMap, shaidanCallback);
	}

	// 晒单回调
	private ResultCallback<String> shaidanCallback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			Toast.makeText(mActivity, resultBean.getInfo(), Toast.LENGTH_SHORT)
					.show();
			if (resultBean.getStatus() == 1) {
				sendBroadcast(new Intent(Constant.ACTION_RELOAD));
				finish();
			}
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
		}
	};

	public static void start(Activity activity, int id) {
		Intent intent = new Intent(activity, ShaiDanActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("id", id);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}
}