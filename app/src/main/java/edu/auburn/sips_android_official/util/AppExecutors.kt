package edu.auburn.sips_android_official.util

import android.os.Handler
import android.os.Looper
import android.support.annotation.NonNull
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * Created by clj00 on 2/19/2018.
 */
/**
 * Global executor pools for the whole application.
 *
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
class AppExecutors private constructor(private val mDiskIO: Executor, private val mNetworkIO: Executor, private val mSensor: Executor, private val mMainThread: Executor) {

    constructor() : this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3), Executors.newSingleThreadExecutor(),
            MainThreadExecutor()) {
    }

    fun diskIO(): Executor {
        return mDiskIO
    }

    fun networkIO(): Executor {
        return mNetworkIO
    }

    fun mainThread(): Executor {
        return mMainThread
    }

    fun sensorThread(): Executor {
        return mSensor
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(@NonNull command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}