package com.jeeb.mycommunityevents.invitation.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.jeeb.mycommunityevents.R;
import com.jeeb.mycommunityevents.KeepRequiredData;
import com.jeeb.mycommunityevents.databinding.WeddingCardBinding;
import com.jeeb.mycommunityevents.invitation.Invitation;
import com.jeeb.mycommunityevents.invitation.activities.CreateInvitationActivity;
import com.jeeb.mycommunityevents.utils.AppUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateInvitationFragment extends Fragment implements View.OnClickListener,
        View.OnTouchListener {

    private static final String ARG_PARAM1 = "param1";
    private WeddingCardBinding mBinding;
    private String mChosenDate = "";
    private String mChosenTime = "";
    private Invitation inv;
    private CreateInvitationFragment.OnFragmentInteractionListener mListener;
    private Invitation.HostName hostName = new Invitation.HostName();
    private char invitationType = 'f';

    public static CreateInvitationFragment newInstance(int sectionNumber) {
        CreateInvitationFragment fragment = new CreateInvitationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

//    @Override
//    public boolean onBackPressed() {
//        super.onBackPressed();
//        return false;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.wedding_card, container, false);
        inv = new Invitation();

        mBinding.btnNext.setOnClickListener(this);
//        mBinding.commonInviCardInclude.btnDate.setOnClickListener(this);
//        mBinding.commonInviCardInclude.btnTime.setOnClickListener(this);
        mBinding.edTime.setOnClickListener(this);
        mBinding.edDate.setOnClickListener(this);
        mBinding.getRoot().setOnTouchListener(this);

        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {

        if (view == mBinding.edDate && getActivity() != null) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                @Override
                @SuppressLint("SimpleDateFormat")
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//            Toast.makeText(getContext(), "Date and Time " + i + ":" + i1 + ":" + i2, Toast.LENGTH_LONG).show();
                    Calendar c = Calendar.getInstance();
                    Calendar c1 = Calendar.getInstance();
                    c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));
                    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                    mChosenDate = df.format(c.getTime());
            mBinding.edDate.setText(mChosenDate);

                }
            }, year, month, day);
            dialog.show();
        }
        if (view == mBinding.edTime) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

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
                        mChosenTime = hour_of_12_hour_format + " : " + minute + " : " + status;
                        mBinding.edTime.setText(mChosenTime);
                    }

            }, 0, 0, false);
            timePickerDialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

//    TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
//        @Override
//        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
//            String status = "AM";
//
//            if (hourOfDay > 11) {
//                // If the hour is greater than or equal to 12
//                // Then the current AM PM status is PM
//                status = "PM";
//            }
//            // Initialize a new variable to hold 12 hour format hour value
//            int hour_of_12_hour_format;
//
//            if (hourOfDay > 11) {
//                // If the hour is greater than or equal to 12
//                // Then we subtract 12 from the hour to make it 12 hour format time
//                hour_of_12_hour_format = hourOfDay - 12;
//            } else {
//                hour_of_12_hour_format = hourOfDay;
//            }
//            mChosenTime = hour_of_12_hour_format + " : " + minute + " : " + status;
//            mBinding.weddingCardInclude.txtTime.setText(mChosenTime);
//            mBinding.weddingCardInclude.edTime.setText(mChosenTime);
//        }
//    };
//
//    DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
//
//
//        @Override
//        @SuppressLint("SimpleDateFormat")
//        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//            Toast.makeText(getContext(), "Date and Time " + i + ":" + i1 + ":" + i2, Toast.LENGTH_LONG).show();
//            Calendar c = Calendar.getInstance();
//            Calendar c1 = Calendar.getInstance();
//            c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));
//            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
//            mChosenDate = df.format(c.getTime());
//            mBinding.weddingCardInclude.edDate.setText(mChosenDate);
//
//        }
//    };


    private boolean fieldsAreNotEmpty() {

        if (mBinding.edNameOfPlace.getText().toString().isEmpty()) {
            mBinding.edNameOfPlace.setError("Name of Location field can't be empty");
            return false;
        } else if (mBinding.edGroomFirstName.getText().toString().isEmpty()) {
            mBinding.edGroomFirstName.setError("Name field can't be empty");
            return false;
        }else if (mBinding.edBrideFirstName.getText().toString().isEmpty()) {
            mBinding.edBrideFirstName.setError("Name field can't be empty");
            return false;
        }else if (mBinding.edGroomLastName.getText().toString().isEmpty()) {
            mBinding.edGroomLastName.setError("Name field can't be empty");
            return false;
        }else if (mBinding.edBrideLastName.getText().toString().isEmpty()) {
            mBinding.edBrideLastName.setError("Name field can't be empty");
            return false;
        }else if (mBinding.edAddress.getText().toString().isEmpty()) {
            mBinding.edAddress.setError("Address field can't be empty");
            return false;
        }
        return true;
    }
