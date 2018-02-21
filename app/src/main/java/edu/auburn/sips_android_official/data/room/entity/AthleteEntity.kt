package edu.auburn.sips_android_official.data.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import edu.auburn.sips_android_official.data.models.Athlete


/**
 * Created by clj00 on 2/19/2018.
 */
@Entity(tableName = "athletes")
data class AthleteEntity(
        @PrimaryKey override var id: Int = 0,
        override var name: String = ""
) : Athlete
