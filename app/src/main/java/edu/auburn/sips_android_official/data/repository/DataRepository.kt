package edu.auburn.sips_android_official.data.repository

import android.arch.lifecycle.LiveData
import edu.auburn.sips_android_official.data.room.AppDatabase
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import edu.auburn.sips_android_official.data.models.TestData
import edu.auburn.sips_android_official.data.room.entity.AthleteEntity
import edu.auburn.sips_android_official.data.room.entity.TestDataEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import java.util.*


/**
 * Created by clj00 on 2/19/2018.
 */
/**
 * Repository handling the work with athletes and test data.
 */
class DataRepository private constructor(private val mDatabase: AppDatabase) {
    private val mObservableAthletes: MediatorLiveData<List<AthleteEntity>>

    /**
     * Get the list of athletes from the database and get notified when the data changes.
     */
    val athletes: LiveData<List<AthleteEntity>>
        get() = mObservableAthletes

    init {
        mObservableAthletes = MediatorLiveData<List<AthleteEntity>>()

        mObservableAthletes.addSource(mDatabase.athleteDao().loadAllAthletes()
        ) { athleteEntities ->
            if (mDatabase.databaseCreated.value != null) {
                mObservableAthletes.postValue(athleteEntities)
            }
        }
    }

    fun loadAthlete(athleteId: Int): LiveData<AthleteEntity> {
        return mDatabase.athleteDao().loadAthlete(athleteId)
    }

    fun loadTestData(athleteId: Int): LiveData<List<TestDataEntity>> {
        return mDatabase.testDataDao().loadTestData(athleteId)
    }

    fun addTestData(testDate: Date,
                    accelerometerData: ArrayList<Array<Float>>,
                    gyroscopeData: ArrayList<Array<Float>>,
                    magnometerData: ArrayList<Array<Float>>,
                    athleteId: Int): Disposable {

        val testData = TestDataEntity(
                athleteId = athleteId,
                testedAt = testDate,
                title = "Test",
                accelerometerArray = accelerometerData,
                gyroscopeArray = gyroscopeData,
                magnetometerArray = magnometerData)

        return Single.fromCallable {
            mDatabase.testDataDao().insert(testData)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }


    companion object {

        private var sInstance: DataRepository? = null

        fun getInstance(database: AppDatabase): DataRepository? {
            if (sInstance == null) {
                synchronized(DataRepository::class.java) {
                    if (sInstance == null) {
                        sInstance = DataRepository(database)
                    }
                }
            }
            return sInstance
        }
    }
}