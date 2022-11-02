package com.novel.reader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.githang.statusbar.StatusBarCompat;
import com.novel.reader.bean.BookEntity;
import com.novel.reader.utils.BrightnessUtils;
import com.novel.reader.utils.ScreenUtils;
import com.novel.reader.widget.CatalogAdapter;
import com.novel.reader.widget.ReaderView;

/**
 * @author : easy on 2022/10/18 11:03
 * @description :
 */
public class MyReaderActivity extends Activity {

    private ReaderView readerView;
    private BookEntity entity;
    private View read_ll_bottom_menu;
    private View read_ll_top_menu;
    private ReadSettingManager mSettingManager;

    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private Animation mLeftInAnim;
    private Animation mLeftOutAnim;
    private View catalogView;
    private RecyclerView catalogRecycler;
    /** bottom View 控制器 */
    private View chapterSelect, font, brightness;
    /** bottom View 亮度 */
    private ImageView mIvBrightnessPlus;
    private ImageView mIvBrightnessMinus;
    private CheckBox mCbBrightnessAuto;
    private SeekBar mSbBrightness;
    /** bottom View 字体 */
    private static final int DEFAULT_TEXT_SIZE = 16;
    private TextView mTvFontMinus;
    private TextView mTvFontPlus;
    private TextView mTvFont;
    private CheckBox mCbFontDefault;
    private View bottomAd;


    private int mBrightness;
    private int mTextSize;

