package com.novel.reader.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.novel.reader.MyPageLoader;
import com.novel.reader.R;
import com.novel.reader.State;
import com.novel.reader.bean.BaseEntity;
import com.novel.reader.bean.BookChapterEntity;
import com.novel.reader.bean.BookEntity;
import com.novel.reader.bean.ReaderPageEntity;

import java.util.List;

/**
 * @author : easy on 2022/10/13 16:06
 * @description :
 */
public class ReaderView extends RelativeLayout {

    private ContentView pre, current, next;
    private AdView ad;
    private float downX, downY;
    private int width, height;
    private VelocityTracker mVelocityTracker = null;
    private boolean isAnim = false;
    private ReaderPageEntity preString, cureString, nextString;
    private MyPageLoader pageLoader;
    private int adState = 0;
    private static final int AdvertisementInterval = 11;
    private BookEntity bookEntity;

    public ReaderView(Context context) {
        super(context);
        init();
    }

    public ReaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ReaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public List<BookChapterEntity> getChapterList() {
        return pageLoader.getChapterList();
    }

    public void startReader(BookEntity bookEntity) {
        BookEntity localBookEntity = BaseEntity.findBookByIndex(bookEntity.cover);
        if(localBookEntity == null) {
            bookEntity.save();
        }
        this.bookEntity = localBookEntity == null ? bookEntity : localBookEntity;

        pageLoader.startReader(this.bookEntity);
        pageLoader.setLoadSuccess(new MyPageLoader.LoadSuccess() {
            @Override
            public void onLoadSuccessListener() {
                post(() -> {
                    pre = findViewById(R.id.pre);
                    current = findViewById(R.id.current);
                    next = findViewById(R.id.next);
                    ad = findViewById(R.id.ad);

                    height = ReaderView.this.getMeasuredHeight();
                    pre.layout(-width, 0, 0, height);

                    preString = pageLoader.getPre();
                    pre.setText(preString, pageLoader.getCurrentContent());
                    pre.setBattery(batteryLevel);

                    cureString = pageLoader.getCurrent();
                    current.setText(cureString, pageLoader.getCurrentContent());
                    current.setBattery(batteryLevel);

                    nextString = pageLoader.getNext();
                    next.setText(nextString, pageLoader.getCurrentContent());
                    next.setBattery(batteryLevel);
                    if (clickType != null)
                        clickType.loadSuccess();
                });
            }
        });
    }

    public void init() {
        width = getContext().getResources().getDisplayMetrics().widthPixels;
//        height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.9);
        pageLoader = new MyPageLoader(getContext());

    }

