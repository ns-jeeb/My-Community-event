package com.jeeb.mycommunityevents.authintication;

import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.jeeb.mycommunityevents.KeepRequiredData;
import com.jeeb.mycommunityevents.RetrofitAPIInterface;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginHelper implements Callback<User> {

    boolean isRegistered;
    private String token = null;
    private static final String MASTER_KEY ="MyuHZcVmFJwgcoz1eJ5wFL9nnnBfE7CS";
    private OnSaveUserInShearedPref mOnSaveUserInShearedPref;
    private OnUserLogin mOnUserLogin;
    private OnGetCurrentUser mCurrentUser;

    public void setCurrentUser(OnGetCurrentUser currentUser) {
        mCurrentUser = currentUser;
    }

    public void setOnUserLogin(OnUserLogin onUserLogin) {
        mOnUserLogin = onUserLogin;
    }

    public void setOnSaveUserInShearedPref(OnSaveUserInShearedPref onSaveUserInShearedPref) {
        mOnSaveUserInShearedPref = onSaveUserInShearedPref;
    }


    public void authenticUser(String userName, String password, RetrofitAPIInterface apiInterface){

//        Map<String, String> header = new HashMap<>();
        Map<String, String> body = new HashMap<>();

//        User user = new User();
//        user.setAccess_token(MASTER_KEY);
//        header.put("email",userName);
//        header.put("password",password);

        body.put("access_token",MASTER_KEY);

        String base = userName+":"+password;
        String auth = Base64.encodeToString(base.getBytes(),Base64.NO_WRAP);

        Call<User> call = apiInterface.doAuthenticatedMember("Basic "+auth,body);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, @NonNull Response<User> response) {
                Log.e("TAG",response.code()+" Success get Call");

                if (response.isSuccessful()&& response.body() != null){
                    KeepRequiredData.getInstance().setCurrentUser(response.body());
                    mOnUserLogin.onUserLoggedInSuccessFully(true, response.body().getToken());
                }else if (response.code() >= 400 && response.code() <409){
                    mOnUserLogin.onFailedUserLoggedIn("User name or password is invalid");
                }else {
                    mOnUserLogin.onFailedUserLoggedIn("Please try again latter");
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("TAG",call+" onFailure get Call");
                mOnUserLogin.onFailedUserLoggedIn("Please try again latter");
            }
        });
    }

    public void register(User user, RetrofitAPIInterface apiInterface) {

        User.UserCommId userCommId = new User.UserCommId();
        userCommId.set$oid("5c33e2c5408c9942cca838e3");
//
        Map<String, String> header = new HashMap<>();
        header.put("email",user.getEmail());
        header.put("password",user.getPassword());
        header.put("f_name",user.getFName());
        header.put("l_name",user.getLName());
        header.put("address",user.getAddress());
        header.put("city",user.getCity());
        header.put("province",user.getProvince());
        header.put("postal_code",user.getPostalCode());
        header.put("home_phone",user.getHomePhone());
        header.put("cell_phone",user.getCellPhone());
        header.put("user_comm_id",user.getUserCommId());
        header.put("gender",user.getGender());
        header.put("age",user.getAge());
        header.put("access_token",MASTER_KEY);

//        JSONObject jsonObject = new JSONObject();
//        try {
//            JSONObject commId = new JSONObject();
//            commId.put("$oid",user.getUserCommId().get$oid());
//            jsonObject.put("email",user.getEmail());
//            jsonObject.put("password",user.getPassword());
//            jsonObject.put("f_name",user.getFName());
//            jsonObject.put("l_name",user.getLName());
//            jsonObject.put("address",user.getAddress());
//            jsonObject.put("city",user.getCity());
//            jsonObject.put("province",user.getProvince());
//            jsonObject.put("postal_code",user.getPostalCode());
//            jsonObject.put("home_phone",user.getHomePhone());
//            jsonObject.put("cell_phone",user.getCellPhone());
//            jsonObject.put("user_comm_id",userCommId.get$oid());
//            jsonObject.put("gender",user.getGender());
//            jsonObject.put("age",user.getAge());
////            jsonObject.put("role", "admin");
//            jsonObject.put("access_token",MASTER_KEY);
//        } catch (JSONException e) {
//            e.printStackTrace();5c40d84036b1cf0017f13475
//        }
        Call<User>callPost = apiInterface.createMember(header);
        callPost.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e("TAG",response.code()+"Success Post call");
                if (response.isSuccessful()) {
                    isRegistered = true;
                    KeepRequiredData.getInstance().setCurrentUser(response.body());
                    mOnSaveUserInShearedPref.onSaveUserSuccessFully(true);
                } else if (response.code() >= 400 && response.code() <409){
                    mOnSaveUserInShearedPref.onFailedSavedUser("You are not authorized to register please contact community " +"admin");
                }else {
                    mOnSaveUserInShearedPref.onFailedSavedUser("Please try again latter");
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("TAG",call+"Failure");
                mOnSaveUserInShearedPref.onFailedSavedUser("Please try again latter");
                isRegistered = false;
            }
        });
    }


    public void getCurrentUser(String bearerToken, RetrofitAPIInterface apiInterface) {

        Map<String, String> header = new HashMap<>();
        header.put("Authorization","Bearer "+bearerToken);

        Call<User>callPost = apiInterface.getCurrentUser("Bearer "+bearerToken);
        callPost.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e("TAG",response.code()+"Success Post call");
                if (response.isSuccessful()){
                    KeepRequiredData.getInstance().setCurrentUser(response.body());
                    mCurrentUser.onCurrentUserLoadedSuccessFully(true,response.body());
                }else if (response.code() >= 400 && response.code() <409){
                    mCurrentUser.onFailedLoadingCurrentUser("User name or password is invalid");
                }else {
                    mCurrentUser.onFailedLoadingCurrentUser("Please try again latter");
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("TAG",call+"Failure");
                mCurrentUser.onFailedLoadingCurrentUser("Please try again latter");
            }
        });
    }

    @Override
    public void onResponse(Call<User> call, Response<User> response) {

    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {

    }

    public interface OnSaveUserInShearedPref{
        void onSaveUserSuccessFully(boolean isRegistered);
        void onFailedSavedUser(String error);
    }

    public interface OnUserLogin {
        void onUserLoggedInSuccessFully(boolean isLogin, String token);
        void onFailedUserLoggedIn(String error);
    }
    public interface OnGetCurrentUser {
        void onCurrentUserLoadedSuccessFully(boolean isLogin, User user);
        void onFailedLoadingCurrentUser(String error);
    }
}
