package edu.auburn.sips_android_official.ui.athletes

/**
 * Created by clj00 on 2/19/2018.
 */
import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v4.app.Fragment
import edu.auburn.sips_android_official.R
import android.databinding.DataBindingUtil
import android.support.annotation.Nullable
import android.support.v7.widget.SearchView
import edu.auburn.sips_android_official.data.models.Athlete
import edu.auburn.sips_android_official.data.room.entity.AthleteEntity
import edu.auburn.sips_android_official.databinding.AthleteListFragmentBinding
import edu.auburn.sips_android_official.ui.MainActivity
import edu.auburn.sips_android_official.ui.adapters.AthleteAdapter
import edu.auburn.sips_android_official.ui.adapters.AthleteClickCallback
import android.util.Log
import android.view.*


class AthleteListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var mAthleteAdapter: AthleteAdapter? = null
    private var mBinding: AthleteListFragmentBinding? = null
    private var mAthletes: List<Athlete> = arrayListOf()

    private val mAthleteClickCallback = object : AthleteClickCallback {

        override fun onClick(athlete: Athlete) {

            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                (activity as MainActivity).showAthleteFragment(athlete.id)
            }
        }
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?,
                              @Nullable savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.athlete_list_fragment, container, false)

        mAthleteAdapter = AthleteAdapter(mAthleteClickCallback)
        mBinding!!.athletesList.setAdapter(mAthleteAdapter)

        setHasOptionsMenu(true)

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
                mAthletes = myAthletes
                (activity as MainActivity).setToolbarTitle("Auburn University") // TODO: Set to organization name
            } else {
                mBinding!!.setIsLoading(true)
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            // sync.
            mBinding!!.executePendingBindings()
        })
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity?
        activity?.hideBackButton()
    }

    /** For handling toolbar_main actions  */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val mActivity = activity as MainActivity
        when (item.itemId) {
            R.id.add_athlete_button -> {
                mActivity.showAddAthleteFragment()
                return true
            }
            android.R.id.home -> {
                mActivity.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_main, menu)

        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu.findItem(R.id.search_athletes_button)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        // Define the listener
        val searchViewExpandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // Do something when action item collapses

                return true // Return true to collapse action view
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // Do something when expanded
                Log.i("AthleteList: ", "onMenuActionExpand")
                mAthleteAdapter?.setAthleteList(mAthletes)
                return true // Return true to expand action view

            }
        }

        // Assign the searchview expansion listener to search item
        searchItem.setOnActionExpandListener(searchViewExpandListener)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        val filteredModelList = filter(mAthletes, newText)
        mAthleteAdapter?.setSearchResult(filteredModelList)
        return true
    }

    private fun filter(models: List<Athlete>, query: String): List<Athlete> {
        val mQuery = query.toLowerCase()
        val filteredModelList = ArrayList<Athlete>()
        for (model in models) {
            val text = model.firstName.toLowerCase() + " " + model.lastName.toLowerCase()
            if (text.contains(mQuery)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }


    companion object {
        val TAG = "AthleteListViewModel"
    }
}
