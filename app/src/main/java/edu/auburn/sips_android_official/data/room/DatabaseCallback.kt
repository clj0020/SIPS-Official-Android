package edu.auburn.sips_android_official.data.room

import edu.auburn.sips_android_official.data.models.Athlete

/**
 * Created by clj00 on 3/1/2018.
 */
interface DatabaseCallback {

    fun onAthleteAdded(athleteId: Int)

}