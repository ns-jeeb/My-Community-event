package com.jeeb.mycommunity.toturials;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jeeb.mycommunity.R;
import com.jeeb.mycommunity.authintication.LoginFragment;
import com.jeeb.mycommunity.authintication.RegisterationFragment;
import com.jeeb.mycommunity.authintication.User;
import com.jeeb.mycommunity.databinding.ActivityToturialBinding;
import com.jeeb.mycommunity.fragments.UtilFragment;

public class ToturialActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterationFragment.OnFragmentInteractionListener, View.OnClickListener {
    ActivityToturialBinding mBinding;
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_toturial);

        setTitle("dev2qa.com - Fragment Back Stack Example.");
        fragment = LoginFragment.newInstance();
        // Get FragmentManager and FragmentTransaction object.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.auth_main_layout1, fragment, "Login_Fragment");
        fragmentTransaction.commit();
        UtilFragment.printActivityFragmentList(fragmentManager);

    }
    @Override
    public void onClick(View view) {
//        helper = new LoginHelper();
//        helper.setOnSaveUserInShearedPref(this);
//        helper.setOnUserLogin(this);
//        helper.setCurrentUser(this);
//        editor = preferences.edit();

//        if (mBinding.fabLock == view) {
//            UtilFragment.lunchFragment("Login_Fragment", getSupportFragmentManager(), LoginFragment.newInstance());
//            ((LoginFragment)fragment).loginUser();
//        } else if (view == mBinding.btnJoinCommunity) {
//            UtilFragment.lunchFragment("Registration_Fragment",getSupportFragmentManager(),RegisterationFragment.newInstance());
////            fragment = new RegisterationFragment();
////            UtilFragment.printActivityFragmentList(mFragManager);
////            mFragManager.beginTransaction().add(R.id.auth_main_layout,fragment).addToBackStack(null).commit();
////            mBinding.fabCreateAccount.setVisibility(View.VISIBLE);
//            mBinding.btnJoinCommunity.setVisibility(View.GONE);
//        }
//        else if (fragment != null && view == mBinding.fabCreateAccount && ((RegisterationFragment) fragment).setData() != null) {
////            helper.register(((RegisterationFragment) fragment).setData(), mAPIInterface);
//        }else {
////            mBinding.fabCreateAccount.setVisibility(View.GONE);
//            mBinding.btnJoinCommunity.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onFragmentInteraction(User user) {

    }
}
