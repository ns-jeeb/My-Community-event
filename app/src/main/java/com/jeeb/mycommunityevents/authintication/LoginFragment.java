package com.jeeb.mycommunityevents.authintication;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jeeb.mycommunityevents.MainActivity;
import com.jeeb.mycommunityevents.R;
import com.jeeb.mycommunityevents.RetrofitAPIInterface;
import com.jeeb.mycommunityevents.databinding.FragmentLoginBinding;
import com.jeeb.mycommunityevents.utils.AppUtil;
import com.jeeb.mycommunityevents.utils.ConstraintValues;

import java.util.Objects;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FragmentLoginBinding mBinding;
    private String mEmail;
    private String mPass;
    public LoginFragment() {
        // Required empty public constructor
    }
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        populateAutoComplete();
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false);
        return mBinding.getRoot();
    }
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
    }
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (Objects.requireNonNull(getActivity()).checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
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

    private boolean validateEmailField(EditText emailField) {
        emailField.setError(null);
        mEmail = emailField.getText().toString();
        if (TextUtils.isEmpty(mEmail)&& getActivity() != null) {
            emailField.setError(getString(R.string.error_field_required));
            AppUtil.showProgress(false,getActivity(),getActivity().findViewById(R.id.login_progress),getActivity().findViewById(R.id.auth_btn_layout));
            emailField.requestFocus();
            return false;
        }else if (!isEmailValid(mEmail)&& getActivity() != null){
            emailField.setError(getString(R.string.error_invalid_email));
            AppUtil.showProgress(false,getActivity(),getActivity().findViewById(R.id.login_progress),getActivity().findViewById(R.id.auth_btn_layout));
            emailField.requestFocus();
        }
        return true;
    }
    private boolean validatePasswordField(EditText passField) {
        // Reset errors.
        passField.setError(null);
        mPass = passField.getText().toString();

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(mPass)&& getActivity() != null) {
            passField.setError(getString(R.string.error_invalid_password));
            passField.requestFocus();
//            showProgress(true);
            return false;
        }else if (!isPasswordValid(mPass)&& getActivity() != null){
            passField.setError(getString(R.string.error_invalid_password));
            AppUtil.showProgress(false,getActivity(),getActivity().findViewById(R.id.login_progress),getActivity().findViewById(R.id.auth_btn_layout));
            passField.requestFocus();
            return false;
        }
        return true;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }
    @Override
    public void onClick(View view) {
    }
    public void loginUser() {
        if ((validateEmailField(mBinding.loginEmail)&& validatePasswordField(mBinding.loginPassword))&&getActivity() != null){
            mEmail = mBinding.loginEmail.getText().toString();
            mPass = mBinding.loginPassword.getText().toString();
            ((LoginActivity) getActivity()).authenticUser(mEmail, mPass);
        }
    }
}
