package edu.auburn.sips_android_official.util

import io.reactivex.internal.disposables.DisposableHelper.dispose
import io.reactivex.internal.disposables.DisposableHelper.isDisposed
import io.reactivex.disposables.Disposable
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.hardware.Sensor
import com.github.pwittchen.reactivesensors.library.SensorNotFoundException
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.util.Log
import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent
import com.github.pwittchen.reactivesensors.library.ReactiveSensorFilter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.widget.TextView
import com.github.pwittchen.reactivesensors.library.ReactiveSensors
import edu.auburn.sips_android_official.data.models.TestData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by clj00 on 2/27/2018.
 */
internal class SensorHelper(private val reactiveSensors: ReactiveSensors) {

    val mAccelerometerData: ArrayList<Array<Float>>
    val mGyroscopeData: ArrayList<Array<Float>>
    val mMagnometerData: ArrayList<Array<Float>>
    private val mCompositeDisposable: CompositeDisposable

    init {
        mAccelerometerData = ArrayList<Array<Float>>()
        mGyroscopeData = ArrayList<Array<Float>>()
        mMagnometerData = ArrayList<Array<Float>>()
        mCompositeDisposable = CompositeDisposable()
    }

    // Subscribe to sensors using ComposteDisposable
    fun subscribeToAllSensors(): CompositeDisposable {

        if (reactiveSensors.hasSensor(Sensor.TYPE_ACCELEROMETER)) {
            Log.i("SensorHelper: ", " Accelerometer found. Reading values...")

            val accelerometerSubscription: Disposable = reactiveSensors.observeSensor(Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_FASTEST)
                    .filter(ReactiveSensorFilter.filterSensorChanged())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Consumer<ReactiveSensorEvent> {
                        @Throws(Exception::class)
                        override fun accept(reactiveSensorEvent: ReactiveSensorEvent) {
                            val event = reactiveSensorEvent.sensorEvent

                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]

                            mAccelerometerData.add(arrayOf(x, y, z))

                            val format = "%s readings:\n x = %f\n y = %f\n z = %f"
                            val message = String.format(Locale.getDefault(), format, "accelerometer", x, y, z)
                            Log.i("SensorHelper: ", message)
                        }
                    }, object : Consumer<Throwable> {
                        @Throws(Exception::class)
                        override fun accept(throwable: Throwable) {
                            if (throwable is SensorNotFoundException) {
                                Log.i("SensorHelper: ", "Sorry, your device doesn't have required sensor.")
                            }
                        }
                    })

            mCompositeDisposable.add(accelerometerSubscription)
        }

        if (reactiveSensors.hasSensor(Sensor.TYPE_GYROSCOPE)) {
            Log.i("SensorHelper: ", " Gyroscope found. Reading values...")
            val gyroscopeSubscription: Disposable = reactiveSensors.observeSensor(Sensor.TYPE_GYROSCOPE, SensorManager.SENSOR_DELAY_FASTEST)
                    .filter(ReactiveSensorFilter.filterSensorChanged())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Consumer<ReactiveSensorEvent> {
                        @Throws(Exception::class)
                        override fun accept(reactiveSensorEvent: ReactiveSensorEvent) {
                            val event = reactiveSensorEvent.sensorEvent

                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]

                            mGyroscopeData.add(arrayOf(x, y, z))

                            val format = "%s readings:\n x = %f\n y = %f\n z = %f"
                            val message = String.format(Locale.getDefault(), format, "gyroscope", x, y, z)
                            Log.i("SensorHelper: ", message)
                        }
                    }, object : Consumer<Throwable> {
                        @Throws(Exception::class)
                        override fun accept(throwable: Throwable) {
                            if (throwable is SensorNotFoundException) {
                                Log.i("SensorHelper: ", "Sorry, your device doesn't have required sensor.")
                            }
                        }
                    })

            mCompositeDisposable.add(gyroscopeSubscription)
        }

        if (reactiveSensors.hasSensor(Sensor.TYPE_MAGNETIC_FIELD)) {
            Log.i("SensorHelper: ", " Magnometer found. Reading values...")
            val magnometerSubscription: Disposable = reactiveSensors.observeSensor(Sensor.TYPE_MAGNETIC_FIELD, SensorManager.SENSOR_DELAY_FASTEST)
                    .filter(ReactiveSensorFilter.filterSensorChanged())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Consumer<ReactiveSensorEvent> {
                        @Throws(Exception::class)
                        override fun accept(reactiveSensorEvent: ReactiveSensorEvent) {
                            val event = reactiveSensorEvent.sensorEvent

                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]

                            mMagnometerData.add(arrayOf(x, y, z))

                            val format = "%s readings:\n x = %f\n y = %f\n z = %f"
                            val message = String.format(Locale.getDefault(), format, "magnometer", x, y, z)
                            Log.i("SensorHelper: ", message)
                        }
                    }, object : Consumer<Throwable> {
                        @Throws(Exception::class)
                        override fun accept(throwable: Throwable) {
                            if (throwable is SensorNotFoundException) {
                                Log.i("SensorHelper: ", "Sorry, your device doesn't have required sensor.")
                            }
                        }
                    })

            mCompositeDisposable.add(magnometerSubscription)
        }

        return mCompositeDisposable
    }

    fun safelyDispose(compositeDisposable: CompositeDisposable?) {

        if (compositeDisposable != null && !compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}