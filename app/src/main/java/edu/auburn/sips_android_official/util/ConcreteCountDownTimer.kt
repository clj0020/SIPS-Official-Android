package edu.auburn.sips_android_official.util

/**
 * Created by clj00 on 2/20/2018.
 */
import android.os.CountDownTimer
import io.reactivex.FlowableEmitter
import java.util.concurrent.TimeUnit

internal class ConcreteCountDownTimer(private val emitter: FlowableEmitter<Long>, periodInFuture: Long, countDownInterval: Long, private val timeUnitToEmitIn: TimeUnit)
    : CountDownTimer(TimeUnit.MILLISECONDS.convert(periodInFuture, timeUnitToEmitIn), TimeUnit.MILLISECONDS.convert(countDownInterval, timeUnitToEmitIn)) {
    override fun onFinish() {
        emitter.onNext(0)
        emitter.onComplete()
    }

    override fun onTick(millisUntilFinished: Long) {
        if (!emitter.isCancelled) {
            emitter.onNext(timeUnitToEmitIn.convert(millisUntilFinished, TimeUnit.MILLISECONDS))
        }
    }
}