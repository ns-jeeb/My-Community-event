package com.jeeb.mycommunityevents.invitation.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeb.mycommunityevents.KeepRequiredData;
import com.jeeb.mycommunityevents.R;
import com.jeeb.mycommunityevents.databinding.CommonInvitationBinding;
import com.jeeb.mycommunityevents.invitation.Invitation;

import java.util.Calendar;

public class MeemonyFragment extends Fragment implements View.OnClickListener {

    private CommonInvitationBinding mBinding;
    private Invitation inv;
    private String mChosenDate = "";
    private String mChosenTime = "";
    private Invitation.HostName hostName = new Invitation.HostName();
    private CreateInvitationFragment.OnFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inv = new Invitation();
        mBinding = DataBindingUtil.inflate(inflater, R.layout.common_invitation,container,false);

        return mBinding.getRoot();
    }

    private boolean memonyFieldsAreNotEmpty() {

        if (mBinding.edTitleOfInvitation.getText().toString().isEmpty()) {
            mBinding.edTitleOfInvitation.setError("Name of Location field can't be empty");
            return false;
        } else if (mBinding.edHostName.getText().toString().isEmpty()) {
            mBinding.edHostName.setError("Name field can't be empty");
            return false;
        }else if (mBinding.edLocationName.getText().toString().isEmpty()) {
            mBinding.edLocationName.setError("Name field can't be empty");
            return false;
        }else if (mBinding.edLocationAddress.getText().toString().isEmpty()) {
            mBinding.edLocationAddress.setError("Name field can't be empty");
            return false;
        }else if (mBinding.edLocationCity.getText().toString().isEmpty()) {
            mBinding.edLocationCity.setError("Name field can't be empty");
            return false;
        }
        return true;
    }

    public char onMemoniClicked(boolean isChecked) {
        char invitationType = 0;
        if (isChecked){
            invitationType = 'm';
        }
        else {

        }
        return invitationType;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateInvitationFragment.OnFragmentInteractionListener) {

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public Invitation createMemoniCard() {

        if (memonyFieldsAreNotEmpty()) {

            inv.setNamePlace(mBinding.edLocationName.getText().toString());
            inv.setAddress(mBinding.edLocationAddress.getText().toString());
            inv.setCityPostalProvince(mBinding.edLocationCity.getText().toString());
            inv.setTitle(mBinding.edTitleOfInvitation.getText().toString());
            inv.setDateTime(mChosenDate+" @"+mChosenTime);
            inv.setIsPartOfFamily("n");
            inv.setIsVisiting("n");
            hostName.setFName(KeepRequiredData.getInstance().getCurrentUser().getFName());
            hostName.setLName(KeepRequiredData.getInstance().getCurrentUser().getLName());
            hostName.setEmail(KeepRequiredData.getInstance().getCurrentUser().getEmail());
            hostName.setId(KeepRequiredData.getInstance().getCurrentUser().getId());
            inv.setHostName(hostName);

        } else {
            mBinding.btnDate.setError("You have to chose a date");

        }
        return inv;
    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.btnDate && getActivity() != null) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

//            DatePickerDialog dialog = new DatePickerDialog(getActivity(), mOnDateSetListener, year,month,day);
//            dialog.getDatePicker();
//            dialog.show();
        }
    }
}
