package com.novel.reader.bean;

import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;

import com.novel.reader.utils.ObjectBox;

import io.objectbox.Box;
import io.objectbox.annotation.Transient;

/**
 * @author : easy on 2022/10/28 11:21
 * @description :
 */
public class BaseEntity<T extends BaseEntity> {

    @Transient
    protected Box<T> boxStore;

    public BaseEntity() {
        Class<T> tClass = (Class<T>) getClass();
        boxStore = ObjectBox.get().boxFor(tClass);
    }

    public void save() {
        boxStore.put((T) this);
    }

    public void delete() {
        boxStore.remove((T) this);
    }

    public static BookEntity findBookByIndex(String index) {
        BookEntity entity = ObjectBox.get().boxFor(BookEntity.class).query().equal(BookEntity_.cover, index).build().findFirst();
        return entity;
    }
}
