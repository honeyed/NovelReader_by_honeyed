package com.novel.reader.bean;

/**
 * @author : easy on 2022/10/17 17:06
 * @description :
 */
public class BookChapterEntity {

    public long id;

    public String link;

    public String title;

    //所属的下载任务
    public String taskName;

    public boolean unreadble;

    //所属的书籍的id
    public String bookId;

    //本地书籍参数


    //在书籍文件中的起始位置
    public long start;

    //在书籍文件中的终止位置
    public long end;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isUnreadble() {
        return unreadble;
    }

    public void setUnreadble(boolean unreadble) {
        this.unreadble = unreadble;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

}
