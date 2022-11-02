package com.novel.reader.bean;

import android.util.Log;

import com.novel.reader.utils.ObjectBox;

import java.io.Serializable;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;

/**
 * @author : easy on 2022/10/17 17:04
 * @description :
 */
@Entity
public class BookEntity extends BaseEntity<BookEntity> implements Serializable {

    @Id(assignable = true)
    public long id; // 本地书籍中，path 的 md5 值作为本地书籍的 id  // 自己分配id@Id(assignable = true)
    public String title;
    public String author;
    public String shortIntro;
    @Index
    public String cover; // 在本地书籍中，该字段作为本地文件的路径
    public boolean hasCp;
    public int latelyFollower;
    public double retentionRatio;
    //最新更新日期
    public String updated;
    //最新阅读日期
    public String lastRead;
    public int chaptersCount;
    public String lastChapter;
    //是否更新或未阅读
    public boolean isUpdate = true;
    //是否是本地文件
    public boolean isLocal = false;
    public int readerChapter;
    public int readerPage;

    @Transient
    public List<BookChapterEntity> bookChapterEntityList;

//    @Transient
//    protected Box<BookEntity> boxStore;

//    public BookEntity() {
//        boxStore = ObjectBox.get().boxFor(BookEntity.class);
//    }

    @Override
    public void save() {
        super.save();
    }

//    @Override
//    public void delete() {
//        boxStore.remove(this);
//    }

    public long getId() {
        return id;
    }

    public void setId(long _id) {
        this.id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShortIntro() {
        return shortIntro;
    }

    public void setShortIntro(String shortIntro) {
        this.shortIntro = shortIntro;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public boolean isHasCp() {
        return hasCp;
    }

    public void setHasCp(boolean hasCp) {
        this.hasCp = hasCp;
    }

    public int getLatelyFollower() {
        return latelyFollower;
    }

    public void setLatelyFollower(int latelyFollower) {
        this.latelyFollower = latelyFollower;
    }

    public double getRetentionRatio() {
        return retentionRatio;
    }

    public void setRetentionRatio(double retentionRatio) {
        this.retentionRatio = retentionRatio;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getLastRead() {
        return lastRead;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }

    public int getChaptersCount() {
        return chaptersCount;
    }

    public void setChaptersCount(int chaptersCount) {
        this.chaptersCount = chaptersCount;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public List<BookChapterEntity> getBookChapterList() {
        return bookChapterEntityList;
    }

    public void setBookChapterList(List<BookChapterEntity> bookChapterEntityList) {
        this.bookChapterEntityList = bookChapterEntityList;
    }

}
