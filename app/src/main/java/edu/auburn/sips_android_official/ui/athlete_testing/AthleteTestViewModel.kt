package edu.auburn.sips_android_official.ui.athlete_testing

import android.app.Application
import android.arch.lifecycle.*
import android.databinding.ObservableField
import android.os.SystemClock
import android.support.annotation.NonNull
import edu.auburn.sips_android_official.data.repository.DataRepository
import edu.auburn.sips_android_official.di.SIPSApplication
import java.util.*
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.hardware.Sensor
import com.github.pwittchen.reactivesensors.library.ReactiveSensors
import edu.auburn.sips_android_official.data.room.entity.TestDataEntity
import edu.auburn.sips_android_official.util.SensorHelper
import edu.auburn.sips_android_official.util.TimerState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by clj00 on 2/20/2018.
 */
class AthleteTestViewModel(@NonNull application: Application, repository: DataRepository,
                       private val mAthleteId: Int) : AndroidViewModel(application) {

    private val ONE_LEG_SQUAT_HOLD = 0
    private val SINGLE_LEG_JUMP = 1

    // Test Variables
    var test: ObservableField<Int> = ObservableField()

    // Timer Variables
    private var mTimer: Timer? = null
    private val mTimerState: TimerState
    private val mFormattedTime: MutableLiveData<String>
    val formattedTime: LiveData<String> get() = mFormattedTime // Getter for mFormattedTime LiveData, cast to immutable value

    // Sensor Variables
    private val mReactiveSensors: ReactiveSensors
    private val mSensorHelper: SensorHelper

    private var mCompositeDisposable: CompositeDisposable ?= null

    private var mRepository: DataRepository

    private var addTestDataObservable: Disposable ?= null

    private var mAccelerometerData: ArrayList<Array<Float>> = ArrayList<Array<Float>>()
    private var mGyroscopeData: ArrayList<Array<Float>> = ArrayList<Array<Float>>()
    private var mMagnometerData: ArrayList<Array<Float>> = ArrayList<Array<Float>>()

    init {
        mFormattedTime = MutableLiveData<String>()
        mTimerState = TimerState(false, null)
        mReactiveSensors = ReactiveSensors(getApplication())
        mSensorHelper = SensorHelper(mReactiveSensors)
        mRepository = repository
    }

    // Starts the timer, starts sensors
    fun startTimer() {
        if (mTimer != null) {
            return
        }

        // Starts sensors (Accelerometer, Gyroscope, Magnometer) and then subscribes to them on the computation thread, observes on the main thread.
        mCompositeDisposable = mSensorHelper.subscribeToAllSensors()

        // Set countdown time according to test

        // Set the initial time to the current time.
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

    // Stops the timer, stops sensors, and saves testing data
    fun stopTimer() {

        // Stop reading sensors
        mSensorHelper.safelyDispose(mCompositeDisposable)

        // Stop the timer
        if (mTimer != null) {
            mTimer?.cancel()
            mTimer = null
            mFormattedTime.value = formatTime(0L)
            mTimerState.startedAt = null
            mTimerState.isRunning = false
        }

        // Save the testing data
        mAccelerometerData = mSensorHelper.mAccelerometerData
        mGyroscopeData = mSensorHelper.mGyroscopeData
        mMagnometerData = mSensorHelper.mMagnometerData

        addTestDataObservable = uploadTestData(mAccelerometerData, mGyroscopeData, mMagnometerData)
    }


    // Sets the test variable
    fun setTest(test: Int) {
        this.test.set(test)
    }

    // Uploads testing data through repository
    fun uploadTestData(accelerometerData: ArrayList<Array<Float>>, gyroscopeData: ArrayList<Array<Float>>, magnometerData: ArrayList<Array<Float>>): Disposable {

        val testDate: Date = Calendar.getInstance().time

        return mRepository.addTestData(
                athleteId = this.mAthleteId,
                testDate = testDate,
                accelerometerData = accelerometerData,
                gyroscopeData = gyroscopeData,
                magnometerData = magnometerData
        )
    }

    // Formats the time for the clock
    private fun formatTime(elapsedTime: Long): String {
        val seconds = elapsedTime % 3600 % 60
        val minutes = elapsedTime % 3600 / 60
        val hours = elapsedTime / 3600

        val hh = if (hours < 10) "0$hours" else hours.toString()
        val mm = if (minutes < 10) "0$minutes" else minutes.toString()
        val ss = if (seconds < 10) "0$seconds" else seconds.toString()

        return "$hh:$mm:$ss"
    }


    // Called when viewmodel is destroyed.
    override fun onCleared() {
        super.onCleared()

        // Just in case, stop the timer. This is safe because of the checks that stopTimer makes
        mSensorHelper.safelyDispose(mCompositeDisposable)

        if (mTimer != null) {
            mTimer?.cancel()
            mTimer = null
            mFormattedTime.value = formatTime(0L)
            mTimerState.startedAt = null
            mTimerState.isRunning = false
        }
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