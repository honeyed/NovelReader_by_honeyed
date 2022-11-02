package com.novel.reader;

import android.content.Context;

import com.novel.reader.bean.BookChapterEntity;
import com.novel.reader.bean.ReaderPageEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : easy on 2022/10/17 10:34
 * @description :
 */
public class MyPageLoader extends BaseLoader {

    private int currentPage = 0;
    private boolean hasPre = false, hasNext = true;
    private LoadSuccess loadSuccess;

    // 上一章的页面列表缓存
    private List<ReaderPageEntity> mPrePageList;
    // 当前章节的页面列表
    private List<ReaderPageEntity> mCurPageList;
    // 下一章的页面列表缓存
    private List<ReaderPageEntity> mNextPageList;

    private int currentChapterPosition = 0;

    public MyPageLoader(Context context) {
        super(context);
    }


    @Override
    public void onLoadSuccess(int chapter, int page) {
        if (chapter < 0) {
            chapter = 0;
        }
        if(chapter >= mChapterList.size()) {
            chapter -= 1;
        }
        currentChapterPosition = chapter;
        if (currentChapterPosition > 0) {
            try {
                mPrePageList = loadPageList(currentChapterPosition - 1);
//                hasPre = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mPrePageList = null;
        }

        try {
            mCurPageList = loadPageList(currentChapterPosition);
            if (page >= mCurPageList.size()) {
                page = mCurPageList.size() - 1;
            }
//            if (currentChapterPosition == 0 && page > 0) {
//                hasPre = true;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mCurPageList.size() > page) {
            currentPage = page;
        } else {
            currentPage = mCurPageList.size() - 1;
        }
        if (currentChapterPosition < mChapterList.size() - 1) {
            try {
                mNextPageList = loadPageList(currentChapterPosition + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mNextPageList = null;
//            hasNext = false;
        }
        loadSuccess.onLoadSuccessListener();
        refresh();
    }

    public void onLoadByTextChange() {
        onLoadSuccess(currentChapterPosition, currentPage);
    }

    public void setLoadSuccess(LoadSuccess loadSuccess) {
        this.loadSuccess = loadSuccess;
    }

    public ReaderPageEntity getPre() {
        if (hasPre) {
            if (currentPage == 0)
                return mPrePageList.get(mPrePageList.size() - 1);
            else
                return mCurPageList.get(currentPage - 1);
        }
        return null;
    }

    public ReaderPageEntity getCurrent() {
        return mCurPageList.get(currentPage);
    }

    public ReaderPageEntity getNext() {
        if (hasNext) {
            if (mCurPageList.size() == currentPage + 1) {
                return mNextPageList.get(0);
            } else
                return mCurPageList.get(currentPage + 1);
        } else
            return null;
    }

    public void addPage() {
        if (mCurPageList.size() == currentPage + 1) {
            currentPage = 0;
            currentChapterPosition += 1;
            try {
                mPrePageList = mCurPageList;
                mCurPageList = mNextPageList;
                if(currentChapterPosition + 1 >= mChapterList.size()) {
                    mNextPageList = null;
                } else {
                    mNextPageList = loadPageList(currentChapterPosition + 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            currentPage += 1;
        }
        refresh();
    }

    public String getCurrentContent() {
        return "第" + (currentChapterPosition + 1) + "章:" + (currentPage + 1) + "/" + mCurPageList.size() + "页; 共:" + mChapterList.size() + "章";
    }

    public void removePage() {
        if (currentPage == 0) {
            currentPage = mPrePageList.size() - 1;
            currentChapterPosition -= 1;
            try {
                mNextPageList = mCurPageList;
                mCurPageList = mPrePageList;
                if (currentChapterPosition > 0)
                    mPrePageList = loadPageList(currentChapterPosition - 1);
                else
                    mPrePageList = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            currentPage -= 1;
        }
        refresh();
    }

    public void refresh() {
        if (currentPage <= 0 && mPrePageList == null) {
            hasPre = false;
        } else {
            hasPre = true;
        }
        if (mCurPageList.size() <= currentPage + 1 && mNextPageList == null) {
            hasNext = false;
        } else {
            hasNext = true;
        }
        bookEntity.readerChapter = currentChapterPosition;
        bookEntity.readerPage = currentPage;
        bookEntity.save();
    }

    public boolean hasPre() {
        return hasPre;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public void nextChapter() {
        if (currentChapterPosition + 1 >= mChapterList.size())
            return;
        onLoadSuccess(currentChapterPosition + 1, 0);
    }

    public void preChapter() {
        if (currentChapterPosition <= 0)
            return;
        onLoadSuccess(currentChapterPosition - 1, 0);
    }

    public interface LoadSuccess {
        void onLoadSuccessListener();
    }


}
