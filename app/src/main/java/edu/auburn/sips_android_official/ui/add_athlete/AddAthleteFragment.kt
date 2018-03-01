package edu.auburn.sips_android_official.ui.add_athlete


import android.app.FragmentManager
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*

import edu.auburn.sips_android_official.R
import edu.auburn.sips_android_official.data.room.DatabaseCallback
import edu.auburn.sips_android_official.databinding.AddAthleteFragmentBinding
import edu.auburn.sips_android_official.ui.MainActivity
import kotlinx.android.synthetic.main.add_athlete_fragment.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import edu.auburn.sips_android_official.ui.athlete.AthleteFragment


/**
 * A simple [Fragment] subclass.
 */
class AddAthleteFragment : Fragment(), DatabaseCallback {

    private var mBinding: AddAthleteFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.add_athlete_fragment, container, false)

        setHasOptionsMenu(true)

        return mBinding!!.getRoot()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = AddAthleteViewModel.Factory(
                getActivity()!!.getApplication())


        val model = ViewModelProviders.of(this, factory)
                .get(AddAthleteViewModel::class.java)

        mBinding!!.setAddAthleteViewModel(model)


        Log.i("AthleteFragment: ", "onActivityCreatedCalled")
        subscribeToModel(model)
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


    private fun subscribeToModel(model: AddAthleteViewModel) {

        (activity as MainActivity).setToolbarTitle("Add Athlete")

        mBinding!!.root.add_athlete_submit.setOnClickListener({

            model.submitAthlete(
                    databaseCallback = this,
                    firstName = mBinding!!.root.input_first_name.text.toString(),
                    lastName = mBinding!!.root.input_last_name.text.toString(),
                    email = mBinding!!.root.input_email.text.toString()
            )

        })

        mBinding!!.root.input_email.setOnEditorActionListener(object : OnEditorActionListener {

            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mBinding!!.root.add_athlete_submit.performClick()
                    handled = true
                }
                return handled
            }
        })



    }

    override fun onAthleteAdded(athleteId: Int) {
        Log.i("AddAthleteFragment: ", "Athlete added to database " + athleteId)


        (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,
                        AthleteFragment.forAthlete(athleteId), "athlete").commit()
    }


    companion object {

        /** Creates an add athlete fragment  */
        fun createAddAthleteFragment(): AddAthleteFragment {
            val fragment = AddAthleteFragment()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }


}// Required empty public constructor
