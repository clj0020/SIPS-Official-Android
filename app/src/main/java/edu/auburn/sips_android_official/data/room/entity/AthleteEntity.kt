package edu.auburn.sips_android_official.data.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import edu.auburn.sips_android_official.data.models.Athlete


/**
 * Created by clj00 on 2/19/2018.
 */
@Entity(tableName = "athletes")
data class AthleteEntity(
        @PrimaryKey(autoGenerate = true) override var id: Int = 0,
        override var firstName: String = "",
        override var lastName: String = "",
        override var email: String = ""
) : Athlete
