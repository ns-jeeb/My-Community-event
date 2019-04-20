package com.jeeb.mycommunity.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppUtil {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void hideSoftKeyBoard(Activity activity){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isAcceptingText()&&activity.getCurrentFocus()!= null){
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Context ctxt, final View progressView, final View layoutView) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = ctxt.getResources().getInteger(android.R.integer.config_shortAnimTime);

            layoutView.setVisibility(show ? View.GONE : View.VISIBLE);
            progressView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    layoutView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            layoutView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static String setTime(){
        final String[] time = {""};
        TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                String status = "AM";

                if (hourOfDay > 11) {
                    // If the hour is greater than or equal to 12
                    // Then the current AM PM status is PM
                    status = "PM";
                }
                // Initialize a new variable to hold 12 hour format hour value
                int hour_of_12_hour_format;

                if (hourOfDay > 11) {
                    // If the hour is greater than or equal to 12
                    // Then we subtract 12 from the hour to make it 12 hour format time
                    hour_of_12_hour_format = hourOfDay - 12;
                } else {
                    hour_of_12_hour_format = hourOfDay;
                }
                time[0] = hour_of_12_hour_format + " : " + minute + " : " + status;
//            mBinding.weddingCardInclude.txtTime.setText(mChosenTime);
//            mBinding.weddingCardInclude.edTime.setText(mChosenTime);
            }
        };
        return time[0];
    }
    public TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            String status = "AM";

            if (hourOfDay > 11) {
                // If the hour is greater than or equal to 12
                // Then the current AM PM status is PM
                status = "PM";
            }
            // Initialize a new variable to hold 12 hour format hour value
            int hour_of_12_hour_format;

            if (hourOfDay > 11) {
                // If the hour is greater than or equal to 12
                // Then we subtract 12 from the hour to make it 12 hour format time
                hour_of_12_hour_format = hourOfDay - 12;
            } else {
                hour_of_12_hour_format = hourOfDay;
            }
            time = hour_of_12_hour_format + " : " + minute + " : " + status;
//            mBinding.weddingCardInclude.txtTime.setText(mChosenTime);
//            mBinding.weddingCardInclude.edTime.setText(mChosenTime);
        }
    };

    public DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {


        @Override
        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//            Toast.makeText(getContext(), "Date and Time " + i + ":" + i1 + ":" + i2, Toast.LENGTH_LONG).show();
            Calendar c = Calendar.getInstance();
            Calendar c1 = Calendar.getInstance();
            c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));
            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
            date = df.format(c.getTime());
//            mBinding.weddingCardInclude.edDate.setText(mChosenDate);

        }
    };
    private String date;
    private String time;
    public String setDateAndTime(){
        return date +" @ "+time;
    }
}
