package com.novel.reader.utils;

import android.content.Context;

import com.novel.reader.bean.MyObjectBox;

import io.objectbox.BoxStore;

/**
 * @author : easy on 2022/10/28 12:03
 * @description :
 */
public class ObjectBox {
    private static BoxStore mBoxStore;

    public static void init(Context context) {
        mBoxStore = MyObjectBox.builder()
                .androidContext(context)
                .build();
    }

    public static BoxStore get() { return mBoxStore; }
}
