package edu.auburn.sips_android_official.util

import android.util.Log

/**
 * Created by clj00 on 2/20/2018.
 */
class CallNative {
    init {
        try {
            System.loadLibrary("utcvatjni")
        } catch (e: UnsatisfiedLinkError) {
            Log.e("ERROR --", "" + e)
        }

    }

    @SuppressWarnings("JniMissingFunction")
    external fun InstantiateSensorsHandler(): Int  //initialize sensors

    @SuppressWarnings("JniMissingFunction")
    external fun StartSensors(): Int  //cut sensors on

    @SuppressWarnings("JniMissingFunction")
    external fun StartSensorsF(flag: Boolean): Int  //cut sensors && flanker on

    @SuppressWarnings("JniMissingFunction")
    external fun StopSensors(): Int  //cut sensors off

    @SuppressWarnings("JniMissingFunction")
    external fun OpenFiles(): Int  //a.dat, g.dat, c.dat

    @SuppressWarnings("JniMissingFunction")
    external fun CloseFiles(): Int  //a.dat, g.dat, c.dat

    @SuppressWarnings("JniMissingFunction")
    external fun PassFilePath(path: String): Double  //pass internal dir --> should use

    @SuppressWarnings("JniMissingFunction")
    external fun SensorState(): Boolean  //check sensors on/off

    @SuppressWarnings("JniMissingFunction")
    external fun FilesOpen(): Boolean  //checks files open

    @SuppressWarnings("JniMissingFunction")
    external fun WriteOn(): Int  //starts writing to files in sensor loop

    @SuppressWarnings("JniMissingFunction")
    external fun WriteOff(): Int  //stops writing to files in sensor loop

    @SuppressWarnings("JniMissingFunction")
    external//TODO: THIS IS DEPRECATED .. ??
    fun PackageData(x: String): Boolean  //groups accel, gyro, comp data

    @SuppressWarnings("JniMissingFunction")
    external fun Render(o: Int, oo: Int): Int  //gl renderer

    @SuppressWarnings("JniMissingFunction")
    external fun OnChanged(w: Int, h: Int): Int  //gl onSurfaceChanged

    @SuppressWarnings("JniMissingFunction")
    external fun InitializeGL(assets: String): Int  //setup gl graphics

    @SuppressWarnings("JniMissingFunction")
    external fun Load(s: Int): Int  //load sprite sheet(s) --> xxhdpi png(s) in /res/drawable

    @SuppressWarnings("JniMissingFunction")
    external fun PassID(id: String): Int  //pass user id and session id to C++

    @SuppressWarnings("JniMissingFunction")
    external fun CheckData(): Boolean  //checks if data has been packaged

    @SuppressWarnings("JniMissingFunction")
    external fun SetFlankerFlag(flag: Boolean)  //sets flanker flag

    @SuppressWarnings("JniMissingFunction")
    external fun CountAccel(): Int

    @SuppressWarnings("JniMissingFunction")
    external fun CountGyro(): Int

    @SuppressWarnings("JniMissingFunction")
    external fun CountCompass(): Int

    @SuppressWarnings("JniMissingFunction")
    external fun FlankerInit(): Int

    @SuppressWarnings("JniMissingFunction")
    external fun FlankerCheck(): Boolean

    @SuppressWarnings("JniMissingFunction")
    external fun Chime(): Boolean

    @SuppressWarnings("JniMissingFunction")
    external fun Copy(src: String, dst: String): Int

}