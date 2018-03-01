package edu.auburn.sips_android_official.ui.athlete

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v4.app.Fragment
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import edu.auburn.sips_android_official.R
import edu.auburn.sips_android_official.data.models.TestData
import edu.auburn.sips_android_official.data.room.entity.AthleteEntity
import edu.auburn.sips_android_official.data.room.entity.TestDataEntity
import edu.auburn.sips_android_official.databinding.AthleteFragmentBinding
import edu.auburn.sips_android_official.ui.adapters.TestDataAdapter
import edu.auburn.sips_android_official.ui.adapters.TestDataClickCallback
import edu.auburn.sips_android_official.ui.MainActivity
import kotlinx.android.synthetic.main.athlete_fragment.*
import kotlinx.android.synthetic.main.athlete_fragment.view.*


/**
 * Created by clj00 on 2/19/2018.
 */
class AthleteFragment : Fragment() {

    private var mBinding: AthleteFragmentBinding? = null

    private var mTestDataAdapter: TestDataAdapter? = null

    private val mTestDataClickCallback = object : TestDataClickCallback {
        override fun onClick(testData: TestData) {
            // no-op
            Log.i("AthleteFragment: ", "AccelerometerData: x=" + testData.accelerometerArray[0][0] + " y=" + testData.accelerometerArray[0][1] + " z=" + testData.accelerometerArray[0][2])
            Log.i("AthleteFragment: ", "GyroscopeData: x=" + testData.gyroscopeArray[0][0] + " y=" + testData.gyroscopeArray[0][1] + " z=" + testData.gyroscopeArray[0][2])
            Log.i("AthleteFragment: ", "MagnometerData: x=" + testData.magnetometerArray[0][0] + " y=" + testData.magnetometerArray[0][1] + " z=" + testData.magnetometerArray[0][2])
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.athlete_fragment, container, false)

        // Create and set the adapter for the RecyclerView.
        mTestDataAdapter = TestDataAdapter(mTestDataClickCallback)
        mBinding!!.testDataList.setAdapter(mTestDataAdapter)

        return mBinding!!.getRoot()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = AthleteViewModel.Factory(
                getActivity()!!.getApplication(), getArguments()!!.getInt(KEY_ATHLETE_ID))


        val model = ViewModelProviders.of(this, factory)
                .get(AthleteViewModel::class.java)

        mBinding!!.setAthleteViewModel(model)


        Log.i("AthleteFragment: ", "onActivityCreatedCalled")
        subscribeToModel(model)
    }

    private fun subscribeToModel(model: AthleteViewModel) {

        mBinding!!.getRoot().test_athlete_button.setOnClickListener({
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                (activity as MainActivity).showAthleteTestFragment(getArguments()!!.getInt(KEY_ATHLETE_ID))
            }
        })

        // Create the observer which updates the UI.
        val athleteObserver = Observer<AthleteEntity>() { athlete ->
            model.setAthlete(athlete!!)
        }


        // Observe athlete data
        model.observableAthlete.observe(this, athleteObserver)

        // Create the observer which updates the UI.
        val testDataObserver = Observer<List<TestDataEntity>>() { testDataArray ->
            if (testDataArray != null) {
                mBinding!!.setIsLoading(false)
                mTestDataAdapter!!.setTestDataList(testDataArray)
            } else {
                mBinding!!.setIsLoading(true)
            }
        }

        // Observe test data
        model.testData.observe(this, testDataObserver)

    }

    companion object {

        private val KEY_ATHLETE_ID = "athlete_id"

        /** Creates athlete fragment for specific athlete ID  */
        fun forAthlete(athleteId: Int): AthleteFragment {
            Log.i("AthleteFragment: ", athleteId.toString())
            val fragment = AthleteFragment()
            val args = Bundle()
            args.putInt(KEY_ATHLETE_ID, athleteId)
            fragment.setArguments(args)
            return fragment
        }
    }
}