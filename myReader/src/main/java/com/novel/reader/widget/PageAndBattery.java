package com.novel.reader.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.novel.reader.utils.ScreenUtils;
import com.novel.reader.utils.StringUtils;

/**
 * @author : easy on 2022/10/18 16:50
 * @description :页数和电池
 */
public class PageAndBattery extends View {

    public PageAndBattery(Context context) {
        super(context);
        init();
    }

    public PageAndBattery(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageAndBattery(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PageAndBattery(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Paint mBatteryPaint;
    private boolean isNightMode;
    private int mBatteryLevel = 90;
    private String progress = "";

    public void setProgress(String progress) {
        this.progress = progress;
        invalidate();
    }

    public void setBattery(int level) {
        mBatteryLevel = level;
    }

    public void updateTime() {
        invalidate();
    }

    // 绘制提示的画笔
    private Paint mTipPaint;

    public void init() {
        // 绘制电池的画笔
        mBatteryPaint = new Paint();
        mBatteryPaint.setAntiAlias(true);
        mBatteryPaint.setDither(true);
        setNightMode(false);

        // 绘制提示的画笔
        mTipPaint = new Paint();
        mTipPaint.setColor(Color.parseColor("#000000"));
        mTipPaint.setTextAlign(Paint.Align.LEFT); // 绘制的起始点
        mTipPaint.setTextSize(ScreenUtils.spToPx(12)); // Tip默认的字体大小
        mTipPaint.setAntiAlias(true);
        mTipPaint.setSubpixelText(true);
    }

    /**
     * 设置夜间模式
     *
     * @param nightMode
     */
    public void setNightMode(boolean nightMode) {
//        mSettingManager.setNightMode(nightMode);
        isNightMode = nightMode;

        if (isNightMode) {
            mBatteryPaint.setColor(Color.WHITE);
//            setPageStyle(PageStyle.NIGHT);
        } else {
            mBatteryPaint.setColor(Color.BLACK);
//            setPageStyle(mPageStyle);
        }
        mBatteryPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int mDisplayWidth = getResources().getDisplayMetrics().widthPixels;
        int mDisplayHeight = 60;

        int visibleRight = mDisplayWidth - 10;
        int visibleBottom = mDisplayHeight - 9;

        int outFrameWidth = (int) mTipPaint.measureText("xxx");
        int outFrameHeight = (int) mTipPaint.getTextSize();

        int polarHeight = ScreenUtils.dpToPx(6);
        int polarWidth = ScreenUtils.dpToPx(2);
        int border = 1;
        int innerMargin = 1;

        //电极的制作
        int polarLeft = visibleRight - polarWidth;
        int polarTop = visibleBottom - (outFrameHeight + polarHeight) / 2;
        Rect polar = new Rect(polarLeft, polarTop, visibleRight,
                polarTop + polarHeight - ScreenUtils.dpToPx(2));

        mBatteryPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(polar, mBatteryPaint);

        //外框的制作
        int outFrameLeft = polarLeft - outFrameWidth;
        int outFrameTop = visibleBottom - outFrameHeight;
        int outFrameBottom = visibleBottom - ScreenUtils.dpToPx(2);
        Rect outFrame = new Rect(outFrameLeft, outFrameTop, polarLeft, outFrameBottom);

        mBatteryPaint.setStyle(Paint.Style.STROKE);
        mBatteryPaint.setStrokeWidth(border);
        canvas.drawRect(outFrame, mBatteryPaint);

        //内框的制作
        float innerWidth = (outFrame.width() - innerMargin * 2 - border) * (mBatteryLevel / 100.0f);
        RectF innerFrame = new RectF(outFrameLeft + border + innerMargin, outFrameTop + border + innerMargin,
                outFrameLeft + border + innerMargin + innerWidth, outFrameBottom - border - innerMargin);

        mBatteryPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(innerFrame, mBatteryPaint);

        /******绘制当前时间********/
        //底部的字显示的位置Y
        float y = mDisplayHeight - mTipPaint.getFontMetrics().bottom - 9;
        String time = StringUtils.dateConvert(System.currentTimeMillis(), "HH:mm");
        float x = outFrameLeft - mTipPaint.measureText(time) - ScreenUtils.dpToPx(4);
        canvas.drawText(time, x, y, mTipPaint);

        canvas.drawText(progress, 30, y, mTipPaint);
    }
}
