package edu.auburn.sips_android_official.ui.add_athlete

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.NonNull
import edu.auburn.sips_android_official.data.repository.DataRepository
import edu.auburn.sips_android_official.data.room.DatabaseCallback
import edu.auburn.sips_android_official.di.SIPSApplication
import edu.auburn.sips_android_official.ui.athlete.AthleteViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Created by clj00 on 2/28/2018.
 */
class AddAthleteViewModel(@NonNull application: Application, repository: DataRepository) : AndroidViewModel(application) {

    private val mRepository: DataRepository

    init {
        mRepository = repository
    }


    fun submitAthlete(databaseCallback: DatabaseCallback, firstName: String, lastName: String, email: String) {
        mRepository.addAthlete(
                databaseCallback = databaseCallback,
                firstName = firstName,
                lastName = lastName,
                email = email
        )
    }

    /**
     * A creator is used to inject the athlete ID into the ViewModel
     *
     *
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the athlete ID can be passed in a public method.
     */
    class Factory(@param:NonNull @field:NonNull
                  private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {

        private val mRepository: DataRepository

        init {
            mRepository = (mApplication as SIPSApplication).getRepository()!!
        }

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return AddAthleteViewModel(mApplication, mRepository) as T
        }
    }

}