package edu.auburn.sips_android_official.ui.athlete_testing

import android.app.Application
import android.arch.lifecycle.*
import android.databinding.ObservableField
import android.os.SystemClock
import android.support.annotation.NonNull
import edu.auburn.sips_android_official.data.repository.DataRepository
import edu.auburn.sips_android_official.di.SIPSApplication
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.Resource
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.os.CountDownTimer
import android.databinding.adapters.TextViewBindingAdapter.setText
import edu.auburn.sips_android_official.util.TimerState


/**
 * Created by clj00 on 2/20/2018.
 */
class AthleteTestViewModel(@NonNull application: Application, repository: DataRepository,
                       private val mAthleteId: Int) : AndroidViewModel(application) {


    private var mTimer: Timer? = null

    private val mFormattedTime = MutableLiveData<String>()
    private val mTimerState = TimerState(false, null)

    // Getter for mFormattedTime LiveData, cast to immutable value
    val formattedTime: LiveData<String> get() = mFormattedTime

    fun startTimer() {
        if (mTimer != null) {
            return
        }

        val initialTime = SystemClock.elapsedRealtime()
        mTimer = Timer()
        mTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val secondsPassed = (SystemClock.elapsedRealtime() - initialTime) / 1000L
                mFormattedTime.postValue(formatTime(secondsPassed))

            }
        }, 1000L, 1000L)

        mTimerState.isRunning = true
        mTimerState.startedAt = Calendar.getInstance().time
    }

    fun stopTimer() {
        if (mTimer != null) {
            mTimer?.cancel()
            mTimer = null
            mFormattedTime.value = formatTime(0L)
            mTimerState.startedAt = null
            mTimerState.isRunning = false
        }
    }

    private fun formatTime(elapsedTime: Long): String {
        val seconds = elapsedTime % 3600 % 60
        val minutes = elapsedTime % 3600 / 60
        val hours = elapsedTime / 3600

        val hh = if (hours < 10) "0$hours" else hours.toString()
        val mm = if (minutes < 10) "0$minutes" else minutes.toString()
        val ss = if (seconds < 10) "0$seconds" else seconds.toString()

        return "$hh:$mm:$ss"
    }

    override fun onCleared() {
        super.onCleared()
    }

    /**
     * A creator is used to inject the athlete ID into the ViewModel
     *
     *
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the athlete ID can be passed in a public method.
     */
    class Factory(@param:NonNull @field:NonNull
                  private val mApplication: Application, private val mAthleteId: Int) : ViewModelProvider.NewInstanceFactory() {

        private val mRepository: DataRepository

        init {
            mRepository = (mApplication as SIPSApplication).getRepository()!!
        }

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return AthleteTestViewModel(mApplication, mRepository, mAthleteId) as T
        }
    }
}