package com.jeeb.mycommunity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jeeb.mycommunity.authintication.LoginActivity;
import com.jeeb.mycommunity.authintication.LoginHelper;
import com.jeeb.mycommunity.authintication.User;
import com.jeeb.mycommunity.community.Community;
import com.jeeb.mycommunity.community.CommunityHelper;
import com.jeeb.mycommunity.databinding.ActivityLandingBinding;
import com.jeeb.mycommunity.databinding.CommunityItemBinding;
import com.jeeb.mycommunity.utils.AppUtil;

import static com.jeeb.mycommunity.utils.ConstraintValues.*;

public class LandingActivity extends AppCompatActivity implements CommunityHelper.OnCommunitiesCreate,
        CommunityHelper.OnCommunitiesLoad,LoginHelper.OnGetCurrentUser,View.OnClickListener {

    private ActivityLandingBinding mBinding;
    private RetrofitAPIInterface mAPIInterface;
    private SharedPreferences preferences;
    boolean registered;
    private CommunityHelper mCommunityHelper;
    private LoginHelper mLoginHelper;
    private String mToken;
    private Community.Count mCommunity;
    private static final String MASTER_KEY ="MfyuHZcVmFJwgcoz1eJ5wFL9nnnBfE7CS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_landing);
        preferences = getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
        mToken = preferences.getString("token","");
        registered = preferences.getBoolean("registered",false);

        if (!TextUtils.isEmpty(mToken)){
            mLoginHelper.getCurrentUser(mToken, mAPIInterface);
            Intent intent = new Intent(LandingActivity.this, MainActivity.class);
            startActivity(intent);
            AppUtil.showProgress(true,this,mBinding.loginProgress,mBinding.landingContain);
        }else {
            Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        if (mAPIInterface == null) {
            mAPIInterface = RetrofitAPIClient.getClient().create(RetrofitAPIInterface.class);
        }
        if (mLoginHelper == null){
            mLoginHelper = new LoginHelper();
        }
        if (mCommunityHelper == null){
            mCommunityHelper = new CommunityHelper();
        }
        preferences = getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
        mToken = preferences.getString("token","");
        registered = preferences.getBoolean("registered",false);
        mLoginHelper.setCurrentUser(this);
        mBinding.fabAdd.setOnClickListener(this);
        mBinding.fabLogin.setOnClickListener(this);
        mCommunityHelper.setCreate(this);
        mCommunityHelper.setLoad(this);
        mAPIInterface = RetrofitAPIClient.getClient().create(RetrofitAPIInterface.class);
//        mCommunityHelper.getCommunities(MASTER_KEY,"",mAPIInterface);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE){
            if (data != null){
                if(data.getBooleanExtra(IS_LOGIN_SUCCEEDED, false) && !TextUtils.isEmpty(data.getStringExtra(TOKEN))){
                    mToken = data.getStringExtra(TOKEN);
                    mLoginHelper.getCurrentUser(mToken, mAPIInterface);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onCurrentUserLoadedSuccessFully(boolean isLogin, User user) {
        AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.landingContain);
        preferences = getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
        String token = preferences.getString( "token",mToken);
        Intent intent = new Intent(this,MainActivity.class);
        startActivityForResult(intent,LOGIN_REQUEST_CODE);
        KeepRequiredData.getInstance().setCurrentUser(user);

    }

    @Override
    public void onFailedLoadingCurrentUser(String error) {

        if (!TextUtils.isEmpty(mToken)&& error == null) {
//            mCommunityHelper.getCommunities(mToken, "1234567", mAPIInterface);
//            AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.landingContain);
        }else {
            Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCommunitiesLoadSuccessFully() {
//        AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.landingContain);
    }

    @Override
    public void onFieldCommunitiesLoad() {
        AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.landingContain);
    }

    @Override
    public void onCommunitySaveSuccessFully(Community community) {
        AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.landingContain);
        if (community!= null){
            CommunityAdapter adapter = new CommunityAdapter(community);
            adapter.setItemClickListener(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
//            mBinding.commList.setLayoutManager(layoutManager);
//            mBinding.commList.setAdapter(adapter);
        }
    }
    @Override
    public void onFieldSavedCommunity() {
        AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.landingContain);
    }

    @Override
    public void onClick(View view) {

        if (view == mBinding.fabAdd){
//            mBinding.commCreateLayout.setVisibility(View.VISIBLE);
//            mBinding.commListLayout.setVisibility(View.GONE);
            if (mCommunityHelper == null){
                mCommunityHelper = new CommunityHelper();
                mCommunityHelper.setLoad(this);
                mCommunityHelper.setCreate(this);
            }
            if (setCommunityInfo()){
//                mCommunityHelper.createCommunity(mCommunity,mAPIInterface);
//                AppUtil.showProgress(true,this,mBinding.loginProgress,mBinding.landingContain);
            }


        }else if (view == mBinding.fabLogin){
            //launch login activity
            Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }else {
//            mBinding.commCreateLayout.setVisibility(View.GONE);
//            mBinding.commListLayout.setVisibility(View.VISIBLE);
        }
    }
    private boolean setCommunityInfo(){
        // TODO: 1/17/2019 should validate for empty EditText if not empty return true.
        mCommunity = new Community.Count();
//        mCommunity.setCommDesc(mBinding.commDesc.toString());
//        mCommunity.setCommName(mBinding.communityName.toString());
        return true;
    }
    private class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommViewHolder> implements OnCommunityItemClickListener{

        private OnCommunityItemClickListener mItemClickListener;
        private Community mCommunities;

        public CommunityAdapter(Community communities) {
            mCommunities = communities;
        }

        public void setItemClickListener(OnCommunityItemClickListener itemClickListener) {
            mItemClickListener = itemClickListener;
        }

        @NonNull
        @Override
        public CommViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new CommViewHolder(viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull CommViewHolder commViewHolder, int i) {
            commViewHolder.onBind(mCommunities,i);
        }

        @Override
        public int getItemCount() {
            if (mCommunities.getCount().size()> 0){
                return mCommunities.getCount().size();
            }
            return 0;
        }

        @Override
        public void onCommunityItemListener() {

        }

        public class CommViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            CommunityItemBinding mBinding;

            public CommViewHolder(@NonNull ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item,parent,false));
                mBinding = DataBindingUtil.bind(itemView);
                mBinding.commItem.setOnClickListener(this);
                mBinding.joinCommunity.setOnClickListener(this);
                mBinding.btnSubmitPass.setOnClickListener(this);
                itemView.setOnClickListener(this);
            }

            public void onBind(Community community, int position) {

                mBinding.commItem.setOnClickListener(this);
                if (community != null) {
                    mBinding.commItem.setText(community.getCount().get(position).getCommName());
                }
            }

            @Override
            public void onClick(View view) {
                if (view == mBinding.joinCommunity){
                    mBinding.joinCommunity.setText("you clicked");
                    mBinding.edPassword.setVisibility(View.VISIBLE);
                    mBinding.btnSubmitPass.setVisibility(View.VISIBLE);
                    mItemClickListener.onCommunityItemListener();
                }else if (view == mBinding.btnSubmitPass){
                    Toast.makeText(LandingActivity.this, "submit password", Toast.LENGTH_SHORT).show();
//                    if (mCommunities.getCount().get(getAdapterPosition()).getCommPassword() == )
                }

            }
//            public void validateCommPass()
        }
    }
    public interface OnCommunityItemClickListener{
        void onCommunityItemListener();
    }
}
