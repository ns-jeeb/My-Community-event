package com.jeeb.mycommunityevents.authintication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.jeeb.mycommunityevents.KeepRequiredData;
import com.jeeb.mycommunityevents.MainActivity;
import com.jeeb.mycommunityevents.R;
import com.jeeb.mycommunityevents.RetrofitAPIClient;
import com.jeeb.mycommunityevents.RetrofitAPIInterface;
import com.jeeb.mycommunityevents.databinding.ActivityLoginBinding;
import com.jeeb.mycommunityevents.utils.AppUtil;
import com.jeeb.mycommunityevents.utils.ConstraintValues;

@SuppressLint("RestrictedApi")
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,OnClickListener,LoginHelper.OnSaveUserInShearedPref,
        LoginHelper.OnUserLogin, LoginHelper.OnGetCurrentUser, RegisterationFragment.OnFragmentInteractionListener {

    private ActivityLoginBinding mBinding;
    private RetrofitAPIInterface mAPIInterface;
    private SharedPreferences.Editor editor;
    private LoginHelper helper;
    private SharedPreferences preferences;
    private boolean registered;
    private Fragment fragment;


    RadioGroup.OnCheckedChangeListener mRadioGender;
    CheckBox.OnCheckedChangeListener mRememberMe = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Toast.makeText(LoginActivity.this, "this will save your user name password", Toast.LENGTH_SHORT).show();
        }
    };
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBinding.fabCreateAccount.setVisibility(View.GONE);
        if (mAPIInterface == null) {
            mAPIInterface = RetrofitAPIClient.getClient().create(RetrofitAPIInterface.class);
        }
        if (helper == null) {
            helper = new LoginHelper();
        }
        preferences = getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
        registered = preferences.getBoolean("registered", false);
        mBinding.rememberMe.setOnCheckedChangeListener(mRememberMe);
        mToken = preferences.getString("token","");

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.auth_main_layout);
        if (fragment == null){
            fragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.auth_main_layout,fragment).commit();
        }
        mBinding.fabLock.setOnClickListener(this);
        mBinding.btnJoinCommunity.setOnClickListener(this);
        mBinding.fabCreateAccount.setOnClickListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        mBinding.fabLock.setVisibility(View.VISIBLE);
        mBinding.btnJoinCommunity.setVisibility(View.VISIBLE);
        super.onResume();
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == ConstraintValues.REQUEST_READ_CONTACTS) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                populateAutoComplete();
//            }
//        }
//    }
//    private boolean isEmailValid(String email) {
//        //TODO: Replace this with your own logic
//        return email.contains("@");
//    }
//    private boolean isPasswordValid(String password) {
//        //TODO: Replace this with your own logic
//        return password.length() > 6;
//    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);
////            mBinding.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
//            mBinding.loginProgress.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
////                    mBinding.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//            mBinding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
//            mBinding.loginProgress.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mBinding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mBinding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
////            mBinding.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
                // Select only mEmail addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary mEmail addresses first. Note that there won't be
                // a primary mEmail address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
//        mBinding.loginEmail.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
    @Override
    public void onClick(View view) {
        helper = new LoginHelper();
        helper.setOnSaveUserInShearedPref(this);
        helper.setOnUserLogin(this);
        helper.setCurrentUser(this);
        editor = preferences.edit();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.auth_main_layout);
        if (mBinding.fabLock == view) {
            if (fragment instanceof LoginFragment){
                AppUtil.showProgress(true,this,mBinding.loginProgress,mBinding.authBtnLayout);
                ((LoginFragment)fragment).loginUser();
            }

        } else if (view == mBinding.btnJoinCommunity) {
            fragment = new RegisterationFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.auth_main_layout, fragment).commit();
            mBinding.fabCreateAccount.setVisibility(View.VISIBLE);
            mBinding.btnJoinCommunity.setVisibility(View.GONE);
            mBinding.fabLock.setVisibility(View.GONE);
        }
        else if (fragment != null && view == mBinding.fabCreateAccount && ((RegisterationFragment) fragment).setData() != null) {
            helper.register(((RegisterationFragment) fragment).setData(), mAPIInterface);
        }else {
            mBinding.fabCreateAccount.setVisibility(View.GONE);
            mBinding.fabLock.setVisibility(View.VISIBLE);
            mBinding.btnJoinCommunity.setVisibility(View.VISIBLE);
        }
    }

//    private void loginUser(String email, String password) {
//        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
//            helper.authenticUser(email, password, mAPIInterface);
//            showProgress(true);
//        }else {
//            mBinding.joinMember.joinForm.setVisibility(View.GONE);
//            mBinding.loginForm.setVisibility(View.VISIBLE);
//            mBinding.fabLock.setVisibility(View.VISIBLE);
//            mBinding.btnSubmit.setVisibility(View.GONE);
//        }
//    }
    @Override
    public void onSaveUserSuccessFully(boolean isRegistered) {
        if (isRegistered) {
//            showProgress(false);
//            mBinding.joinMember.joinForm.setVisibility(View.GONE);
//            mBinding.loginForm.setVisibility(View.VISIBLE);
            mBinding.fabLock.setVisibility(View.VISIBLE);
            mBinding.fabCreateAccount.setVisibility(View.GONE);
        }
    }
    @Override
    public void onFailedSavedUser(String error) {
//        showProgress(false);
        showAlertDialogButtonClicked(error);
    }
    public void authenticUser(String email, String pass){
        helper.authenticUser(email,pass,mAPIInterface);
        AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.authBtnLayout);
    }

    @Override
    public void onUserLoggedInSuccessFully(boolean isLogin, String token) {
        if (isLogin) {
            helper.getCurrentUser(token,mAPIInterface);
            SharedPreferences preferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
            if (mBinding.rememberMe.isChecked()) {
                preferences.edit().putString("token", token).apply();
            }else {
                preferences.edit().putString("token","").apply();
            }
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(ConstraintValues.TOKEN,token);
            startActivity(intent);
            finish();
        }
    }
    public void showAlertDialogButtonClicked(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Authorization Failed");
        builder.setMessage(message);
        fragment = getSupportFragmentManager().findFragmentById(R.id.auth_main_layout);
        // add the buttons
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!(fragment instanceof LoginFragment)){
                    fragment = new LoginFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.auth_main_layout, fragment).commit();
                mBinding.fabLock.setVisibility(View.VISIBLE);
                mBinding.btnJoinCommunity.setVisibility(View.VISIBLE);
                mBinding.fabCreateAccount.setVisibility(View.GONE);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!(fragment instanceof RegisterationFragment)){
                    fragment = new RegisterationFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.auth_main_layout, fragment).commit();
                mBinding.fabCreateAccount.setVisibility(View.VISIBLE);
                mBinding.fabLock.setVisibility(View.GONE);
                mBinding.btnJoinCommunity.setVisibility(View.GONE);
                dialogInterface.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onFailedUserLoggedIn(String error) {
//        showProgress(false);
        showAlertDialogButtonClicked(error);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCurrentUserLoadedSuccessFully(boolean isLogin, User user) {
        AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.authBtnLayout);
        KeepRequiredData.getInstance().setCurrentUser(user);
    }
    @Override
    public void onFailedLoadingCurrentUser(String error) {
        AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.authBtnLayout);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}

