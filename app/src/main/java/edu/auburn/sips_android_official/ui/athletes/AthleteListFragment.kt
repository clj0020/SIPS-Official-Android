package edu.auburn.sips_android_official.ui.athletes

/**
 * Created by clj00 on 2/19/2018.
 */
import android.app.Application
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.auburn.sips_android_official.R
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.Nullable
import edu.auburn.sips_android_official.data.models.Athlete
import edu.auburn.sips_android_official.data.room.entity.AthleteEntity
import edu.auburn.sips_android_official.databinding.AthleteListFragmentBinding
import edu.auburn.sips_android_official.di.SIPSApplication
import edu.auburn.sips_android_official.di.ViewModelFactory
import edu.auburn.sips_android_official.ui.MainActivity
import edu.auburn.sips_android_official.ui.adapters.AthleteAdapter
import edu.auburn.sips_android_official.ui.adapters.AthleteClickCallback


class AthleteListFragment : Fragment() {

    private var mAthleteAdapter: AthleteAdapter? = null
    private var mBinding: AthleteListFragmentBinding? = null

    private val mAthleteClickCallback = object : AthleteClickCallback {

        override fun onClick(athlete: Athlete) {

            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                (activity as MainActivity).show(athlete)
            }
        }
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?,
                              @Nullable savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.athlete_list_fragment, container, false)

        mAthleteAdapter = AthleteAdapter(mAthleteClickCallback)
        mBinding!!.athletesList.setAdapter(mAthleteAdapter)

        return mBinding!!.getRoot()
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val viewModel = ViewModelProviders.of(this).get(AthleteListViewModel::class.java)

        subscribeUi(viewModel)
    }

    private fun subscribeUi(viewModel: AthleteListViewModel) {
        // Update the list when the data changes
        viewModel.athletes.observe(this, Observer<List<AthleteEntity>> {
            myAthletes ->
                if (myAthletes != null) {
                    mBinding!!.setIsLoading(false)
                    mAthleteAdapter!!.setAthleteList(myAthletes)
                } else {
                    mBinding!!.setIsLoading(true)
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding!!.executePendingBindings()
        })
    }

    companion object {
        val TAG = "AthleteListViewModel"
    }
}
