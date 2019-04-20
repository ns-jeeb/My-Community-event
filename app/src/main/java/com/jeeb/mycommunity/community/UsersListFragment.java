package com.jeeb.mycommunity.community;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jeeb.mycommunity.KeepRequiredData;
import com.jeeb.mycommunity.R;
import com.jeeb.mycommunity.databinding.FragmentUserRecyclerBinding;
import com.jeeb.mycommunity.invitation.adapters.UserAdapter;
import com.jeeb.mycommunity.invitation.fragments.CreateInvitationFragment;

import java.util.ArrayList;

public class UsersListFragment extends Fragment implements UserAdapter.OnUserClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private CreateInvitationFragment.OnFragmentInteractionListener mListener;
    private FragmentUserRecyclerBinding binding;

    public static UsersListFragment newInstance(String titleId) {
        UsersListFragment fragment = new UsersListFragment();
        Bundle args = new Bundle();
        args.putString("title", titleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_recycler,container,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        binding.userIds.setLayoutManager(layoutManager);
        binding.userIds.setHasFixedSize(true);
        UserAdapter userAdapter = new UserAdapter(KeepRequiredData.getInstance().getUsers());
        userAdapter.setListener(this);
        binding.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null && mUserIds!= null&& mUserIds.size()>0){
                    mListener.onFragmentInteraction("Wedding",null);
                }else {
                    Toast.makeText(getActivity(), "Please select your guest or press back to go to main page", Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.userIds.setAdapter(userAdapter);
        return binding.getRoot();
    }

//    @Override
//    public boolean onBackPressed() {
//        return true;
//    }

    @Override
    public void onStart() {
        super.onStart();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    ArrayList<String> mUserIds;

    @Override
    public void onUserCheckedListener(ArrayList<String> userIds, int position) {
        mUserIds = new ArrayList<>();
        if (userIds != null && userIds.size() != 0){
            mUserIds = userIds;
        }
    }
    public ArrayList<String> getUserIds(){
        return mUserIds;
    }

}
