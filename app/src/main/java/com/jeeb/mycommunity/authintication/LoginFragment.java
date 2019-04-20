package com.jeeb.mycommunity.authintication;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jeeb.mycommunity.R;
import com.jeeb.mycommunity.databinding.FragmentLoginBinding;
import com.jeeb.mycommunity.fragments.UtilFragment;
import com.jeeb.mycommunity.utils.AppUtil;
import com.jeeb.mycommunity.utils.ConstraintValues;

import java.util.Objects;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginFragment extends Fragment implements View.OnClickListener {
    private FragmentLoginBinding mBinding;
    private OnFragmentInteractionListener mInteractionListern;
    private String mEmail;
    private String mPass;
    private  FragmentManager fragmentManager;
    public LoginFragment() {
    }
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        populateAutoComplete();
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false);
        mBinding.btnJoinCommunity.setOnClickListener(this);
        mBinding.fabLock.setOnClickListener(this);
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
        if (TextUtils.isEmpty(mEmail)|| getActivity() == null) {
            emailField.setError(getString(R.string.error_field_required));
            emailField.requestFocus();
            return false;
        }else if (!isEmailValid(mEmail)|| getActivity() == null){
            emailField.setError(getString(R.string.error_invalid_email));
            emailField.requestFocus();
            return false;
        }
        AppUtil.showProgress(false,getActivity(),getActivity().findViewById(R.id.login_progress),getActivity().findViewById(R.id.auth_main_layout));
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
            AppUtil.showProgress(false,getActivity(),getActivity().findViewById(R.id.login_progress),getActivity().findViewById(R.id.auth_main_layout));
            passField.requestFocus();
            return false;
        }
        return true;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }
    @Override
    public void onClick(View view) {
        if (getActivity()!= null){
            fragmentManager = getActivity().getSupportFragmentManager();
            if (view == mBinding.btnJoinCommunity) {
                UtilFragment.lunchFragment("Registration_Fragment",fragmentManager,RegisterationFragment.newInstance());
            }else if (mBinding.fabLock == view) {
                if (loginUser()!= null){
                    ((LoginActivity)getActivity()).authenticate(loginUser());
                }
            }else {
                getActivity().onBackPressed();
            }
        }
    }
    public User loginUser() {
        User user = new User();
        if ((validateEmailField(mBinding.loginEmail) && validatePasswordField(mBinding.loginPassword)) && getActivity() != null) {
            user.setEmail(mBinding.loginEmail.getText().toString());
            user.setPassword(mBinding.loginPassword.getText().toString());
            return user;
        }
        return null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(User user);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterationFragment.OnFragmentInteractionListener) {
            mInteractionListern = (LoginFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInteractionListern = null;
    }
}
