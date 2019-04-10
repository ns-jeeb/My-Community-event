package com.jeeb.mycommunityevents;

import com.jeeb.mycommunityevents.authintication.User;
import com.jeeb.mycommunityevents.community.Community;
import com.jeeb.mycommunityevents.invitation.Invitation;
import com.jeeb.mycommunityevents.invitation.InvitationRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitAPIInterface {

    @Headers("Accept: application/json, Content-Type: application/json")
    @POST("auth")
    Call<User> doAuthenticatedMember(@Header("Authorization") String header, @Body Map<String, String> body);

    @Headers("Accept: application/json")
    @POST("users")
    Call<User>createMember(@Body  Object body);

    @Headers("Accept: application/json")
    @GET("users/me")
    Call<User>getCurrentUser(@Header("Authorization") String token);

    @GET("users")
    @Headers("Content-Type: application/json")
    Call<List<User>> getUsers(@Header("Authorization") String token);

    @Headers("Accept: application/json")
    @POST("communities")
    Call<Community> createCommunity(@Body Map<String, String> body);

    @Headers("Accept: application/json")
    @GET("communities")
    Call<Community>getCommunities(@Header("Authorization") String token);

    @Headers("Accept: application/json")
    @POST("invitations")
    Call<Invitation> createInvitations(@Header("Authorization") String token,@Body Map<String, Object> body);

    @Headers("Accept: application/json")
    @GET("invitations")
    Call<InvitationRow>getInvitations(@Header("Authorization") String token);

}
