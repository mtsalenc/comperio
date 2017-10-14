package com.rigel.comperio.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrence;
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment;
import com.rigel.comperio.R;
import com.rigel.comperio.viewmodel.FiltersViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FiltersFragment extends BaseFragment {

    private static final String START_TIME_PICKER = "startTimePicker";
    private static final String END_TIME_PICKER = "endTimePicker";
    private static final String RECURRENCE_PICKER = "RECURRENCEPicker";
    private static final String LOG_TAG = FiltersFragment.class.getSimpleName();
    @BindView(R.id.btnSelectDaysOfTheWeek) Button btnSelectRecurrence;
    @BindView(R.id.txtStarTime) TextView txtStartTime;
    @BindView(R.id.txtEndTime) TextView txtEndTime;

    private String rRule;

    private EventRecurrence eventRecurrence = new EventRecurrence();

    public FiltersFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);

        ButterKnife.bind(this, view);
        setupClickListeners();
        return view;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_filters;
    }


    private void setupClickListeners() {
        btnSelectRecurrence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recurrenceSelect();
            }
        });

        txtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimeSelect();
            }
        });

        txtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTimeSelect();
            }
        });
    }

    public void startTimeSelect(){
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
                        ((FiltersViewModel)viewModel).setStartTime(hourOfDay,minute);
                    }
                })
                .setStartTime(10, 10)
                .setDoneText(getString(R.string.OK))
                .setCancelText(getString(R.string.Cancel))
                .setThemeDark();

        rtpd.show(getFragmentManager(), START_TIME_PICKER);
    }

    public void endTimeSelect(){
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
                        ((FiltersViewModel)viewModel).setEndTime(hourOfDay,minute);
                    }
                })
                .setStartTime(10, 10)
                .setDoneText(getString(R.string.OK))
                .setCancelText(getString(R.string.Cancel))
                .setThemeDark();

        rtpd.show(getFragmentManager(), END_TIME_PICKER);
    }

    public void recurrenceSelect(){
        FragmentManager fm = getFragmentManager();
        Bundle bundle = new Bundle();
        Time time = new Time();
        time.setToNow();
        bundle.putLong(RecurrencePickerDialogFragment.BUNDLE_START_TIME_MILLIS, time.toMillis(false));
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_TIME_ZONE, time.timezone);
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_RRULE, rRule);
        bundle.putBoolean(RecurrencePickerDialogFragment.BUNDLE_HIDE_SWITCH_BUTTON, true);

        RecurrencePickerDialogFragment rpd = new RecurrencePickerDialogFragment();
        rpd.setArguments(bundle);
        rpd.setOnRecurrenceSetListener(new RecurrencePickerDialogFragment.OnRecurrenceSetListener() {
            @Override
            public void onRecurrenceSet(String rRule) {
                FiltersFragment.this.rRule = rRule;
                if (FiltersFragment.this.rRule == null) {
                    Log.w(LOG_TAG, "rRule is null.");
                    return;
                }

                eventRecurrence.parse(FiltersFragment.this.rRule);
                ((FiltersViewModel) viewModel).setRecurrence(eventRecurrence);
            }
        });
        rpd.show(fm, RECURRENCE_PICKER);
    }

}
