package com.jeeb.mycommunityevents.authintication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.jeeb.mycommunityevents.fragments.UtilFragment;
import com.jeeb.mycommunityevents.utils.AppUtil;
import com.jeeb.mycommunityevents.utils.ConstraintValues;

@SuppressLint("RestrictedApi")
public class LoginActivity extends AppCompatActivity implements LoginHelper.OnSaveUserInShearedPref,
        LoginHelper.OnUserLogin, LoginHelper.OnGetCurrentUser, RegisterationFragment.OnFragmentInteractionListener,LoginFragment.OnFragmentInteractionListener {

    private ActivityLoginBinding mBinding;
    private RetrofitAPIInterface mAPIInterface;
    private SharedPreferences.Editor editor;
    private LoginHelper helper;
    private SharedPreferences preferences;
    private boolean registered;
    private FragmentManager mFragManager;
    private String mToken;
    private User mUser;
    private Fragment fragment;

    RadioGroup.OnCheckedChangeListener mRadioGender;
    CheckBox.OnCheckedChangeListener mRememberMe = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Toast.makeText(LoginActivity.this, "this will save your user name password", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
//        mBinding.fabCreateAccount.setVisibility(View.GONE);
        if (mAPIInterface == null) {
            mAPIInterface = RetrofitAPIClient.getClient().create(RetrofitAPIInterface.class);
        }
        if (helper == null) {
            helper = new LoginHelper();
        }
        helper.setOnSaveUserInShearedPref(this);
        helper.setOnUserLogin(this);
        helper.setCurrentUser(this);
        preferences = getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
        registered = preferences.getBoolean("registered", false);
        mBinding.rememberMe.setOnCheckedChangeListener(mRememberMe);
        mToken = preferences.getString("token","");
        if (mToken != null &&!mToken.isEmpty()){
            helper.getCurrentUser(mToken,mAPIInterface);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        fragment = LoginFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.auth_main_layout, fragment, "Login_Fragment");
        fragmentTransaction.commit();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return new CursorLoader(this,
//                // Retrieve data rows for the device user's 'profile' contact.
//                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
//                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
//                // Select only mEmail addresses.
//                ContactsContract.Contacts.Data.MIMETYPE +
//                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
//                .CONTENT_ITEM_TYPE},
//
//                // Show primary mEmail addresses first. Note that there won't be
//                // a primary mEmail address if the user hasn't specified one.
//                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
//    }
//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
//        List<String> emails = new ArrayList<>();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            emails.add(cursor.getString(ProfileQuery.ADDRESS));
//            cursor.moveToNext();
//        }
//        addEmailsToAutoComplete(emails);
//    }
//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//    }
//    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(LoginActivity.this,
//                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
////        mBinding.loginEmail.setAdapter(adapter);
//    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (getSupportFragmentManager().findFragmentById(R.id.auth_main_layout) instanceof RegisterationFragment){
            UtilFragment.lunchFragment("Login_Fragment",getSupportFragmentManager(),LoginFragment.newInstance());
        }else {
            finish();
        }

    }
    @Override
    public void onSaveUserSuccessFully(boolean isRegistered) {
        if (isRegistered) {
            AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.authMainLayout);
            UtilFragment.lunchFragment("Login_Fragment",getSupportFragmentManager(),LoginFragment.newInstance());
        }
    }
    @Override
    public void onFailedSavedUser(String error) {
        AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.authMainLayout);
        showAlertDialogButtonClicked(error);
    }

    @Override
    public void onUserLoggedInSuccessFully(boolean isLogin, String token) {
        if (isLogin) {
            AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.authMainLayout);
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
        }
    }
    public void showAlertDialogButtonClicked(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Authorization Failed");
        builder.setMessage(message);
        if (mFragManager == null){
            mFragManager = getSupportFragmentManager();
        }
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mFragManager.beginTransaction().replace(R.id.auth_main_layout, LoginFragment.newInstance()).addToBackStack(null).commit();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getSupportFragmentManager().beginTransaction().replace(R.id.auth_main_layout, RegisterationFragment.newInstance()).commit();
                dialogInterface.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onFailedUserLoggedIn(String error) {
        showAlertDialogButtonClicked(error);
        AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.authMainLayout);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCurrentUserLoadedSuccessFully(boolean isLogin, User user) {
        AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.authMainLayout);
        KeepRequiredData.getInstance().setCurrentUser(user);
    }
    @Override
    public void onFailedLoadingCurrentUser(String error) {
        AppUtil.showProgress(false,this,mBinding.loginProgress,mBinding.authMainLayout);
    }

    @Override
    public void onFragmentInteraction(User user) {
        AppUtil.showProgress(true,this,mBinding.loginProgress,mBinding.authMainLayout);
        helper.authenticUser(mUser.getPassword(),mUser.getEmail(),mAPIInterface);
    }

    public void authenticate(User user) {
        if (helper != null){
            AppUtil.showProgress(true,this,mBinding.loginProgress,mBinding.authMainLayout);
            helper.authenticUser(user.getEmail(),user.getPassword(),mAPIInterface);
        }
    }
    public void registerUser(User user) {
        if (helper != null){
            AppUtil.showProgress(true,this,mBinding.loginProgress,mBinding.authMainLayout);
            helper.register(user, mAPIInterface);
        }
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

