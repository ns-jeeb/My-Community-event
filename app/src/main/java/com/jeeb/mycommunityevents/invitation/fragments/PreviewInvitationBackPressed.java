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

import com.jeeb.mycommunityevents.R;
import com.jeeb.mycommunityevents.databinding.CreateInvitationFragmentBinding;
import com.jeeb.mycommunityevents.invitation.Invitation;
import com.jeeb.mycommunityevents.invitation.activities.CreateInvitationActivity;

import java.util.ArrayList;

public class PreviewInvitationBackPressed extends Fragment implements View.OnClickListener {

    private CreateInvitationFragmentBinding mBinding;
    private Invitation inv;
    public static String ARG_PARAM= "ARG_PARAM";
    CreateInvitationFragment.OnFragmentInteractionListener mListener;


    public static Fragment newInstance(ArrayList< String> data) {
        PreviewInvitationBackPressed fragment = new PreviewInvitationBackPressed();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM, data);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mBinding = DataBindingUtil.inflate(inflater, R.layout.create_invitation_fragment,container,false);
        inv = new Invitation();
        Invitation invitation = ((CreateInvitationActivity)getActivity()).onPreviewInvation();
        mBinding.txtBrideFirstName.setText(invitation.getHostName().getFName());
        mBinding.txtBrideLastName.setText(invitation.getBrideLName());
        mBinding.txtGroomFirstName.setText(invitation.getGroomName());
        mBinding.txtGroomLastName.setText(invitation.getGroomLName());
        mBinding.txtTitleBrideGroom.setText(invitation.getTitle());
        mBinding.txtAddress.setText(invitation.getAddress());
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateInvitationFragment.OnFragmentInteractionListener) {
            mListener = (CreateInvitationFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onClick(View view) {
//        if (view == mBinding. && getActivity() != null) {
//            previewWeddingCard();
//        }


    }
}
