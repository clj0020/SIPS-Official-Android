package edu.auburn.sips_android_official.data.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index
import edu.auburn.sips_android_official.data.models.TestData
import java.util.*


/**
 * Created by clj00 on 2/19/2018.
 */
@Entity(tableName = "testData",
        indices= arrayOf(Index(value = ["athleteId"])),
        foreignKeys = arrayOf(ForeignKey(
                entity = AthleteEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("athleteId"),
                onDelete = ForeignKey.CASCADE)
        ))
data class TestDataEntity (
        @PrimaryKey(autoGenerate = true) override var id: Int = 0,
        override var athleteId: Int = 0,
        override var testedAt: Date = Date(),
        override var title: String = " ",
        override var accelerometerArray: ArrayList<Array<Float>>,
        override var gyroscopeArray: ArrayList<Array<Float>>,
        override var magnetometerArray: ArrayList<Array<Float>>
) : TestData