    private boolean hasMove = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isAnim) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hasMove = false;
                mVelocityTracker = VelocityTracker.obtain();
                mVelocityTracker.addMovement(event);
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(clickType.isShow()) {
                    clickType.close();
                }
                hasMove = true;
                mVelocityTracker.addMovement(event);
                float x = event.getX();
                mVelocityTracker.computeCurrentVelocity(1000);
                if (x > downX) {//pre
                    if (pageLoader.hasPre())
                        layoutPre(event, pre);
                } else { //next
                    if(adState != AdvertisementInterval  || State.isVip) {
                        if (pageLoader.hasNext())
                            layoutCurr(event, current);
                    } else {
                        layoutCurr(event, ad);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                float upx = event.getX();
                if(!hasMove) {
                    if(clickType.isShow()) {
                        clickType.close();
                        return true;
                    }
                    if (upx < width / 3) {
                        if (clickType != null)
                            clickType.onState(0);
                        startAnim((int) pre.getX(), 0, pre);
                    } else if (upx > width / 3 && upx < width / 3 * 2) {
                        if (clickType != null)
                            clickType.onState(1);
                    } else if (upx > width / 3 * 2) {
                        if (clickType != null)
                            clickType.onState(2);
                        if(adState != AdvertisementInterval || State.isVip) {
                            if (pageLoader.hasNext())
                                startAnim((int) current.getX(), -width, current);
                        } else {
                            startAnim((int) ad.getX(), -width, ad);
                        }
                    }
                    return true;
                }
                if (Math.abs(mVelocityTracker.getXVelocity()) >= 50) {//根据X轴速度判断是否翻页
                    if (upx > downX) {//pre 上一页
                        if (pageLoader.hasPre())
                            startAnim((int) pre.getX(), 0, pre);
                    } else { //next
                        if(adState != AdvertisementInterval || State.isVip) {
                            if (pageLoader.hasNext())
                                startAnim((int) current.getX(), -width, current);
                        } else {
                            startAnim((int) ad.getX(), -width, ad);
                        }
                    }
                } else {
                    if (upx > downX) {//pre 上一页
                        if (upx - downX > width / 6) {
                            if (pageLoader.hasPre())
                                startAnim((int) pre.getX(), 0, pre);
                        } else {
                            if (pageLoader.hasPre())
                                startAnim((int) pre.getX(), -width, pre, false);
                        }
                    } else { //next
                        if (downX - upx > width / 6) {
                            if(adState != AdvertisementInterval  || State.isVip) {
                                if (pageLoader.hasNext())
                                    startAnim((int) current.getX(), -width, current);
                            } else {
                                startAnim((int) ad.getX(), -width, ad);
                            }
                        } else {
                            if(adState != AdvertisementInterval  || State.isVip) {
                                if (pageLoader.hasNext())
                                    startAnim((int) current.getX(), 0, current, false);
                            } else {
                                startAnim((int) ad.getX(), 0, ad, false);
                            }

                        }
                    }
                }
                mVelocityTracker.recycle();
                break;
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.recycle();
                break;
        }
        return true;
    }

    public void layoutPre(MotionEvent event, View view) {
        if (!hasPre())
            return;
        int move = (int) (event.getX() - downX);
        view.layout(-width + move, 0, move, height);
    }

    public void layoutCurr(MotionEvent event, View view) {
        if (!hasNext())
            return;
        int move = (int) (downX - event.getX());
        view.layout(-move, 0, -move + width, height);
    }

    private boolean hasPre() {
        return true;
    }

    private boolean hasNext() {
        return true;
    }

    public void startAnim(int start, int end, View view) {
        startAnim(start, end, view, true);
    }

    public void startAnim(int start, int end, View view, boolean pageChange) {
        isAnim = true;
        int time = Math.abs(start - end) * 400 / width;
        ValueAnimator animation = ValueAnimator.ofInt(start, end);
        animation.setDuration(time);
        animation.addUpdateListener(animation1 -> {
            int value = (int) animation1.getAnimatedValue();
            view.layout(value, 0, value + width, height);
        });
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (pageChange && view != ad)
                    reset(view == current);
                if(pageChange && view == ad) {
                    resetAd();
                }
                isAnim = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.start();
    }

    public void resetAd() {
        adState = 0;
        ad.setElevation(0);
        ad.layout(0, 0, width, height);
    }

    public void reset(boolean isNext) {
        pre.layout(-width, 0, 0, height);
        current.layout(0, 0, width, height);
        if (isNext) {
            pageLoader.addPage();
            preString = cureString;
            cureString = nextString;
            nextString = pageLoader.getNext();
            adState += 1;
            if (adState == AdvertisementInterval + 1)
                adState = 1;
        } else {
            pageLoader.removePage();
            nextString = cureString;
            cureString = preString;
            preString = pageLoader.getPre();
            adState -= 1;
        }

        pre.setText(preString, pageLoader.getCurrentContent());
        current.setText(cureString, pageLoader.getCurrentContent());
        next.setText(nextString, pageLoader.getCurrentContent());
        if (State.isVip) {
            ad.setElevation(0);
        } else if (adState == AdvertisementInterval - 1) {
            ad.setElevation(20);
            ad.loadAd();
        } else if (adState == AdvertisementInterval) {
            ad.setElevation(40);
        } else {
            ad.setElevation(0);
        }
        next.setElevation(10);
        current.setElevation(30);
        pre.setElevation(60);
    }

    private ClickType clickType;

    public void setClickType(ClickType clickType) {
        this.clickType = clickType;
    }

    public void load(int position) {
        pageLoader.onLoadSuccess(position, 0);
    }

    public void setTextSize(int fontSize) {
        pageLoader.setTextSize(fontSize);
        pageLoader.onLoadByTextChange();
        pre.setTextSize(fontSize);
        current.setTextSize(fontSize);
        next.setTextSize(fontSize);
    }

    public void nextChapter() {
        pageLoader.nextChapter();
    }

    public void preChapter() {
        pageLoader.preChapter();
    }

    private int batteryLevel;

    public void updateBattery(int level) {
        this.batteryLevel = level;
        if (current != null) {
            pre.setBattery(level);
            current.setBattery(level);
            next.setBattery(level);
        }
    }

    public void updateTime() {
        if (current != null) {
            pre.updateTime();
            current.updateTime();
            next.updateTime();
        }
    }

    public interface ClickType {
        void onState(int state);

        boolean isShow();

        void close();

        void loadSuccess();
    }
}
