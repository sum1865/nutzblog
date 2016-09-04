package net.javablog.util;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Threads {

    static ExecutorService pool = Executors.newSingleThreadExecutor();
    public static void run(Runnable run) {
        pool.execute(run);
    }

}
