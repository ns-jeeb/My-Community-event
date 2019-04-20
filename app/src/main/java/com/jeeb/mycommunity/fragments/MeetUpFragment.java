package com.jeeb.mycommunity.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jeeb.mycommunity.R;
import com.jeeb.mycommunity.databinding.MeetUpFragmentBinding;
import com.jeeb.mycommunity.utils.AppUtil;

public class MeetUpFragment extends Fragment implements View.OnTouchListener {
    private static final String ARG_PARAM1 = "param1";
    private int mParam1;
    private MeetUpFragmentBinding mBinding;

    private OnFragmentInteractionListener mListener;

    public MeetUpFragment() {
        // Required empty public constructor
    }

    public static MeetUpFragment newInstance(int secotionNumber) {
        MeetUpFragment fragment = new MeetUpFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, secotionNumber);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    String announcement;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.meet_up_fragment, container, false);
        mBinding.getRoot().setOnTouchListener(this);
        mBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mBinding.edOrganizerName.getText().toString();
                announcement = getString(R.string.announcement_txt,name,"");
                mBinding.multiAutoCompleteTextView.setText(announcement);
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(getActivity() != null){
            AppUtil.hideSoftKeyBoard(getActivity());
        }
        return false;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
