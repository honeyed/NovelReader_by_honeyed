package com.novel.reader.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.novel.reader.ReadSettingManager;
import com.novel.reader.State;
import com.novel.reader.bean.ReaderPageEntity;
import com.novel.reader.utils.ScreenUtils;

/**
 * @author : easy on 2022/10/21 18:24
 * @description :显示文字的页面
 */
public class ReaderPageView extends View {

    // 默认的显示参数配置
    public static final int DEFAULT_MARGIN_HEIGHT = 28;
    public static final int DEFAULT_MARGIN_WIDTH = 15;
    public static final int DEFAULT_TIP_SIZE = 12;
    public static final int EXTRA_TITLE_SIZE = 4;

    // 绘制提示的画笔
    private Paint mTipPaint;
    // 绘制标题的画笔
    private Paint mTitlePaint;
    // 绘制背景颜色的画笔(用来擦除需要重绘的部分)
    private Paint mBgPaint;
    // 绘制小说内容的画笔
    private TextPaint mTextPaint;

    private ReaderPageEntity entity;

    //应用的宽高
    private int mDisplayWidth = State.mDisplayWidth;
    private int mDisplayHeight = State.mDisplayHeight;

    //间距
    private int mMarginWidth;
    private int mMarginHeight;
    //字体的颜色
    private int mTextColor = Color.parseColor("#191919");
    //标题的大小
    private int mTitleSize;
    //字体的大小
    private int mTextSize;
    //行间距
    private int mTextInterval;
    //标题的行间距
    private int mTitleInterval;
    //段落距离(基于行间距的额外距离)
    private int mTextPara;
    private int mTitlePara;
    //当前是否是夜间模式
    private boolean isNightMode;

    private ReadSettingManager mSettingManager;

    public ReaderPageView(Context context) {
        super(context);
        initData();
    }

    public ReaderPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public ReaderPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    public void setEntity(ReaderPageEntity entity) {
        this.entity = entity;
        if(entity == null)
            return;
        invalidate();
    }

    private void initData() {
        mDisplayWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mDisplayWidth = getContext().getResources().getDisplayMetrics().widthPixels;

        // 获取配置管理器
        mSettingManager = ReadSettingManager.getInstance();
        setUpTextParams(mSettingManager.getTextSize());
        initPaint();

        // 初始化参数
        mMarginWidth = ScreenUtils.dpToPx(DEFAULT_MARGIN_WIDTH);
        mMarginHeight = ScreenUtils.dpToPx(DEFAULT_MARGIN_HEIGHT);
        // 配置文字有关的参数
    }

    /**
     * 作用：设置与文字相关的参数
     *
     * @param textSize
     */
    private void setUpTextParams(int textSize) {
        // 文字大小
        mTextSize = textSize;
        mTitleSize = mTextSize + ScreenUtils.spToPx(EXTRA_TITLE_SIZE);
        // 行间距(大小为字体的一半)
        mTextInterval = mTextSize / 2;
        mTitleInterval = mTitleSize / 2;
        // 段落间距(大小为字体的高度)
        mTextPara = mTextSize;
        mTitlePara = mTitleSize;
    }

    private void initPaint() {
        // 绘制提示的画笔
        mTipPaint = new Paint();
        mTipPaint.setColor(mTextColor);
        mTipPaint.setTextAlign(Paint.Align.LEFT); // 绘制的起始点
        mTipPaint.setTextSize(ScreenUtils.spToPx(DEFAULT_TIP_SIZE)); // Tip默认的字体大小
        mTipPaint.setAntiAlias(true);
        mTipPaint.setSubpixelText(true);

        // 绘制页面内容的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);

        // 绘制标题的画笔
        mTitlePaint = new TextPaint();
        mTitlePaint.setColor(mTextColor);
        mTitlePaint.setTextSize(mTitleSize);
        mTitlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTitlePaint.setAntiAlias(true);

        // 绘制背景的画笔
//        mBgPaint = new Paint();
//        mBgPaint.setColor(mBgColor);

        // 初始化页面样式
        setNightMode(mSettingManager.isNightMode());
    }

    /**
     * 设置夜间模式
     *
     * @param nightMode
     */
    public void setNightMode(boolean nightMode) {
        mSettingManager.setNightMode(nightMode);
        isNightMode = nightMode;

        if (isNightMode) {
//            mBatteryPaint.setColor(Color.WHITE);
//            setPageStyle(PageStyle.NIGHT);
        } else {
//            mBatteryPaint.setColor(Color.BLACK);
//            setPageStyle(mPageStyle);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /******绘制内容****/
        /**
         * 这是一个居中的圆
         */
//        float x = (getWidth() - getHeight() / 2) / 2;
//        float y = getHeight() / 4;
//
//        RectF oval = new RectF( x, y,
//                getWidth() - x, getHeight() - y);
//        canvas.drawArc(oval,-90,120,false,mTextPaint);

        if(entity == null)
            return;
//        canvas.drawColor(Color.YELLOW);
        float top;
        top = mMarginHeight - mTextPaint.getFontMetrics().top;
        //设置总距离
        int interval = mTextInterval + (int) mTextPaint.getTextSize();
        int para = mTextPara + (int) mTextPaint.getTextSize();
        int titleInterval = mTitleInterval + (int) mTitlePaint.getTextSize();
        int titlePara = mTitlePara + (int) mTextPaint.getTextSize();
        String str = null;
        //对标题进行绘制
        for (int i = 0; i < entity.titleLines; ++i) {
            str = entity.lines.get(i);
            //设置顶部间距
            if (i == 0) {
                top += mTitlePara;
            }
            //计算文字显示的起始点
            int start = (int) (mDisplayWidth - mTitlePaint.measureText(str)) / 2;
            //进行绘制
            canvas.drawText(str, start, top, mTitlePaint);
            //设置尾部间距
            if (i == entity.titleLines - 1) {
                top += titlePara;
            } else {
                //行间距
                top += titleInterval;
            }
        }
        //对内容进行绘制
        for (int i = entity.titleLines; i < entity.lines.size(); ++i) {
            str = entity.lines.get(i);
            canvas.drawText(str, mMarginWidth, top, mTextPaint);
            if (str.endsWith("\n")) {
                top += para;
            } else {
                top += interval;
            }
        }
    }

    public void setTextSize(int fontSize) {
        setUpTextParams(fontSize);
        mTextPaint.setTextSize(mTextSize);
        // 绘制标题的画笔
        mTitlePaint.setTextSize(mTitleSize);
    }
}
