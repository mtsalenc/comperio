package com.rigel.comperio.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rigel.comperio.DevUtils;
import com.rigel.comperio.Navigator;
import com.rigel.comperio.PersistenceManager;
import com.rigel.comperio.adapters.ScheduleAdapter;
import com.rigel.comperio.databinding.FragmentFavoritesBinding;
import com.rigel.comperio.viewmodel.FavoritesViewModel;

import java.util.Observable;
import java.util.Observer;


public class FavoritesFragment extends Fragment implements Observer{

    Navigator navigator;
    DevUtils.Logger logger;
    PersistenceManager persistenceManager;

    FragmentFavoritesBinding fragmentFavoritesBinding;
    FavoritesViewModel favoritesViewModel;

    public FavoritesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentFavoritesBinding = FragmentFavoritesBinding.inflate(inflater,container,false);

        favoritesViewModel = new FavoritesViewModel(navigator, persistenceManager,logger);
        favoritesViewModel.addObserver(this);
        fragmentFavoritesBinding.setFavoritesViewModel(favoritesViewModel);

        fragmentFavoritesBinding.recyclerFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentFavoritesBinding.recyclerFavorites.setAdapter(new ScheduleAdapter(navigator,logger));

        favoritesViewModel.refreshItems();

        return fragmentFavoritesBinding.getRoot();
    }

    @Override
    public void update(Observable observable, Object o) {
        if (!(observable instanceof FavoritesViewModel)) {
            return;
        }

        ScheduleAdapter scheduleAdapter = (ScheduleAdapter) fragmentFavoritesBinding.recyclerFavorites.getAdapter();
        FavoritesViewModel favoritesViewModel = (FavoritesViewModel) observable;
        scheduleAdapter.setScheduleList(favoritesViewModel.getSchedules());

    }

    public static FavoritesFragment newInstance(Navigator navigator, PersistenceManager persistenceManager,
                                           DevUtils.Logger logger) {
        FavoritesFragment favoritesFragment = new FavoritesFragment();

        favoritesFragment.setNavigator(navigator);
        favoritesFragment.setPersistenceManager(persistenceManager);
        favoritesFragment.setLogger(logger);

        return favoritesFragment;
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public void setLogger(DevUtils.Logger logger) {
        this.logger = logger;
    }
}
