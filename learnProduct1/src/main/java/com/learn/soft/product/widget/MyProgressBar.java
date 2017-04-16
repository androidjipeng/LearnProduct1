package com.learn.soft.product.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class MyProgressBar extends ProgressBar {
    private static final String SCHEMAS_ANDROID = "http://schemas.android.com/apk/res/android";
    private int mCurProgress = 0;
    private int mOldProgress = 0;

    public MyProgressBar(Context context) {
        super(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCurProgress = attrs.getAttributeIntValue(SCHEMAS_ANDROID, "progress", 0);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCurProgress = attrs.getAttributeIntValue(SCHEMAS_ANDROID, "progress", 0);
    }

    // 设置进度条当前进度
    public synchronized void setProgress(int progress) {
        mCurProgress = progress;
        if (mCurProgress <= mOldProgress) {
            mOldProgress = mCurProgress;
        }
        super.setProgress(progress);
    }

    @Override
    public synchronized int getProgress() {
        return mCurProgress;
    }

    // 自定义进度条的绘制
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawBackgroud(canvas);
    }

    private void drawBackgroud(Canvas canvas) {
        int max = getMax();
        if (mOldProgress < mCurProgress) {
            mOldProgress++;
        }
        Paint paint = new Paint();
        paint.setColor(Color.argb(255, 156, 156, 156));

        float per = (float) mOldProgress / max;
        int x_start = (int) (getWidth() * per);
        canvas.drawRoundRect(new RectF(x_start, 0, getWidth(), getHeight()), 5, 5, paint);
        canvas.drawRect(x_start, 0, x_start + 5, getHeight(), paint);
    }
}
