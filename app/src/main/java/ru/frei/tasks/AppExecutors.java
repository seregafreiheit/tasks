package ru.frei.tasks;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static AppExecutors executors;
    private final Executor diskIO;
    private final Executor mainTread;
    private final Executor networkIO;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainTread) {
        this.diskIO = diskIO;
        this.mainTread = mainTread;
        this.networkIO = networkIO;
    }

    public static AppExecutors getInstance() {
        if (executors == null) {
            executors = new AppExecutors(Executors.newSingleThreadExecutor(),
                    Executors.newFixedThreadPool(3), new MainThreadExecutor());
        }
        return executors;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getMainTread() {
        return mainTread;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
