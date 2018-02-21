package edu.auburn.sips_android_official.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import edu.auburn.sips_android_official.data.room.entity.AthleteEntity


/**
 * Created by clj00 on 2/19/2018.
 */
@Dao
interface AthleteDao {
    @Query("SELECT * FROM athletes")
    fun loadAllAthletes(): LiveData<List<AthleteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(athletes: List<AthleteEntity>)

    @Query("SELECT * from athletes WHERE id = :athleteId")
    fun loadAthlete(athleteId: Int): LiveData<AthleteEntity>

    @Query("select * from athletes where id = :athleteId")
    fun loadAthleteSync(athleteId: Int): AthleteEntity
}