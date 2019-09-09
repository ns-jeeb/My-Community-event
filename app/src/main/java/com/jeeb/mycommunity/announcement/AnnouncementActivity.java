package com.jeeb.mycommunity.announcement;

import android.support.v4.app.Fragment;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeeb.mycommunity.R;
import com.jeeb.mycommunity.databinding.ActivityAnnouncementBinding;

public class AnnouncementActivity extends AppCompatActivity implements AnnouncementDashbordFrament.OnFragmentInteractionListener{

    ActivityAnnouncementBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_announcement);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.event_announce_contain);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction;
        if (fragment == null){
            fragment = AnnouncementDashbordFrament.newInstance("","");
            transaction = fm.beginTransaction();
            transaction.replace(R.id.event_announce_contain,fragment);
            transaction.addToBackStack(null).commit();


        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