    private boolean isBrightnessAuto;
    private boolean isTextDefault;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        State.mDisplayWidth = getResources().getDisplayMetrics().widthPixels;
        if (State.isVip) {
            State.mDisplayHeight = getResources().getDisplayMetrics().heightPixels;
        } else {
            State.mDisplayHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.9f);
        }
        setContentView(R.layout.activity_easy_reader);
        readerView = findViewById(R.id.bookView);
        bottomAd = findViewById(R.id.bottomAd);
        read_ll_bottom_menu = findViewById(R.id.read_ll_bottom_menu);
        read_ll_top_menu = findViewById(R.id.read_ll_top_menu);
        catalogView = findViewById(R.id.catalogView);
        catalogRecycler = findViewById(R.id.catalogRecycler);
        entity = (BookEntity) getIntent().getSerializableExtra("book");
        readerView.startReader(entity);
        readerView.setClickType(new ReaderView.ClickType() {
            @Override
            public void onState(int state) {
                if (state == 1) {
                    toggleMenu(read_ll_bottom_menu.getVisibility() == View.GONE);
                } else {
                    toggleMenu(false);
                }
            }

            @Override
            public boolean isShow() {
                return read_ll_bottom_menu.getVisibility() == View.VISIBLE || catalogView.getVisibility() == View.VISIBLE;
            }

            @Override
            public void close() {
                toggleMenu(false);
                if(catalogView.getVisibility() == View.VISIBLE) {
                    catalogView.setVisibility(View.GONE);
                    catalogView.startAnimation(mLeftOutAnim);
                }
            }

            @Override
            public void loadSuccess() {
                CatalogAdapter catalogAdapter = new CatalogAdapter(readerView.getChapterList());
                catalogRecycler.setLayoutManager(new LinearLayoutManager(MyReaderActivity.this));
                catalogRecycler.setAdapter(catalogAdapter);
                catalogAdapter.setItemClickListener(new CatalogAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        readerView.load(position);
                        if(catalogView.getVisibility() == View.VISIBLE) {
                            catalogView.setVisibility(View.GONE);
                            catalogView.startAnimation(mLeftOutAnim);
                        }
                    }
                });
            }
        });
        initAnim();
        initBottomView();
    }

    public void initAnim() {
        mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
        mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);

        mLeftInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        mLeftOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (State.isVip) {
            bottomAd.setVisibility(View.GONE);
        } else {
            bottomAd.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 切换菜单栏的可视状态
     * 默认是隐藏的
     */
    private void toggleMenu(boolean show) {
        if (show) {
            if (read_ll_bottom_menu.getVisibility() == View.VISIBLE)
                return;
            read_ll_bottom_menu.setVisibility(View.VISIBLE);
            read_ll_bottom_menu.startAnimation(mBottomInAnim);
            read_ll_top_menu.setVisibility(View.VISIBLE);
            read_ll_top_menu.startAnimation(mTopInAnim);
            setTheme(R.style.BaseAppTheme);
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#191919"));
        } else {
            if (read_ll_bottom_menu.getVisibility() == View.GONE)
                return;
            //关闭
            read_ll_bottom_menu.setVisibility(View.GONE);
            read_ll_bottom_menu.startAnimation(mBottomOutAnim);
            read_ll_top_menu.setVisibility(View.GONE);
            read_ll_top_menu.startAnimation(mTopOutAnim);
            setTheme(R.style.ReadAppThemeFullscreen);
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#2094f4"));
        }
    }

    public static void startActivity(Context mContext, BookEntity entity) {
        Intent intent = new Intent(mContext, MyReaderActivity.class);
        intent.putExtra("book", entity);
        mContext.startActivity(intent);
    }

    public void showCatalog(View view) {
        toggleMenu(false);
        catalogView.setVisibility(View.VISIBLE);
        catalogView.startAnimation(mLeftInAnim);
    }

    public void read_tv_setting(View view) {
        chapterSelect = findViewById(R.id.chapterSelect);
        font = findViewById(R.id.font);
        brightness = findViewById(R.id.brightness);
        if(chapterSelect.getVisibility() == View.VISIBLE) {
            chapterSelect.setVisibility(View.GONE);
            brightness.setVisibility(View.VISIBLE);
            font.setVisibility(View.VISIBLE);
        } else {
            chapterSelect.setVisibility(View.VISIBLE);
            brightness.setVisibility(View.GONE);
            font.setVisibility(View.GONE);
        }
    }

    public void initBottomView() {
        mSettingManager = ReadSettingManager.getInstance();

        chapterSelect = findViewById(R.id.chapterSelect);
        font = findViewById(R.id.font);
        brightness = findViewById(R.id.brightness);

        isBrightnessAuto = mSettingManager.isBrightnessAuto();
        mBrightness = mSettingManager.getBrightness();
        mTextSize = mSettingManager.getTextSize();
        isTextDefault = mSettingManager.isDefaultTextSize();

        //亮度
        mSbBrightness = findViewById(R.id.read_setting_sb_brightness);
        mIvBrightnessMinus = findViewById(R.id.read_setting_iv_brightness_minus);
        mIvBrightnessPlus = findViewById(R.id.read_setting_iv_brightness_plus);
        mCbBrightnessAuto = findViewById(R.id.read_setting_cb_brightness_auto);

        //亮度调节
        mIvBrightnessMinus.setOnClickListener(
                (v) -> {
                    if (mCbBrightnessAuto.isChecked()) {
                        mCbBrightnessAuto.setChecked(false);
                    }
                    int progress = mSbBrightness.getProgress() - 1;
                    if (progress < 0) return;
                    mSbBrightness.setProgress(progress);
                    BrightnessUtils.setBrightness(MyReaderActivity.this, progress);
                }
        );
        mIvBrightnessPlus.setOnClickListener(
                (v) -> {
                    if (mCbBrightnessAuto.isChecked()) {
                        mCbBrightnessAuto.setChecked(false);
                    }
                    int progress = mSbBrightness.getProgress() + 1;
                    if (progress > mSbBrightness.getMax()) return;
                    mSbBrightness.setProgress(progress);
                    BrightnessUtils.setBrightness(MyReaderActivity.this, progress);
                    //设置进度
                    ReadSettingManager.getInstance().setBrightness(progress);
                }
        );

        mSbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (mCbBrightnessAuto.isChecked()) {
                    mCbBrightnessAuto.setChecked(false);
                }
                //设置当前 Activity 的亮度
                BrightnessUtils.setBrightness(MyReaderActivity.this, progress);
                //存储亮度的进度条
                ReadSettingManager.getInstance().setBrightness(progress);
            }
        });

        mCbBrightnessAuto.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        //获取屏幕的亮度
                        BrightnessUtils.setBrightness(MyReaderActivity.this, BrightnessUtils.getScreenBrightness(MyReaderActivity.this));
                    } else {
                        //获取进度条的亮度
                        BrightnessUtils.setBrightness(MyReaderActivity.this, mSbBrightness.getProgress());
                    }
                    ReadSettingManager.getInstance().setAutoBrightness(isChecked);
                }
        );

        mTvFontMinus = findViewById(R.id.read_setting_tv_font_minus);
        mTvFontPlus = findViewById(R.id.read_setting_tv_font_plus);
        mTvFont = findViewById(R.id.read_setting_tv_font);
        mCbFontDefault = findViewById(R.id.read_setting_cb_font_default);

        mTvFont.setText(mTextSize + "");
        //字体大小调节
        mTvFontMinus.setOnClickListener(
                (v) -> {
                    if (mCbFontDefault.isChecked()) {
                        mCbFontDefault.setChecked(false);
                    }
                    int fontSize = Integer.valueOf(mTvFont.getText().toString()) - 1;
                    if (fontSize < 0) return;
                    mTvFont.setText(fontSize + "");
                    readerView.setTextSize(fontSize);
                }
        );

        mTvFontPlus.setOnClickListener(
                (v) -> {
                    if (mCbFontDefault.isChecked()) {
                        mCbFontDefault.setChecked(false);
                    }
                    int fontSize = Integer.valueOf(mTvFont.getText().toString()) + 1;
                    mTvFont.setText(fontSize + "");
                    readerView.setTextSize(fontSize);
                }
        );
        mCbFontDefault.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        int fontSize = ScreenUtils.dpToPx(DEFAULT_TEXT_SIZE);
                        mTvFont.setText(fontSize + "");
                        readerView.setTextSize(fontSize);
                    }
                }
        );

        findViewById(R.id.read_tv_pre_chapter).setOnClickListener(v -> readerView.preChapter());

        findViewById(R.id.read_tv_next_chapter).setOnClickListener(v -> readerView.nextChapter());

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, intentFilter);

    }



    // 接收电池信息和时间更新的广播
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                readerView.updateBattery(level);
            }
            // 监听分钟的变化
            else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                readerView.updateTime();
            }
        }
    };
}
