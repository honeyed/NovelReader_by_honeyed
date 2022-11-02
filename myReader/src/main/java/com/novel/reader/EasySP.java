package com.novel.reader;

import com.tencent.mmkv.MMKV;

/**
 * description:一些优化建议：
 * 1.初始化sp放在application；
 * 2.不要频繁的commit/apply,尽量使用一次事物提交；
 * 3.优先选择用apply而不是commit，因为commit会卡UI；
 * 4.sp是轻量级的存储工具，所以请你不要存放太大的数据，不要存json等；
 * 5.单个sp文件不要太大，如果数据量很大，请把关联性比较大的，高频操作的放在单独的sp文件
 * <p>
 * author: tianhonglong
 * new date: 2021/7/6
 * version: v 1.0
 */
public class EasySP {

    // 私有化静态成员变量，已初始化
    private final static EasySP instance = new EasySP();
    private MMKV share = MMKV.defaultMMKV();

    private EasySP() {
    }

    public static EasySP getInstance() {
        return instance;
    }

    /**
     * ------- Int ---------
     */
    public EasySP putInt(String spName, int value) {
        share.putInt(spName, value);
        return this;
    }

    public int getInt(String spName, int defaultvalue) {
        return share.getInt(spName, defaultvalue);
    }

    public EasySP putString(String spName, String value) {
        share.putString(spName, value);
        return this;
    }

    public String getString(String spName, String defaultvalue) {
        return share.getString(spName, defaultvalue);
    }

    public String getString(String spName) {
        return share.getString(spName, "");
    }


    /**
     * ------- boolean ---------
     */
    public EasySP putBoolean(String key, boolean value) {
        share.putBoolean(key, value);
        return this;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return share.getBoolean(key, defValue);
    }

    public boolean getBoolean(String key) {
        return share.getBoolean(key, false);
    }

    /**
     * ------- float ---------
     */
    public EasySP putFloat(String key, float value) {
        share.putFloat(key, value);
        return this;
    }

    public float getFloat(String key, float defValue) {
        return share.getFloat(key, defValue);
    }


    /**
     * ------- long ---------
     */
    public EasySP putLong(String key, long value) {
        share.putLong(key, value);
        return this;
    }

    public long getLong(String key, long defValue) {
        return share.getLong(key, defValue);
    }

    /**
     * 清空SP里所有数据 谨慎调用
     */
    public EasySP clear() {
        share.clear();//清空
        return this;
    }

    /**
     * 删除SP里指定key对应的数据项
     *
     * @param key
     */
    public EasySP remove(String key) {
        share.remove(key);//删除掉指定的值
        return this;//提交
    }

    /**
     * 查看sp文件里面是否存在此 key
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return share.contains(key);
    }

    public void apply() {
        share.apply();
    }

    public void commit() {
        share.commit();
    }
}
