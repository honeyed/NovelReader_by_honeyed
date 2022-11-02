package com.easy.read;

import android.app.Application;

import com.novel.reader.ReaderApp;

/**
 * @author : easy on 2022/11/2 11:04
 * @description :
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ReaderApp.init(this);
    }
}
