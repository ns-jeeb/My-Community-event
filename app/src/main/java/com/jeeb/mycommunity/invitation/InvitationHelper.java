package com.jeeb.mycommunity.invitation;

import android.util.Log;

import com.jeeb.mycommunity.KeepRequiredData;
import com.jeeb.mycommunity.RetrofitAPIInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationHelper {

    public static InvitationHelper invitationHelper;
    boolean isRegistered;
    private String token = null;
    private static final String MASTER_KEY ="MyuHZcVmFJwgcoz1eJ5wFL9nnnBfE7CS";

    private String TAG = "Invitation_Tag";
    private OnCreateInvitation mCreateInvitation;
    private OnLoadInvitation mLoadInvitation;

    public void setCreateInvitation(OnCreateInvitation createInvitation) {
        mCreateInvitation = createInvitation;
    }

    public void setLoadInvitation(OnLoadInvitation loadInvitation) {
        mLoadInvitation = loadInvitation;
    }

    public void createInvitation(RetrofitAPIInterface apiInterface, Invitation invitation, ArrayList<String> ids, String token){

        Map<String, Object> bodies = new HashMap<>();
//        bodies.put("host_name","5c4f4fa9605a760017bd2a34");
        bodies.put("host_name",invitation.getHostName().getId());
        bodies.put("title",invitation.getTitle()+"/"+invitation.getHostName().getId());
        bodies.put("message","");
        bodies.put("guest_names",ids);
        bodies.put("bride_l_name",invitation.getBrideName());
        bodies.put("bride_name",invitation.getBrideName());
        bodies.put("groom_l_name",invitation.getGroomLName());
        bodies.put("groom_name",invitation.getGroomName());
        bodies.put("num_guests","50");
        bodies.put("is_visiting","n");
        bodies.put("is_part_of_family","n");
        bodies.put("date_time",invitation.getDateTime());
        bodies.put("place_name",invitation.getNamePlace());
        bodies.put("place_addres",invitation.getAddress());
        bodies.put("postal_city_province",invitation.getCityPostalProvince());
        Call<Invitation> invitationCall = apiInterface.createInvitations("Bearer "+token,bodies);
        invitationCall.enqueue(new Callback<Invitation>() {
            @Override
            public void onResponse(Call<Invitation> call, Response<Invitation> response) {
                Log.i(TAG,""+response.body());
                String errMessage = "";
                if (response.isSuccessful() && response.code() < 400){
                    mCreateInvitation.onSuccessCreateInvitation(true,"Invitation is created successfully");
                }else {
                    if(response.code() == 400){
                        errMessage = "You might miss some field empty";
                    }else if (response.code() == 401){
                        errMessage = " You are not unAuthorized to create";
                    }else {
                        errMessage = "There might the same invitation is already exist";
                    }
                    mCreateInvitation.onFailedCreateInvitation(errMessage);
                }
            }

            @Override
            public void onFailure(Call<Invitation> call, Throwable t) {
                mCreateInvitation.onFailedCreateInvitation(t.getMessage());
            }
        });
    }

    public void loadInvitation(RetrofitAPIInterface apiInterface, String token){

        Call<InvitationRow> invitationCall = apiInterface.getInvitations("Bearer "+token);
        invitationCall.enqueue(new Callback<InvitationRow>() {
            @Override
            public void onResponse(Call<InvitationRow> call, Response<InvitationRow> response) {
                Log.i(TAG,""+response.body());
                if (response.isSuccessful() && response.code() < 400){
                    KeepRequiredData.getInstance().setInvitations(response.body());
                    mLoadInvitation.onSuccessLoadInvitation(true,"Invitation is Loaded successfully");

                }else {
                    mLoadInvitation.onFailedInvitation(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<InvitationRow> call, Throwable t) {
                mLoadInvitation.onFailedInvitation(t.getMessage());
            }
        });
    }


    public interface OnCreateInvitation {
        void onSuccessCreateInvitation(boolean isCreated,String successMessage);
        void onFailedCreateInvitation(String errorMessage);
    }

    public interface OnLoadInvitation {
        void onSuccessLoadInvitation(boolean isLoaded,String successMessage);
        void onFailedInvitation(String errorMessage);
    }

}
