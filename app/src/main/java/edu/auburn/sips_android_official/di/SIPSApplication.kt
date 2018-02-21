package edu.auburn.sips_android_official.di

import android.app.Application
import edu.auburn.sips_android_official.data.repository.DataRepository
import edu.auburn.sips_android_official.data.room.AppDatabase
import edu.auburn.sips_android_official.util.AppExecutors




/**
 * Created by clj00 on 1/30/2018.
 */
class SIPSApplication : Application() {

    private var mAppExecutors: AppExecutors? = null

    override fun onCreate() {
        super.onCreate()

        mAppExecutors = AppExecutors()
    }

    fun getDatabase(): AppDatabase? {
        return AppDatabase.getInstance(this, this.mAppExecutors!!)
    }

    fun getRepository(): DataRepository? {
        return DataRepository.getInstance(this.getDatabase()!!)
    }

//    companion object {
//        lateinit var appComponent: AppComponent
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        initializeDagger()
//    }
//
//    fun initializeDagger() {
//        appComponent = DaggerAppComponent.builder()
//                .appModule(AppModule(this))
////                .roomModule(RoomModule())
////                .remoteModule(RemoteModule())
//                .build()
//    }
}