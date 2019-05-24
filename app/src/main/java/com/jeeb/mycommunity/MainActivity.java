package com.jeeb.mycommunity;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import com.jeeb.mycommunity.authintication.LoginActivity;
import com.jeeb.mycommunity.authintication.LoginHelper;
import com.jeeb.mycommunity.authintication.User;
import com.jeeb.mycommunity.community.CommunityHelper;
import com.jeeb.mycommunity.databinding.ActivityMainBinding;
import com.jeeb.mycommunity.fragments.MeetUpFragment;
import com.jeeb.mycommunity.invitation.fragments.CreateInvitationFragment;
import com.jeeb.mycommunity.fragments.FohtiahFragment;
import com.jeeb.mycommunity.invitation.fragments.ItemInvitationFragment;
import com.jeeb.mycommunity.utils.AppUtil;
import com.jeeb.mycommunity.utils.ConstraintValues;

import java.util.ArrayList;
@SuppressLint("RestrictedApi")
public class MainActivity extends AppCompatActivity implements
        MeetUpFragment.OnFragmentInteractionListener,View.OnClickListener,LoginHelper.OnGetCurrentUser,
        CreateInvitationFragment.OnFragmentInteractionListener,CommunityHelper.GetAllUsers,
        FohtiahFragment.OnFragmentInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ActivityMainBinding mBinding;
    private SharedPreferences preferences;
    private RetrofitAPIInterface mAPIInterface;
    private String mToken;
    boolean registered;
    private LoginHelper helper;
    private CommunityHelper mHelperAllUsers;
    private NotificationManager mNotifyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mBinding.container.setAdapter(mSectionsPagerAdapter);
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (mAPIInterface == null) {
            mAPIInterface = RetrofitAPIClient.getClient().create(RetrofitAPIInterface.class);
        }
        if (helper == null){
            helper = new LoginHelper();
            helper.setCurrentUser(this);
        }
        if (mHelperAllUsers == null){
            mHelperAllUsers = new CommunityHelper();
            mHelperAllUsers.setAllUsers(this);
        }
        mBinding.fabLockOpen.setOnClickListener(this);
        preferences = getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
        mToken = preferences.getString("token","");
        registered = preferences.getBoolean("registered",false);
        if (TextUtils.isEmpty(mToken)){
            mToken = getIntent().getStringExtra(ConstraintValues.TOKEN);
        }
        mHelperAllUsers.getAllUsers(mToken,mAPIInterface);
        AppUtil.showProgress(true,this,mBinding.progress,mBinding.container);
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstraintValues.LOGIN_REQUEST_CODE && data != null){
            if (data.getBooleanExtra(ConstraintValues.IS_BACK_PRESSED,false)){
                finish();
            }
        }else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (mBinding.fabLockOpen == view){
            SharedPreferences preferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
            preferences.edit().putString("token", "").apply();

            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onAllUserLoadedSuccessFully(boolean isMe, ArrayList<User> users) {
        KeepRequiredData.getInstance().setUsers(users);
        AppUtil.showProgress(false,this,mBinding.progress,mBinding.container);
    }

    @Override
    public void onFailedLoadAllUser(String error) {
        Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
        AppUtil.showProgress(false,this,mBinding.progress,mBinding.container);

    }

    @Override
    public void onCurrentUserLoadedSuccessFully(boolean isLogin, User user) {
        AppUtil.showProgress(false,this,mBinding.progress, mBinding.container);
        if (!TextUtils.isEmpty(mToken)&& mHelperAllUsers != null) {
            mHelperAllUsers.getAllUsers(mToken, mAPIInterface);
        }
    }

    @Override
    public void onFailedLoadingCurrentUser(String error) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String tag, Object data) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return ItemInvitationFragment.newInstance(mToken);
                case 1:
                    return MeetUpFragment.newInstance(position + 1);
                case 2:
                    return FohtiahFragment.newInstance();
            }
            return null;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "My Invitation";
                case 1:
                    return "Meet up";
                case 2:
                    return "Announcement";
            }
            return null;
        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
