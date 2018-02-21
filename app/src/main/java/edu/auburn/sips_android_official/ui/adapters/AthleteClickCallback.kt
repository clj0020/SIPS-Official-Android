package edu.auburn.sips_android_official.ui.adapters

import edu.auburn.sips_android_official.data.models.Athlete

/**
 * Created by clj00 on 2/19/2018.
 */
interface AthleteClickCallback {
    fun onClick(athlete: Athlete)
}