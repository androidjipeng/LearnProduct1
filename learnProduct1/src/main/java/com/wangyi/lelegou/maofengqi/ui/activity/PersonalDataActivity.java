package com.wangyi.lelegou.maofengqi.ui.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.bean.UploadImageBean;
import com.wangyi.lelegou.maofengqi.bean.UserBean;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.BitmapUtils;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;

/**
 * **********************************************************
 * <p/>
 * 说明:个人资料
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class PersonalDataActivity extends BaseActivity implements
		OnClickListener {

	public static PersonalDataActivity instance = null;

	private ImageView ivHead;
	private TextView tvNickName, tvPhoneNumber;

	private LinearLayout layoutHead;
	private LinearLayout layoutNickName;
	private LinearLayout layoutPhoneNumber;
	private LinearLayout layoutAddressManager;

	private UserBean userBean;

	private Uri tempUri;

	// 用户选择的图片
	Bitmap photo;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_personal_data;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		instance = this;
		tvContent.setText(R.string.personal_data);

		ivHead = (ImageView) findViewById(R.id.iv_head);
		tvNickName = (TextView) findViewById(R.id.tv_nick_name);
		tvPhoneNumber = (TextView) findViewById(R.id.tv_phone_number);

		layoutHead = (LinearLayout) findViewById(R.id.layout_head);
		layoutNickName = (LinearLayout) findViewById(R.id.layout_nick_name);
		layoutPhoneNumber = (LinearLayout) findViewById(R.id.layout_phone_number);
		layoutAddressManager = (LinearLayout) findViewById(R.id.layout_address_manager);

		layoutHead.setOnClickListener(this);
		layoutNickName.setOnClickListener(this);
		layoutPhoneNumber.setOnClickListener(this);
		layoutAddressManager.setOnClickListener(this);

		getData();
	}

	// 获取个人资料
	private void getData() {
		showProgressInfo(null);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
		ApiClass.getPersonalData(paramsMap, callback);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_head:
			showDialog();
			break;
		case R.id.layout_nick_name:
			UpdateNickNameActivity.start(this, userBean.getNickName(),
					Constant.REQUESTCODE_UPDATE_NICK_NAME);
			break;
		case R.id.layout_phone_number:
			UpdatePhoneNumberActivity.start(this, userBean.getPhoneNumber(),
					Constant.REQUESTCODE_UPDATE_PHONE_NUMBER);
			break;
		case R.id.layout_address_manager:
			AddressManagerActivity.start(this, -1, 0);
			break;
		default:
			break;
		}
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
		tempUri = Uri.fromFile(new File(fileDir, "image.jpg"));
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
					// 开始对图片进行裁剪处理
					startPhotoZoom(data.getData());
				}
				break;
			case Constant.REQUESTCODE_TAKE_PHONO:
				// 开始对图片进行裁剪处理
				startPhotoZoom(tempUri);
				break;
			case Constant.REQUESTCODE_CROP_SMALL_PICTURE:
				if (data != null) {
					// 让刚才选择裁剪得到的图片显示在界面上
					setImageToView(data);
				}
				break;
			case Constant.REQUESTCODE_UPDATE_NICK_NAME:// 修改昵称
				if (data != null
						&& data.getBooleanExtra("updatePersonalData", false)) {
					getData();
				}
				break;
			case Constant.REQUESTCODE_UPDATE_PHONE_NUMBER:// 修改电话号码
				if (data != null
						&& data.getBooleanExtra("updatePersonalData", false)) {
					getData();
				}
				break;
			}
		}
	}

	// 裁剪图片方法实现
	protected void startPhotoZoom(Uri uri) {
		tempUri = uri;
		int outputSize = getResources().getDimensionPixelSize(
				R.dimen.output_size);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", outputSize);
		intent.putExtra("outputY", outputSize);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Constant.REQUESTCODE_CROP_SMALL_PICTURE);
	}

	// 保存裁剪之后的图片数据
	protected void setImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");

			try {
				showProgressInfo(null);
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
				ApiClass.uploadImage(paramsMap, saveFile(photo), "data",
						uploadImageCallback);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 保存图片
	public File saveFile(Bitmap bm) throws IOException {
		File fileDir = new File(Constant.DIRECTORY + Constant.HEAD_IMAGE_DIR);
		if (!fileDir.exists())
			fileDir.mkdirs();

		File iconHeadFile = new File(fileDir, "icon_head.jpg");
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(iconHeadFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
		return iconHeadFile;
	}

	// Bitmap → byte[]
	public byte[] bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
		return baos.toByteArray();
	}

	private ResultCallback<UserBean> callback = new ResultCallback<UserBean>() {

		@Override
		public void onSuccess(ResultBean<UserBean> resultBean, int id) {
			dismissProgress();
			if (resultBean.getStatus() == 1) {
				userBean = resultBean.getJsonMsg();
				tvNickName.setText(userBean.getNickName());
				tvPhoneNumber.setText(userBean.getPhoneNumber());
				if (userBean.getHeadUrl() != null
						&& !userBean.getHeadUrl().equals("")) {
					String imageUrl = /* Constant.IMG_IP + */userBean
							.getHeadUrl();
					ImageLoader.getInstance().displayImage(imageUrl, ivHead,
							JiaZhengApp.getInstance().getUserPicOptions());
				}
			}
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
		}
	};

	// 上传图片
	private ResultCallback<UploadImageBean> uploadImageCallback = new ResultCallback<UploadImageBean>() {

		@Override
		public void onSuccess(ResultBean<UploadImageBean> resultBean, int id) {
			if (resultBean.getStatus() == 1) {
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
				paramsMap
						.put("imageUrl", resultBean.getJsonMsg().getImageUrl());
				ApiClass.updateHead(paramsMap, updateHeadCallback);
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

	// 更新头像回调
	private ResultCallback<String> updateHeadCallback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			Toast.makeText(mActivity, resultBean.getInfo(), Toast.LENGTH_SHORT)
					.show();
			if (resultBean.getStatus() == 1) {
				ivHead.setImageBitmap(BitmapUtils.toRoundBitmap(photo));
				sendBroadcast(new Intent(Constant.ACTION_RELOAD));
			}
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	// 启动PersonalDataActivity
	public static void start(Context context) {
		Intent intent = new Intent(context, PersonalDataActivity.class);
		context.startActivity(intent);
	}

	// 启动PersonalDataActivity并且返回结果
	public static void startForResult(Activity activity) {
		Intent intent = new Intent(activity, PersonalDataActivity.class);
		intent.putExtra("updatePersonalData", true);
		activity.setResult(RESULT_OK, intent);
		activity.finish();
	}
}