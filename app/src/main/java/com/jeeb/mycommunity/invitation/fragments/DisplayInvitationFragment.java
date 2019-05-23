package com.jeeb.mycommunity.invitation.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeb.mycommunity.KeepRequiredData;
import com.jeeb.mycommunity.R;
import com.jeeb.mycommunity.RetrofitAPIClient;
import com.jeeb.mycommunity.RetrofitAPIInterface;
import com.jeeb.mycommunity.authintication.User;
import com.jeeb.mycommunity.databinding.InvitationItemBinding;
import com.jeeb.mycommunity.databinding.ListInvationsUsersFragmentBinding;
import com.jeeb.mycommunity.fragments.FohtiahFragment;
import com.jeeb.mycommunity.invitation.Invitation;
import com.jeeb.mycommunity.invitation.InvitationHelper;
import com.jeeb.mycommunity.invitation.InvitationRow;
import com.jeeb.mycommunity.invitation.activities.CreateInvitationActivity;
import com.jeeb.mycommunity.utils.ConstraintValues;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayInvitationFragment extends Fragment implements InvitationHelper.OnLoadInvitation {

    private ListInvationsUsersFragmentBinding mBinding;
    public ArrayList<Invitation> mInvitations;
    private FohtiahFragment.OnFragmentInteractionListener mListener;
    public static String ARG_PARAM = "token";
    private String mToken;
    private RetrofitAPIInterface mRetrofitAPIInterface;
    private InvitationHelper mHelper;
    private boolean noItemFound = false;

    public static DisplayInvitationFragment newInstance(String token) {
        DisplayInvitationFragment fragment = new DisplayInvitationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, token);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onSuccessLoadInvitation(boolean isLoaded, String successMessage) {
        if (mBinding.invitationSwipeRefresh.isRefreshing()){
            mBinding.invitationSwipeRefresh.setRefreshing(false);
        }
        if (isLoaded){
            InvitationAdapter myInvAdapter = new InvitationAdapter(fetchedInvited(KeepRequiredData.getInstance().getInvitations()));
            LinearLayoutManager linearManInv = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mBinding.listOfInvitation.setLayoutManager(linearManInv);
            mBinding.listOfInvitation.setAdapter(myInvAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public InvitationRow fetchedInvited(InvitationRow invitationRow){
        Invitation invitation ;
        if (KeepRequiredData.getInstance().getCurrentUser().getId()!= null){
            if (invitationRow != null && invitationRow.getRows()!= null){
                InvitationRow returnInvitations = new InvitationRow();
                ArrayList<Invitation>invitations = new ArrayList<>();
                for (int i = 0; i< invitationRow.getRows().size(); i++){
                    invitation = cleanTitle(invitationRow.getRows().get(i));
                    if (invitation != null){
                        for (String s :invitation.getGuestNames()){
                            if (s.equalsIgnoreCase(KeepRequiredData.getInstance().getCurrentUser().getId())){
                                invitations.add(invitation);
                                returnInvitations.setRows(invitations);
                            }
                        }
                    }
                }
                return returnInvitations;
            }
        }
        return null;
    }
    private Invitation cleanTitle(Invitation invitation) {
        String[] stringTitles = invitation.getTitle().split("/");
        String title = "";
        String id = "";
        String name = "";
        if (stringTitles.length > 0&& invitation!= null){
            for (int i = 0; i < stringTitles.length; i++) {

                if (i == 0) {
                    title = stringTitles[i];
                }else {
                    id = stringTitles[i];
                    name = getName(id);
                }
            }
            invitation.setTitle(title+name);
            return invitation;
        }
        return null;
    }
    public String getName(String id){
        String name = "";
        for (User u: KeepRequiredData.getInstance().getUsers()){
            if (u.getId().equalsIgnoreCase(id)){
                name = u.getFName();
            }
        }
        return name;
    }
    @Override
    public void onFailedInvitation(String errorMessage) {
    }
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_invations_users_fragment, container, false);

        if (getArguments()!= null){
            mToken = getArguments().getString("token");
        }else {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
            mToken = sharedPreferences.getString("token","");
        }
        loadInvitation();

        mBinding.btnCreateInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateInvitationActivity.class);
                intent.putExtra("token",mToken);
                startActivityForResult(intent, ConstraintValues.CREATE_INVITATION_CODE);
            }
        });
        mBinding.invitationSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadInvitation();
                mBinding.invitationSwipeRefresh.setRefreshing(true);
            }
        });
        mBinding.txtNoItem.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (noItemFound){
                    mBinding.txtNoItem.setVisibility(View.VISIBLE);
                }else {
                    mBinding.txtNoItem.setVisibility(View.GONE);
                }
            }
        },2000);

        return mBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.invitationSwipeRefresh.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
    }
    public void loadInvitation(){
        if (mHelper == null){
            mHelper = new InvitationHelper();
        }

        if (mInvitations == null || mInvitations.size() == 0) {
            mInvitations = new ArrayList<>();
        }
        if (mRetrofitAPIInterface == null){
            mRetrofitAPIInterface = RetrofitAPIClient.getClient().create(RetrofitAPIInterface.class);
        }
        mHelper.setLoadInvitation(this);
        mHelper.loadInvitation(mRetrofitAPIInterface,mToken);
    }
    boolean isFinished;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstraintValues.CREATE_INVITATION_CODE && data != null){
            isFinished = data.getBooleanExtra(ConstraintValues.FINISHED_ACTIVITY,false);
        }
    }

    public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.InvtionViewHolder> implements OnInvitationItemClickLister {

        OnInvitationItemClickLister onClickLister;
        InvitationRow mInvitationRow;
        public InvitationAdapter(InvitationRow invitationRow){
            mInvitationRow = invitationRow;
            if (mInvitationRow == null){
                mInvitationRow = new InvitationRow();
            }
        }
        public void setOnClickLister(OnInvitationItemClickLister onClickLister) {
            this.onClickLister = onClickLister;
        }
        @NonNull
        @Override
        public InvitationAdapter.InvtionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            setOnClickLister(this);
            return new InvtionViewHolder(viewGroup);
        }
        @Override
        public void onBindViewHolder(@NonNull InvitationAdapter.InvtionViewHolder invtionViewHolder, int i) {
            invtionViewHolder.binding(mInvitationRow.getRows(),i);
        }
        @Override
        public int getItemCount() {
            if (mInvitationRow.getRows()!= null && mInvitationRow.getRows().size() != 0){
                noItemFound = false;
                return mInvitationRow.getRows().size();
            }else {
                noItemFound = true;
            }
            return 0;
        }
        @Override
        public void onItemClick(View view) {
            view.setVisibility(View.VISIBLE);
        }
        public class InvtionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            InvitationItemBinding binding;
            public InvtionViewHolder(@NonNull ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation_item, parent, false));
                binding = DataBindingUtil.bind(itemView);
            }
            public void binding(List<Invitation> invitations, int i) {
                binding.txtMessage.setText(invitations.get(i).getTitle());
                binding.txtTitleBrideGroom.setText(invitations.get(i).getGroomName()+" "+invitations.get(i).getBrideName());
                binding.txtBrideFirstName.setText(invitations.get(i).getBrideName());
                binding.txtGroomFirstName.setText(invitations.get(i).getGroomName());
                binding.txtBrideLastName.setText(invitations.get(i).getGroomLName());
                binding.txtGroomLastName.setText(invitations.get(i).getGroomLName());
                binding.txtTime.setText(invitations.get(i).getDateTime());
                binding.txtNameOfPlace.setText(invitations.get(i).getNamePlace());
                binding.txtCityOfPostalProvince.setText(invitations.get(i).getCityPostalProvince());
//                Glide.with(getActivity()).load(R.drawable.ic_lock_open).into(binding.imgBrideGroom);
                binding.txtAddress.setText(invitations.get(i).getAddress());
                itemView.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
                onClickLister.onItemClick(binding.scrollerViewPreview);
            }
        }
    }
    public interface OnInvitationItemClickLister {
        void onItemClick(View view);
    }
}
