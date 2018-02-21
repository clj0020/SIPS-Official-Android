package edu.auburn.sips_android_official.ui.athlete_testing


import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.auburn.sips_android_official.BR

import edu.auburn.sips_android_official.data.room.entity.AthleteEntity
import edu.auburn.sips_android_official.databinding.AthleteTestFragmentBinding
import edu.auburn.sips_android_official.ui.MainActivity
import edu.auburn.sips_android_official.ui.adapters.TestDataAdapter
import edu.auburn.sips_android_official.ui.athlete.AthleteFragment
import edu.auburn.sips_android_official.ui.athlete.AthleteViewModel
import edu.auburn.sips_android_official.util.RxCountDownTimer
import kotlinx.android.synthetic.main.athlete_fragment.view.*
import kotlinx.android.synthetic.main.athlete_test_fragment.view.*
import android.widget.TextView
import edu.auburn.sips_android_official.R
import kotlinx.android.synthetic.main.athlete_test_fragment.*





/**
 * A simple [Fragment] subclass.
 */
class AthleteTestFragment : Fragment() {

    private var mBinding: AthleteTestFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.athlete_test_fragment, container, false)

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

        mBinding!!.getRoot().start_athlete_test_button.setOnClickListener({
            mBinding!!.setIsClockStarted(true)
            model.startTimer()
        })

        mBinding!!.getRoot().stop_athlete_test_button.setOnClickListener({
            mBinding!!.setIsClockStarted(false)
            model.stopTimer()
        })

        val timeObserver = Observer<String> {formattedTime ->
            if (formattedTime != null) {
                time_text.setText(formattedTime)
            }
        }


        model.formattedTime.observe(this, timeObserver)

    }

    companion object {

        private val KEY_ATHLETE_ID = "athlete_id"

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
