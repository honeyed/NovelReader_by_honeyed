package com.novel.reader.bean;

import java.util.List;

/**
 * @author : easy on 2022/10/21 17:32
 * @description :
 */
public class ReaderPageEntity {
    public int position;
    public String title;
    public int titleLines; //当前 lines 中为 title 的行数。
    public List<String> lines;
}
