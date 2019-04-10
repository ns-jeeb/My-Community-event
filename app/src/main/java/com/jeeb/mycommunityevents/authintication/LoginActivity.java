package com.jeeb.mycommunityevents.authintication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.jeeb.mycommunityevents.KeepRequiredData;
import com.jeeb.mycommunityevents.MainActivity;
import com.jeeb.mycommunityevents.R;
import com.jeeb.mycommunityevents.RetrofitAPIClient;
import com.jeeb.mycommunityevents.RetrofitAPIInterface;
import com.jeeb.mycommunityevents.authintication.LoginHelper;
import com.jeeb.mycommunityevents.authintication.User;
import com.jeeb.mycommunityevents.databinding.ActivityLoginBinding;
import com.jeeb.mycommunityevents.utils.AppUtil;
import com.jeeb.mycommunityevents.utils.ConstraintValues;

import static android.Manifest.permission.READ_CONTACTS;
@SuppressLint("RestrictedApi")
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,OnClickListener,LoginHelper.OnSaveUserInShearedPref,
        LoginHelper.OnUserLogin, LoginHelper.OnGetCurrentUser {

    private ActivityLoginBinding mBinding;
    private RetrofitAPIInterface mAPIInterface;
    private SharedPreferences preferences;
    private boolean registered;
    private LoginHelper helper;
    private SharedPreferences.Editor editor;
    private String mEmail, mPass;
    private User mUser;


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
        populateAutoComplete();
        if (mAPIInterface == null) {
            mAPIInterface = RetrofitAPIClient.getClient().create(RetrofitAPIInterface.class);
        }
        if (helper == null) {
            helper = new LoginHelper();
        }
        preferences = getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
        registered = preferences.getBoolean("registered", false);
        mBinding.rememberMe.setOnCheckedChangeListener(mRememberMe);

        if (!registered) {
            mBinding.loginForm.setVisibility(View.GONE);
            mBinding.joinMember.joinForm.setVisibility(View.VISIBLE);
            mBinding.genderGroup.setVisibility(View.VISIBLE);
            mBinding.rememberMe.setVisibility(View.GONE);
        } else {
            mBinding.loginForm.setVisibility(View.VISIBLE);
            mBinding.joinMember.joinForm.setVisibility(View.GONE);
            mBinding.genderGroup.setVisibility(View.GONE);
            mBinding.rememberMe.setVisibility(View.VISIBLE);
        }

        mBinding.fabLock.setOnClickListener(this);
        mBinding.btnJoinCommunity.setOnClickListener(this);
        mBinding.btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

//        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mBinding.loginEmail, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, ConstraintValues.REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, ConstraintValues.REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstraintValues.REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private boolean validateEmailField(EditText emailField) {
        emailField.setError(null);
        mEmail = emailField.getText().toString();
        // Store values at the time of the login attempt.
        // Check for a valid mEmail address.
        if (TextUtils.isEmpty(mEmail)&& !isEmailValid(mEmail)) {
            emailField.setError(getString(R.string.error_field_required));
            emailField.requestFocus();
//            showProgress(true);
            return false;
        }
        return true;
    }

    private boolean validatePasswordField(EditText passField) {
        // Reset errors.
        passField.setError(null);
        mPass = passField.getText().toString();

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(mPass) || !isPasswordValid(mPass)) {
            passField.setError(getString(R.string.error_invalid_password));
            passField.requestFocus();
//            showProgress(true);
            return false;
        }
        return true;
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);
//            mBinding.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mBinding.loginProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mBinding.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mBinding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mBinding.loginProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mBinding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mBinding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
//            mBinding.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
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
        mBinding.loginEmail.setAdapter(adapter);
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
        if (mBinding.fabLock == view) {

            if (validateEmailField(mBinding.loginEmail) && validatePasswordField(mBinding.loginPassword)) {
                loginUser(mEmail, mPass);
            } else {
                mBinding.joinMember.joinForm.setVisibility(View.GONE);
                mBinding.loginForm.setVisibility(View.VISIBLE);
                mBinding.fabLock.setVisibility(View.VISIBLE);
                mBinding.btnSubmit.setVisibility(View.GONE);
                showProgress(false);
            }
        } else if (view == mBinding.btnJoinCommunity) {
            mBinding.joinMember.joinForm.setVisibility(View.VISIBLE);
            mBinding.btnSubmit.setVisibility(View.VISIBLE);
            mBinding.loginForm.setVisibility(View.GONE);
            mBinding.fabLock.setVisibility(View.GONE);
            showProgress(false);
        } else if (view == mBinding.btnSubmit && setData()!= null) {
            helper.register(setData(), mAPIInterface);
            showProgress(true);
        } else {
            editor.putBoolean("registered", false);
            mBinding.joinMember.joinForm.setVisibility(View.GONE);
            mBinding.loginForm.setVisibility(View.VISIBLE);
            mBinding.fabLock.setVisibility(View.VISIBLE);
            mBinding.btnSubmit.setVisibility(View.GONE);
        }
    }
    public User setData(){
        mUser = new User();
        User.UserCommId userCommId = new User.UserCommId();

        if (!validatePasswordField(mBinding.joinMember.joinPassword) || !validateEmailField(mBinding.joinMember.joinEmail) ||
                !getErrorField(mBinding.joinMember.fName) || !getErrorField(mBinding.joinMember.joinLName) ||
                !getErrorField(mBinding.joinMember.joinAddress) || !getErrorField(mBinding.joinMember.joinCity) ||
                !getErrorField(mBinding.joinMember.joinPostalCode) || !getErrorField(mBinding.joinMember.joinProvince) ||
                !getErrorField(mBinding.joinMember.joinHomePho) || !getErrorField(mBinding.joinMember.joinCellPho) ||
                !getErrorField(mBinding.joinMember.joinAge) || !getErrorField(mBinding.joinMember.joinCommunity)) {
            return null;
        } else {
            userCommId.set$oid(mBinding.joinMember.joinCommunity.getText().toString());
            mUser.setEmail(mEmail);
            mUser.setPassword(mPass);
            mUser.setFName(mBinding.joinMember.fName.getText().toString());
            mUser.setLName(mBinding.joinMember.joinLName.getText().toString());
            mUser.setAddress(mBinding.joinMember.joinAddress.getText().toString());
            mUser.setCity(mBinding.joinMember.joinCity.getText().toString());
            mUser.setPostalCode(mBinding.joinMember.joinPostalCode.getText().toString());
            mUser.setProvince(mBinding.joinMember.joinProvince.getText().toString());
            mUser.setHomePhone(mBinding.joinMember.joinHomePho.getText().toString());
            mUser.setCellPhone(mBinding.joinMember.joinCellPho.getText().toString());
            mUser.setUserCommId(mBinding.joinMember.joinCommunity.getText().toString());
            mUser.setGender("");
            mUser.setAge(mBinding.joinMember.joinAge.getText().toString());
        }
        return mUser;
    }
    private void loginUser(String email, String password) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            helper.authenticUser(email, password, mAPIInterface);
            showProgress(true);
        }else {
            mBinding.joinMember.joinForm.setVisibility(View.GONE);
            mBinding.loginForm.setVisibility(View.VISIBLE);
            mBinding.fabLock.setVisibility(View.VISIBLE);
            mBinding.btnSubmit.setVisibility(View.GONE);
        }
    }
    @Override
    public void onSaveUserSuccessFully(boolean isRegistered) {
        if (isRegistered) {
            showProgress(false);
            mBinding.joinMember.joinForm.setVisibility(View.GONE);
            mBinding.loginForm.setVisibility(View.VISIBLE);
            mBinding.fabLock.setVisibility(View.VISIBLE);
            mBinding.btnSubmit.setVisibility(View.GONE);
        }
    }
    @Override
    public void onFailedSavedUser(String error) {
        showProgress(false);
        showAlertDialogButtonClicked(error);
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
        // add the buttons
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mBinding.joinMember.joinForm.setVisibility(View.GONE);
                mBinding.loginForm.setVisibility(View.VISIBLE);
                mBinding.fabLock.setVisibility(View.VISIBLE);
                mBinding.btnSubmit.setVisibility(View.GONE);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mBinding.joinMember.joinForm.setVisibility(View.VISIBLE);
                mBinding.btnSubmit.setVisibility(View.VISIBLE);
                mBinding.loginForm.setVisibility(View.GONE);
                mBinding.fabLock.setVisibility(View.GONE);
                dialogInterface.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onFailedUserLoggedIn(String error) {
        showProgress(false);
        showAlertDialogButtonClicked(error);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
    public boolean getErrorField(EditText field) {
        View focusView;
        if (!TextUtils.isEmpty(field.getText().toString())) {
            return true;
        } else {
            field.setError(getString(R.string.error_field_required));
            focusView = field;
            focusView.requestFocus();
            return false;
        }
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
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}

