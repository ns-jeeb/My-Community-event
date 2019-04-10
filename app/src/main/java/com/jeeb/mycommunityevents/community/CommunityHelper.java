package com.jeeb.mycommunityevents.community;

import android.util.Log;

import com.jeeb.mycommunityevents.KeepRequiredData;
import com.jeeb.mycommunityevents.RetrofitAPIClient;
import com.jeeb.mycommunityevents.RetrofitAPIInterface;
import com.jeeb.mycommunityevents.authintication.Id;
import com.jeeb.mycommunityevents.authintication.User;

import java.net.IDN;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityHelper {

    private OnCommunitiesLoad mLoad;
    private OnCommunitiesCreate mCreate;
    private static final String MASTER_KEY ="MyuHZcVmFJwgcoz1eJ5wFL9nnnBfE7CS";
    private GetAllUsers mAllUsers;


    public void setAllUsers(GetAllUsers allUsers) {
        mAllUsers = allUsers;
    }
    public void setLoad(OnCommunitiesLoad load) {
        mLoad = load;
    }

    public void setCreate(OnCommunitiesCreate create) {
        mCreate = create;
    }

    public void createCommunity(Community.Count community, RetrofitAPIInterface apiInterface){
        Map<String, String> communityBody = new HashMap<>();
            communityBody.put("comm_name",community.getCommName());
            communityBody.put("comm_desc",community.getCommDesc());
//        communityBody.put( "comm_anouncement",community.getCommAnouncement());
            communityBody.put("comm_password",community.getCommPassword() );
            communityBody.put("user_comm_id",community.getId());
            communityBody.put("access_token",MASTER_KEY);



        Call<Community> communityCall = apiInterface.createCommunity(communityBody);
        communityCall.enqueue(new Callback<Community>() {
            @Override 
            public void onResponse(Call<Community> call, Response<Community> response) {
                if (response.isSuccessful()){
                    mLoad.onCommunitiesLoadSuccessFully();
                    Log.i("Community"," "+response.body());
                }else if (response.code() >=400 && response.code() <409){
                    //authentication error
                    mLoad.onFieldCommunitiesLoad();
                }else {
                    //connection error
                    mLoad.onFieldCommunitiesLoad();
                }
            }

            @Override
            public void onFailure(Call<Community> call, Throwable t) {
                Log.i("Failure"," "+t.getMessage());
            }
        });
    }

    public void getCommunities(String token,String password, RetrofitAPIInterface apiInterface){
        //this if you want more body to getCommunities.
//        Map<String, String> communityBody = new HashMap<>();
//        communityBody.put("comm_password", password);

        Call<Community> communityCall = apiInterface.getCommunities(token);
        communityCall.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(Call<Community> call, Response<Community> response) {
                if (response.isSuccessful()){
                    Log.i("Community"," "+response.body());
                    mCreate.onCommunitySaveSuccessFully(response.body());
                }else if (response.code() >=400 && response.code() <409){
                    //authentication error
                    mCreate.onFieldSavedCommunity();
                }else {
                    //connection error
                    mCreate.onFieldSavedCommunity();
                }
            }

            @Override
            public void onFailure(Call<Community> call, Throwable t) {
                Log.i("Failure"," "+t.getMessage());
            }
        });
    }

    public void getAllUsers(String bearerToken, RetrofitAPIInterface apiInterface) {

        Map<String, String> header = new HashMap<>();
        header.put("Authorization","Bearer "+bearerToken);

        Call<List<User>>callPost = apiInterface.getUsers("Bearer "+bearerToken);
        callPost.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.e("TAG",response.code()+"Success Post call");
                if (response.isSuccessful()){
                    KeepRequiredData.getInstance().setUsers((ArrayList<User>) response.body());
                    mAllUsers.onAllUserLoadedSuccessFully(true, (ArrayList<User>) response.body());
//                    Id id = new Id();
                    String id = response.body().get(0).getId();
                }else if (response.code() >= 400 && response.code() <409){
                    mAllUsers.onFailedLoadAllUser("User name or password is invalid");
                }else {
                    mAllUsers.onFailedLoadAllUser("Please try again latter");
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("TAG",call+"Failure");
                mAllUsers.onFailedLoadAllUser("Please try again latter");
            }
        });
    }

    public interface GetAllUsers {
        void onAllUserLoadedSuccessFully(boolean isMe, ArrayList<User> users);
        void onFailedLoadAllUser(String error);
    }

    public interface OnCommunitiesLoad {
        void onCommunitiesLoadSuccessFully();
        void onFieldCommunitiesLoad();
    }

    public interface OnCommunitiesCreate {
        void onCommunitySaveSuccessFully(Community community);
        void onFieldSavedCommunity();
    }
}
