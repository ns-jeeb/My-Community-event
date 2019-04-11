package com.jeeb.mycommunityevents.authintication;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jeeb.mycommunityevents.R;
import com.jeeb.mycommunityevents.RetrofitAPIInterface;
import com.jeeb.mycommunityevents.databinding.FragmentRegisterationBinding;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_registeration,container,false);
        return mBinding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
