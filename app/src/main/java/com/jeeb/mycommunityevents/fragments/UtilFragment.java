package com.jeeb.mycommunityevents.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.jeeb.mycommunityevents.R;
import com.jeeb.mycommunityevents.authintication.LoginFragment;
import com.jeeb.mycommunityevents.authintication.RegisterationFragment;

import java.util.List;

public class UtilFragment {
    public final static String TAG_NAME_FRAGMENT = "ACTIVITY_FRAGMENT";

    public static Fragment getFragmentByTagName(FragmentManager fragmentManager, String fragmentTagName) {
        Fragment ret = null;

        // Get all Fragment list.
        List<Fragment> fragmentList = fragmentManager.getFragments();

        if (fragmentList != null) {
            int size = fragmentList.size();
            for (int i = 0; i < size; i++) {
                Fragment fragment = fragmentList.get(i);

                if (fragment != null) {
                    String fragmentTag = fragment.getTag();

                    if (fragmentTag != null && fragmentTag.equals(fragmentTagName)) {
                        ret = fragment;
                    }
                }
            }
        }
        return ret;
    }

    public static void printActivityFragmentList(FragmentManager fragmentManager) {
        // Get all Fragment list.
        List<Fragment> fragmentList = fragmentManager.getFragments();

        if (fragmentList != null) {
            int size = fragmentList.size();
            for (int i = 0; i < size; i++) {
                Fragment fragment = fragmentList.get(i);

                if (fragment != null) {
                    String fragmentTag = fragment.getTag();
                    Log.d(TAG_NAME_FRAGMENT, fragmentTag);
                }
            }

            Log.d(TAG_NAME_FRAGMENT, "***********************************");
        }
    }

    public static void lunchFragment(String fragmentTag, FragmentManager fragmentManager, Fragment newFragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Get newFragment two if exist.
        Fragment fragment = fragmentManager.findFragmentById(R.id.auth_main_layout);
            if(fragment instanceof LoginFragment) {
                Log.d(UtilFragment.TAG_NAME_FRAGMENT, "Fragment Two exist in back stack, will hide it now.");
                // Hide newFragment two. Only hide not destroy.
                // When user type back menu in Fragment three,
                // this hidden Fragment will be shown again with input text saved.
                fragmentTransaction.hide(fragment);
                fragmentTransaction.add(R.id.auth_main_layout, newFragment, fragmentTag).addToBackStack(null);
            }else {
                fragmentTransaction.replace(R.id.auth_main_layout, newFragment, fragmentTag).addToBackStack(null);
            }

        fragmentTransaction.commit();
    }
}
