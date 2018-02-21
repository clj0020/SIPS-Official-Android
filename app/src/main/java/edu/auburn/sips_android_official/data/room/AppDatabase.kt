package edu.auburn.sips_android_official.data.room

import android.arch.lifecycle.LiveData
import java.nio.file.Files.exists
import android.os.AsyncTask.execute
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.VisibleForTesting
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.Database
import android.content.Context
import android.support.annotation.NonNull
import android.util.Log
import edu.auburn.sips_android_official.data.room.converter.DateConverter
import edu.auburn.sips_android_official.data.room.dao.AthleteDao
import edu.auburn.sips_android_official.data.room.dao.TestDataDao
import edu.auburn.sips_android_official.data.room.entity.AthleteEntity
import edu.auburn.sips_android_official.data.room.entity.TestDataEntity
import edu.auburn.sips_android_official.util.AppExecutors


/**
 * Created by clj00 on 2/19/2018.
 */

@Database(entities = arrayOf(AthleteEntity::class, TestDataEntity::class), version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    private val mIsDatabaseCreated = MutableLiveData<Boolean>()

    val databaseCreated: LiveData<Boolean>
        get() = mIsDatabaseCreated

    abstract fun athleteDao(): AthleteDao

    abstract fun testDataDao(): TestDataDao

    /**
     * Check whether the database already exists and expose it via [.getDatabaseCreated]
     */
    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated()
        }
    }

    private fun setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true)
    }

    companion object {

        private var sInstance: AppDatabase? = null

        @VisibleForTesting
        val DATABASE_NAME = "sips-db"

        fun getInstance(context: Context, executors: AppExecutors): AppDatabase? {
            if (sInstance == null) {
                synchronized(AppDatabase::class.java) {
                    if (sInstance == null) {
                        sInstance = buildDatabase(context.getApplicationContext(), executors)
                        sInstance!!.updateDatabaseCreated(context.getApplicationContext())
                    }
                }
            }
            return sInstance
        }

        /**
         * Build the database. [Builder.build] only sets up the database configuration and
         * creates a new instance of the database.
         * The SQLite database is only created when it's accessed for the first time.
         */
        private fun buildDatabase(appContext: Context,
                                  executors: AppExecutors): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            executors.diskIO().execute({
                                // Add a delay to simulate a long-running operation
                                addDelay()
                                // Generate the data for pre-population
                                val database = AppDatabase.getInstance(appContext, executors)
                                val athletes = DataGenerator.generateAthletes()
                                val testData = DataGenerator.generateTestDataForAthletes(athletes)

                                if (database != null) {
                                    insertData(database, athletes, testData)
                                }
                                // notify that the database was created and it's ready to be used
                                database?.setDatabaseCreated()
                            })
                        }
                    }).build()
        }

        private fun insertData(database: AppDatabase, athletes: List<AthleteEntity>,
                               testData: List<TestDataEntity>) {
            database.runInTransaction {
                database.athleteDao().insertAll(athletes)
                database.testDataDao().insertAll(testData)
            }
        }

        private fun addDelay() {
            try {
                Thread.sleep(4000)
            } catch (ignored: InterruptedException) {
            }

        }
    }
}