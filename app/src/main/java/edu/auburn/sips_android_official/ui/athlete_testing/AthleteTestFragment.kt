package edu.auburn.sips_android_official.ui.athlete_testing


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*

import edu.auburn.sips_android_official.databinding.AthleteTestFragmentBinding
import kotlinx.android.synthetic.main.athlete_test_fragment.view.*
import edu.auburn.sips_android_official.R
import edu.auburn.sips_android_official.ui.MainActivity
import kotlinx.android.synthetic.main.athlete_test_fragment.*





/**
 * The fragment for performing athlete tests.
 */
class AthleteTestFragment : Fragment() {

    private var mBinding: AthleteTestFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.athlete_test_fragment, container, false)

        setHasOptionsMenu(true)

        return mBinding!!.getRoot()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.i("AthleteTestFragment: ", "onActivityCreatedCalled")

        val factory = AthleteTestViewModel.Factory(
                getActivity()!!.getApplication(), getArguments()!!.getInt(AthleteTestFragment.KEY_ATHLETE_ID))

        val model = ViewModelProviders.of(this, factory)
                .get(AthleteTestViewModel::class.java)

        mBinding!!.setAthleteTestViewModel(model)

        subscribeUI(model)
    }

    private fun subscribeUI(model: AthleteTestViewModel) {

        (activity as MainActivity).setToolbarTitle("Choose a test")

        mBinding!!.root.one_leg_squat_hold_button.setOnClickListener({
            mBinding!!.setIsTestChosen(true)
            mBinding!!.root.test_title_text.setText("One Leg Squat Hold")
            model.setTest(ONE_LEG_SQUAT_HOLD)
            (activity as MainActivity).setToolbarTitle("One Leg Squat Hold")
        })

        mBinding!!.root.single_leg_jump_button.setOnClickListener({
            mBinding!!.setIsTestChosen(true)
            mBinding!!.root.test_title_text.setText("Single Leg Jump")
            model.setTest(SINGLE_LEG_JUMP)
            (activity as MainActivity).setToolbarTitle("Single Leg Jump")
        })

        mBinding!!.root.start_athlete_test_button.setOnClickListener({
            mBinding!!.setIsClockStarted(true)
            model.startTimer()
            (activity as MainActivity).setToolbarTitle("Perform Exercise..")
        })

        mBinding!!.root.stop_athlete_test_button.setOnClickListener({
            mBinding!!.setIsClockStarted(false)
            model.stopTimer()
            (activity as MainActivity).setToolbarTitle("Exercise Finished..")
        })

        val timeObserver = Observer<String> {formattedTime ->
            if (formattedTime != null) {
                time_text.setText(formattedTime)
            }
        }

        model.formattedTime.observe(this, timeObserver)

        val testObserver = Observer<Int>() { test ->
            model.setTest(test!!)
        }
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity?
        activity?.showBackButton()
    }

    /** For handling toolbar_main actions  */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val mActivity = activity as MainActivity
        when (item.itemId) {
            android.R.id.home -> {
                mActivity.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** Simply inflates menu resource file to the toolbar_main  */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_detail, menu)
    }

    companion object {

        private val KEY_ATHLETE_ID = "athlete_id"
        private val ONE_LEG_SQUAT_HOLD = 0
        private val SINGLE_LEG_JUMP = 1

        /** Creates athlete fragment for specific athlete ID  */
        fun forAthlete(athleteId: Int): AthleteTestFragment {
            Log.i("AthleteTestFragment: ", athleteId.toString())
            val fragment = AthleteTestFragment()
            val args = Bundle()
            args.putInt(KEY_ATHLETE_ID, athleteId)
            fragment.setArguments(args)
            return fragment
        }
    }

}// Required empty public constructor
