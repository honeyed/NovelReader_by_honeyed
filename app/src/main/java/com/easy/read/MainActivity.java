package com.easy.read;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.novel.reader.MyReaderActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //动态获取权限需要添加的常量
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //动态申请储存权限
        boolean sSRPR=ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)|ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if(sSRPR) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }else{
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    public void click(View view) {
        File file = new File("/storage/emulated/0/com.easy.reader/农家小福女/农家小福女.txt");
        com.novel.reader.bean.BookEntity collBook = new com.novel.reader.bean.BookEntity();
//                collBook.set_id(MD5Utils.strToMd5By16(file.getAbsolutePath()));
        collBook.setTitle(file.getName().replace(".txt", ""));
        collBook.setAuthor("我是神");
        collBook.setShortIntro("无");
        collBook.setCover(file.getAbsolutePath());
        collBook.setLocal(true);
        collBook.setLastChapter("开始阅读");
        MyReaderActivity.startActivity(this, collBook);
    }



}