package edu.auburn.sips_android_official.data.room.dao

import android.arch.persistence.room.OnConflictStrategy
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import edu.auburn.sips_android_official.data.room.entity.TestDataEntity


/**
 * Created by clj00 on 2/19/2018.
 */
@Dao
interface TestDataDao {

    @Query("SELECT * FROM testData where athleteId = :athleteId")
    fun loadTestData(athleteId: Int): LiveData<List<TestDataEntity>>

    @Query("SELECT * FROM testData where athleteId = :athleteId")
    fun loadTestDataSync(athleteId: Int): List<TestDataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(testData: List<TestDataEntity>);
}