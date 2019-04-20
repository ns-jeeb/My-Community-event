package com.jeeb.mycommunity.invitation.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.jeeb.mycommunity.KeepRequiredData;
import com.jeeb.mycommunity.R;
import com.jeeb.mycommunity.authintication.User;
import com.jeeb.mycommunity.databinding.UserSelectItemBinding;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private OnUserClickListener mListener;
    private ArrayList<User> mUsers;
    private ArrayList<String> mSelectedUserId;

    public UserAdapter(ArrayList<User>users) {
        mUsers = new ArrayList<>();
        mSelectedUserId = new ArrayList<>();
        if (users != null && users.size() != 0)
        for (int i = 0; i<users.size(); i ++){
            if (users.get(i).getId() != null&& !users.get(i).getId().equalsIgnoreCase(KeepRequiredData.getInstance().getCurrentUser().getId())){
                mUsers.add(users.get(i)) ;
            }
        }
    }

    public void setListener(OnUserClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserAdapter.UserViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
        userViewHolder.bind(mUsers.get(i));
    }

    @Override
    public int getItemCount() {
        if (mUsers == null || mUsers.size()==0) {
            return 0;
        }
        return  mUsers.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        UserSelectItemBinding mBinding;

        public UserViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_select_item,parent,false));
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(User user){
            if (user != null&& !user.getId().equalsIgnoreCase(KeepRequiredData.getInstance().getCurrentUser().getId())){
                mBinding.selectUserName.setText(user.getFName());
                mBinding.selectLastName.setText(user.getLName());
                mBinding.lblCellPhone.setText(user.getCellPhone());
                mBinding.txtHomePhone.setText(user.getPostalCode());
                mBinding.txtAddress.setText(user.getAddress());
                mBinding.lblAddress.setText(user.getCity());
                Glide.with(itemView.getContext()).load(user.getPicture()).into(mBinding.selectUserAvatar);
                mBinding.chUserSelect.setOnCheckedChangeListener(this);
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            if (mBinding.userMoreInfoLayout.getVisibility() == View.VISIBLE){
                mBinding.userMoreInfoLayout.setVisibility(View.GONE);
            }else {
                mBinding.userMoreInfoLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b){
                mSelectedUserId.add(mUsers.get(getAdapterPosition()).getId());
                mListener.onUserCheckedListener(mSelectedUserId,getAdapterPosition());
            }else{
                checkSizeWithIndex(mSelectedUserId);

            }

        }
    }
    public void checkSizeWithIndex(ArrayList<String>selectedUserId){
        if (selectedUserId.size() >0) {
            for (int i = 0; i < selectedUserId.size(); i++) {
                selectedUserId.remove(i);
            }
        }
    }

    public interface OnUserClickListener {
        void onUserCheckedListener(ArrayList<String> users, int position);
    }

}
