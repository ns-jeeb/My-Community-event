package com.jeeb.mycommunityevents.community;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeeb.mycommunityevents.R;

public class CommunityActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        FragmentManager fg = getSupportFragmentManager();
        FragmentTransaction ft = fg.beginTransaction();
        ft.replace(R.id.community_content,new UsersListFragment()).commit();
    }


    public void doPositiveClick() {

    }

    public void doNegativeClick() {

    }

}
