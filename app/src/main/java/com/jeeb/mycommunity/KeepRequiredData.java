package com.jeeb.mycommunity;

import com.jeeb.mycommunity.authintication.User;
import com.jeeb.mycommunity.invitation.Invitation;
import com.jeeb.mycommunity.invitation.InvitationRow;

import java.util.ArrayList;

public class KeepRequiredData {

    public  static KeepRequiredData mRequiredData;

    public KeepRequiredData () {
        init();
    }

    private ArrayList<User> mUsers;
    private InvitationRow mInvitations;
    private User mCurrentUser;
    private Invitation mInvitation;

    public InvitationRow getInvitations() {
        return mInvitations;
    }

    public void setInvitations(InvitationRow invitations) {
        mInvitations = invitations;
    }

    public Invitation getInvitation() {
        return mInvitation;
    }

    public void setInvitation(Invitation invitation) {
        mInvitation = invitation;
    }

    public ArrayList<User> getUsers() {
        return mUsers;
    }

    public void setUsers(ArrayList<User> users) {
        mUsers = users;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(User currentUser) {
        mCurrentUser = currentUser;
    }

    public void init(){
        mCurrentUser = new User();
        mUsers = new ArrayList<>();
        mInvitation = new Invitation();
        mInvitations = new InvitationRow();
    }

    public static KeepRequiredData getInstance(){
        if (mRequiredData == null){
            mRequiredData = new KeepRequiredData();
        }
        return mRequiredData;
    }

}
