package edu.auburn.sips_android_official.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import edu.auburn.sips_android_official.R
import android.content.ContentValues.TAG
import android.support.annotation.Nullable
import android.util.Log
import edu.auburn.sips_android_official.data.models.Athlete
import edu.auburn.sips_android_official.ui.athlete.AthleteFragment
import edu.auburn.sips_android_official.ui.athlete_testing.AthleteTestFragment
import edu.auburn.sips_android_official.ui.athletes.AthleteListFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add athlete list fragment if this is first creation
        if (savedInstanceState == null) {
            val fragment = AthleteListFragment()

            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment, AthleteListFragment.TAG).commit()
        }
    }

    /** Shows the athlete detail fragment  */
    fun show(athlete: Athlete) {

        val athleteFragment = AthleteFragment.forAthlete(athlete.id)

        supportFragmentManager
                .beginTransaction()
                .addToBackStack("athlete")
                .replace(R.id.fragment_container,
                        athleteFragment, null).commit()
    }

    /** Shows the athlete detail fragment  */
    fun showAthleteTestFragment(athleteId: Int) {

        val athleteTestFragment = AthleteTestFragment.forAthlete(athleteId)

        supportFragmentManager
                .beginTransaction()
                .addToBackStack("athleteProfile")
                .replace(R.id.fragment_container,
                        athleteTestFragment, null).commit()
    }

    /** Begin athlete test */
    fun beginTest(test: Int, athleteId: Int) {
        Log.i("MainActivity: ", "Begin testing for test #" + test + " on Athlete #" + athleteId)

        // Use CallNative to initialize sensors

        // Start Timer

            // Use CallNative to begin sensor data monitoring

            // If sensors are on

                // Prepare sensor data for storage.

                // if sensor data has been prepared for storage

                    // Add sensor data to local storage.

                    // Ask user if they want to upload sensor data to network database.

                        // If user wants to upload sensor data

                            // upload sensor data

                                // if successful

                                    // update cached athlete

                                    // show success message

                                    // redirect to athlete fragment.

                                // else

                                    // show error message

                // else

                    // show error message

            // else

                // show error message
        // End Timer


    }
}