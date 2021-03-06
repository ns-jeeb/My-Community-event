package com.jeeb.mycommunity.authintication;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jeeb.mycommunity.R;
import com.jeeb.mycommunity.RetrofitAPIInterface;
import com.jeeb.mycommunity.databinding.FragmentRegisterationBinding;

public class RegisterationFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private LoginHelper helper;
    private FragmentRegisterationBinding mBinding;
    private RetrofitAPIInterface mAPIInterface;
    private User mUser;
    private String mPass;
    private String mEmail;

    public RegisterationFragment() {
        // Required empty public constructor
    }
    public static RegisterationFragment newInstance() {
        RegisterationFragment fragment = new RegisterationFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_registeration,container,false);
        mBinding.fabCreateAccount.setOnClickListener(this);
        return mBinding.getRoot();
    }
    public void onButtonPressed(User user) {
        if (mListener != null) {
            mListener.onFragmentInteraction(user);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (getActivity() == null) {
            return;
        }
        if (v == mBinding.fabCreateAccount) {
            setData();
            ((LoginActivity)getActivity()).registerUser(setData());
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(User user);
    }

    public User setData(){
        mUser = new User();
        User.UserCommId userCommId = new User.UserCommId();

        if (!validatePasswordField(mBinding.joinPassword) || !validateEmailField(mBinding.joinEmail) ||
                !getErrorField(mBinding.fName) || !getErrorField(mBinding.joinLName) ||
                !getErrorField(mBinding.joinAddress) || !getErrorField(mBinding.joinCity) ||
                !getErrorField(mBinding.joinPostalCode) || !getErrorField(mBinding.joinProvince) ||
                !getErrorField(mBinding.joinHomePho) || !getErrorField(mBinding.joinCellPho) ||
                !getErrorField(mBinding.joinAge) || !getErrorField(mBinding.joinCommunity)) {
            return null;
        } else {
            userCommId.set$oid(mBinding.joinCommunity.getText().toString());
            mUser.setEmail(mEmail);
            mUser.setPassword(mPass);
            mUser.setFName(mBinding.fName.getText().toString());
            mUser.setLName(mBinding.joinLName.getText().toString());
            mUser.setAddress(mBinding.joinAddress.getText().toString());
            mUser.setCity(mBinding.joinCity.getText().toString());
            mUser.setPostalCode(mBinding.joinPostalCode.getText().toString());
            mUser.setProvince(mBinding.joinProvince.getText().toString());
            mUser.setHomePhone(mBinding.joinHomePho.getText().toString());
            mUser.setCellPhone(mBinding.joinCellPho.getText().toString());
            mUser.setUserCommId(mBinding.joinCommunity.getText().toString());
            mUser.setGender("");
            mUser.setAge(mBinding.joinAge.getText().toString());
        }
        return mUser;
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
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

}
