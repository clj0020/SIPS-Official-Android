package edu.auburn.sips_android_official.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import edu.auburn.sips_android_official.R
import android.support.annotation.Nullable
import android.view.Menu
import edu.auburn.sips_android_official.data.models.Athlete
import edu.auburn.sips_android_official.ui.add_athlete.AddAthleteFragment
import edu.auburn.sips_android_official.ui.athlete.AthleteFragment
import edu.auburn.sips_android_official.ui.athlete_testing.AthleteTestFragment
import edu.auburn.sips_android_official.ui.athletes.AthleteListFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpToolbar()

        // Add athlete list fragment if this is first creation
        if (savedInstanceState == null) {
            val fragment = AthleteListFragment()

            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment, AthleteListFragment.TAG).commit()
        }
    }


    /** Shows the athlete detail fragment  */
    fun showAthleteFragment(athleteId: Int) {

        val athleteFragment = AthleteFragment.forAthlete(athleteId)

        supportFragmentManager
                .beginTransaction()
                .addToBackStack("athlete")
                .replace(R.id.fragment_container,
                        athleteFragment, "athlete").commit()
    }

    /** Shows the athlete detail fragment  */
    fun showAthleteTestFragment(athleteId: Int) {

        val athleteTestFragment = AthleteTestFragment.forAthlete(athleteId)

        supportFragmentManager
                .beginTransaction()
                .addToBackStack("athleteTest")
                .replace(R.id.fragment_container,
                        athleteTestFragment, "athleteTest").commit()

    }

    /** Shows the add athlete fragment  */
    fun showAddAthleteFragment() {
        val addAthleteFragment = AddAthleteFragment.createAddAthleteFragment()

        supportFragmentManager
                .beginTransaction()
                .addToBackStack("addAthlete")
                .replace(R.id.fragment_container,
                        addAthleteFragment, "addAthlete").commit()
    }

    fun showAthleteListFragment() {
        val athleteListFragment = AthleteListFragment()

        supportFragmentManager
                .beginTransaction()
                .addToBackStack("athleteList")
                .replace(R.id.fragment_container,
                        athleteListFragment, "athleteList").commit()
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
    }

    fun setToolbarTitle(title: String) {
        supportActionBar!!.setTitle(title)
    }

    /** Shows the back button on the toolbar_main */
    fun showBackButton() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /** Shows the back button on the toolbar_main */
    fun hideBackButton() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    /** pops the fragment back stack on back button pressed. */
    override fun onBackPressed() {

        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
    }



}