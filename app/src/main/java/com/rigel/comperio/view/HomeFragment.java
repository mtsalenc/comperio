package com.rigel.comperio.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rigel.comperio.adapters.ScheduleAdapter;
import com.rigel.comperio.databinding.FragmentHomeBinding;
import com.rigel.comperio.viewmodel.HomeViewModel;

import java.util.Observable;
import java.util.Observer;

public class HomeFragment extends BaseFragment implements Observer {

    public static final String TAG = "HomeFragmentTag";
    private static int lastVisiblePosition;

    FragmentHomeBinding fragmentHomeBinding;
    HomeViewModel homeViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentHomeBinding.recyclerHome.setLayoutManager(
                new LinearLayoutManager(getActivity()));
        fragmentHomeBinding.recyclerHome.setAdapter(
                new ScheduleAdapter(getContext(),navigator, logger, buildOnClickHandler()));

        buildItemTouchHelper().attachToRecyclerView(fragmentHomeBinding.recyclerHome);


        homeViewModel = new HomeViewModel(navigator, persistenceManager, logger,
                getLoaderManager(), getContext());
        fragmentHomeBinding.setHomeViewModel(homeViewModel);

        homeViewModel.initializeLoader();
    }

    @Override
    public void onStart() {
        homeViewModel.addObserver(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        int pos = ((LinearLayoutManager)fragmentHomeBinding.recyclerHome.getLayoutManager())
                        .findFirstCompletelyVisibleItemPosition();

        if(pos!=-1) {
            lastVisiblePosition = pos;
        }

        homeViewModel.deleteObserver(this);
        super.onStop();
    }

    public HomeFragment() { setArguments(new Bundle()); }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected void updateConnectivityStatus(Boolean isConnectedToInternet) {
        if(homeViewModel!=null) {
            homeViewModel.isConnectedToInternet = isConnectedToInternet;
        }
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (!(observable instanceof HomeViewModel)) {
            return;
        }

        ScheduleAdapter scheduleAdapter =
                (ScheduleAdapter) fragmentHomeBinding.recyclerHome.getAdapter();
        HomeViewModel homeViewModel = (HomeViewModel) observable;
        scheduleAdapter.setScheduleList(homeViewModel.getSchedules());

        if(lastVisiblePosition!=-1 && lastVisiblePosition!=0) {
            fragmentHomeBinding.recyclerHome
                    .getLayoutManager()
                    .scrollToPosition(lastVisiblePosition);
        }

    }

    private ItemTouchHelper buildItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                homeViewModel.addToFavorites(((ScheduleAdapter.ScheduleViewHolder)viewHolder)
                        .getItemScheduleBinding().getItemScheduleViewModel());

                ((ScheduleAdapter)fragmentHomeBinding.recyclerHome.getAdapter())
                        .getScheduleList().remove(viewHolder.getAdapterPosition());

                fragmentHomeBinding.recyclerHome.getAdapter()
                        .notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
    }

}
