package edu.auburn.sips_android_official.ui.athletes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.Observer
import edu.auburn.sips_android_official.data.room.entity.AthleteEntity
import edu.auburn.sips_android_official.di.SIPSApplication


/**
 * Created by clj00 on 2/19/2018.
 */
class AthleteListViewModel(application: Application) : AndroidViewModel(application) {


    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    public val mObservableAthletes: MediatorLiveData<List<AthleteEntity>>

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    val athletes: LiveData<List<AthleteEntity>>
        get() = mObservableAthletes



    init {
        mObservableAthletes = MediatorLiveData<List<AthleteEntity>>()
        // set by default null, until we get data from the database.
        mObservableAthletes.value = null

        val athletes = (application as SIPSApplication).getRepository()!!.athletes

        // observe the changes of the athletes from the database and forward them
        mObservableAthletes.addSource<List<AthleteEntity>>(athletes, Observer<List<AthleteEntity>> { mObservableAthletes.setValue(it) })
    }
}