//    private boolean memonyFieldsAreNotEmpty() {
//
//        if (mBinding.commonInviCardInclude.edTitleOfInvitation.getText().toString().isEmpty()) {
//            mBinding.commonInviCardInclude.edTitleOfInvitation.setError("Name of Location field can't be empty");
//            return false;
//        } else if (mBinding.commonInviCardInclude.edHostName.getText().toString().isEmpty()) {
//            mBinding.commonInviCardInclude.edHostName.setError("Name field can't be empty");
//            return false;
//        }else if (mBinding.commonInviCardInclude.edLocationName.getText().toString().isEmpty()) {
//            mBinding.commonInviCardInclude.edLocationName.setError("Name field can't be empty");
//            return false;
//        }else if (mBinding.commonInviCardInclude.edLocationAddress.getText().toString().isEmpty()) {
//            mBinding.commonInviCardInclude.edLocationAddress.setError("Name field can't be empty");
//            return false;
//        }else if (mBinding.commonInviCardInclude.edLocationCity.getText().toString().isEmpty()) {
//            mBinding.commonInviCardInclude.edLocationCity.setError("Name field can't be empty");
//            return false;
//        }
////        else if (mBinding.commonInviCardInclude.btnDate.getText().toString().isEmpty()) {
////            mBinding.commonInviCardInclude.edAddress.setError("Address field can't be empty");
////            return false;
////        }
//        return true;
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (getActivity() != null) {
            AppUtil.hideSoftKeyBoard(getActivity());
        }

        return false;
    }

    public char onWeddingClicked(boolean isChecked) {
        if (isChecked){
            inv = createWeddingCard();
            invitationType = 'w';
        }
        return invitationType;
    }

    public void onFotiaClicked(boolean isChecked) {

    }

    public void onMeetUpClicked(boolean isChecked) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String tag,Object data);
    }

    public Invitation createWeddingCard() {

        if (inv != null && fieldsAreNotEmpty()) {

            inv.setBrideName(mBinding.edBrideFirstName.getText().toString());
            inv.setBrideLName(mBinding.edBrideLastName.getText().toString());
            inv.setGroomName(mBinding.edGroomFirstName.getText().toString());
            inv.setGroomLName(mBinding.edGroomLastName.getText().toString());

            inv.setDateTime(mChosenDate +" "+mChosenTime);
            inv.setNamePlace(mBinding.edNameOfPlace.getText().toString());
            inv.setAddress(mBinding.edAddress.getText().toString());
            inv.setCityPostalProvince(mBinding.edCityOfPostalProvince.getText().toString());
            inv.setTitle(mBinding.edTitleBrideGroom.getText().toString());
            inv.setIsPartOfFamily("n");
            inv.setIsVisiting("n");
            hostName.setFName(KeepRequiredData.getInstance().getCurrentUser().getFName());
            hostName.setLName(KeepRequiredData.getInstance().getCurrentUser().getLName());
            hostName.setEmail(KeepRequiredData.getInstance().getCurrentUser().getEmail());
            hostName.setId(KeepRequiredData.getInstance().getCurrentUser().getId());
            inv.setHostName(hostName);

//            ((CreateInvitationActivity) getActivity()).sendInvitation(true, mBinding.getRoot());
        } else {
//            mBinding.commonInviCardInclude.btnDate.setError("You have chose a date");

        }
        return inv;
    }

//    public Invitation createMemoniCard() {
//
//        if (memonyFieldsAreNotEmpty()) {
//
//            inv.setNamePlace(mBinding.commonInviCardInclude.edLocationName.getText().toString());
//            inv.setAddress(mBinding.commonInviCardInclude.edLocationAddress.getText().toString());
//            inv.setCityPostalProvince(mBinding.commonInviCardInclude.edLocationCity.getText().toString());
//            inv.setTitle(mBinding.commonInviCardInclude.edTitleOfInvitation.getText().toString());
//            inv.setDateTime(mChosenDate+" @"+mChosenTime);
//            inv.setIsPartOfFamily("n");
//            inv.setIsVisiting("n");
//            hostName.setFName(KeepRequiredData.getInstance().getCurrentUser().getFName());
//            hostName.setLName(KeepRequiredData.getInstance().getCurrentUser().getLName());
//            hostName.setEmail(KeepRequiredData.getInstance().getCurrentUser().getEmail());
//            hostName.setId(KeepRequiredData.getInstance().getCurrentUser().getId());
//            inv.setHostName(hostName);
//
//        } else {
//            mBinding.commonInviCardInclude.btnDate.setError("You have to chose a date");
//
//        }
//        return inv;
//    }


}
