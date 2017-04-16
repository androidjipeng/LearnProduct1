package com.learn.soft.product.jni;

import android.content.Context;

public interface ILoading {
	Loading getLoading();

	Loading initLoading();

	Context getLoadingContext();
}