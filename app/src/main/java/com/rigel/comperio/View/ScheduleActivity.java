package com.rigel.comperio.View;

import android.support.annotation.NonNull;

import com.manaschaudhari.android_mvvm.MvvmActivity;
import com.manaschaudhari.android_mvvm.ViewModel;
import com.rigel.comperio.R;
import com.rigel.comperio.ViewModel.ScheduleViewModel;

public class ScheduleActivity extends BaseActivity {

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ScheduleViewModel(getNavigator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_schedule;
    }
}
