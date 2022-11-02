package com.novel.reader.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : easy on 2022/10/21 16:34
 * @description :
 */
public class ExecutorServiceUtils {

    public static ExecutorServiceUtils getInstance() {
        return ExecutorServiceUtilsInner.serviceUtils;
    }

    private ExecutorServiceUtils() {}

    private static class ExecutorServiceUtilsInner {
        private static ExecutorServiceUtils serviceUtils = new ExecutorServiceUtils();
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    public void run(Runnable runnable) {
        executor.execute(runnable);
    }
}
