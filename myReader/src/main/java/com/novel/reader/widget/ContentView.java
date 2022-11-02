package com.novel.reader.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.novel.reader.R;
import com.novel.reader.State;
import com.novel.reader.bean.ReaderPageEntity;

/**
 * @author : easy on 2022/10/18 17:05
 * @description :
 */
public class ContentView extends LinearLayout {

    private ReaderPageView readerPageView;
    private PageAndBattery pageAndBattery;

    public ContentView(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.linear_layout, this);
        readerPageView = findViewById(R.id.text);
        pageAndBattery = findViewById(R.id.pageAndBattery);
    }

    public ContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.linear_layout, this);
        readerPageView = findViewById(R.id.text);
        pageAndBattery = findViewById(R.id.pageAndBattery);
    }

    public ContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.linear_layout, this);
        readerPageView = findViewById(R.id.text);
        pageAndBattery = findViewById(R.id.pageAndBattery);
    }

    public void setText(ReaderPageEntity txt, String content) {
        readerPageView.setEntity(txt);
        setPageAndBattery(content);
    }

    public void setBattery(int level) {
        pageAndBattery.setBattery(level);
    }

    public void setTextSize(int fontSize) {
        readerPageView.setTextSize(fontSize);
    }

    public void setPageAndBattery(String content) {
        pageAndBattery.setProgress(content);
    }

    public void updateTime() {
        pageAndBattery.updateTime();
    }
}
