package com.novel.reader;

import android.app.Application;
import android.content.Context;

import com.novel.reader.utils.ObjectBox;
import com.tencent.mmkv.MMKV;

/**
 * @author : easy on 2022/9/27 17:06
 * @description :
 */
public class ReaderApp {

    public static Application app;

    public static void init(Application app) {
        MMKV.initialize(app);
        ReaderApp.app = app;
        ObjectBox.init(app);
    }

    private ReaderApp() {
    }

    public static Context getContext() {
        return app;
    }
}