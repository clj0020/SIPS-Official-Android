package edu.auburn.sips_android_official.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import edu.auburn.sips_android_official.ui.athletes.AthleteListViewModel;

/**
 * Created by clj00 on 2/19/2018.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {
    private AthleteListViewModel mAthleteListViewModel;

    @Inject
    public ViewModelFactory(AthleteListViewModel athleteListViewModel) {
        this.mAthleteListViewModel = athleteListViewModel;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AthleteListViewModel.class)) {
            return (T) mAthleteListViewModel;
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}