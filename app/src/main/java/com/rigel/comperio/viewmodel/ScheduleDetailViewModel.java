package com.rigel.comperio.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.rigel.comperio.Navigator;
import com.rigel.comperio.model.Schedule;

public class ScheduleDetailViewModel extends BaseObservable {
    private Schedule schedule;
    private Navigator navigator;

    public ScheduleDetailViewModel(Schedule schedule, Navigator navigator) {
        this.schedule = schedule;
        this.navigator = navigator;
    }

    @BindingAdapter("imageUrl") public static void setImageUrl(ImageView imageView, String url) {
        // TODO: add picasso loading
    }

    public void onLetsTalkClick(View view) {
        // TODO: lauch intent to add teacher to contacts
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        notifyChange();
    }

    public String getTeacherName() {
        return schedule.teacherName;
    }

    public String getTeacherPhone() {
        return schedule.teacherPhone.toString();
    }

    public String getTeacherPicUrl() {
        return schedule.teacherPicUrl;
    }

    public String getTeacherRating(){return schedule.teacherRating.toString();}

    public String getHourPrice(){
        return schedule.hourPrice.toString();
    }

    public String getDistance(){
        return "3.5km";
    }



}
