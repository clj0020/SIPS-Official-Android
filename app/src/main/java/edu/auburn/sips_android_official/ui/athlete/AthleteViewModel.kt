package edu.auburn.sips_android_official.ui.athlete

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField
import android.support.annotation.NonNull;
import android.util.Log
import edu.auburn.sips_android_official.data.repository.DataRepository
import edu.auburn.sips_android_official.data.room.entity.AthleteEntity
import edu.auburn.sips_android_official.data.room.entity.TestDataEntity
import edu.auburn.sips_android_official.di.SIPSApplication


/**
 * Created by clj00 on 2/19/2018.
 */
class AthleteViewModel(@NonNull application: Application, repository: DataRepository,
                       private val mAthleteId: Int) : AndroidViewModel(application) {

    val observableAthlete: LiveData<AthleteEntity>

    var athlete: ObservableField<AthleteEntity> = ObservableField()

    /**
     * Expose the LiveData TestData query so the UI can observe it.
     */
    val testData: LiveData<List<TestDataEntity>>

    init {

        testData = repository.loadTestData(mAthleteId)

        observableAthlete = repository.loadAthlete(mAthleteId)

    }

    fun setAthlete(athlete: AthleteEntity) {
        this.athlete.set(athlete)
    }

    /**
     * A creator is used to inject the athlete ID into the ViewModel
     *
     *
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the athlete ID can be passed in a public method.
     */
    class Factory(@param:NonNull @field:NonNull
                  private val mApplication: Application, private val mAthleteId: Int) : ViewModelProvider.NewInstanceFactory() {

        private val mRepository: DataRepository

        init {
            mRepository = (mApplication as SIPSApplication).getRepository()!!
        }

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return AthleteViewModel(mApplication, mRepository, mAthleteId) as T
        }
    }
}