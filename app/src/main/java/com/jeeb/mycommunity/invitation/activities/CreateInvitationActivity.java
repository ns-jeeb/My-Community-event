package com.jeeb.mycommunity.invitation.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jeeb.mycommunity.KeepRequiredData;
import com.jeeb.mycommunity.MainActivity;
import com.jeeb.mycommunity.R;
import com.jeeb.mycommunity.RetrofitAPIClient;
import com.jeeb.mycommunity.RetrofitAPIInterface;
import com.jeeb.mycommunity.community.UsersListFragment;
import com.jeeb.mycommunity.databinding.InviationActivityBinding;
import com.jeeb.mycommunity.invitation.Invitation;
import com.jeeb.mycommunity.invitation.InvitationHelper;
import com.jeeb.mycommunity.invitation.fragments.CreateInvitationFragment;
import com.jeeb.mycommunity.invitation.fragments.MeemonyFragment;
import com.jeeb.mycommunity.invitation.fragments.DisplayInvitationFragment;
import com.jeeb.mycommunity.utils.AppUtil;
import com.jeeb.mycommunity.utils.ConstraintValues;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.jeeb.mycommunity.utils.ConstraintValues.NOTIFICATION_ID;

@SuppressLint("RestrictedApi")
public class CreateInvitationActivity extends AppCompatActivity implements InvitationHelper.OnCreateInvitation,
        View.OnClickListener, CreateInvitationFragment.OnFragmentInteractionListener,RadioGroup.OnCheckedChangeListener {

    private InviationActivityBinding mBinding;
    private RetrofitAPIInterface mInterfaceRetrofitClinet;
    private Fragment mFragment;
    private Invitation mInvitation;
    private char mChar= 'f';
    private NotificationManager mNotifyManager;
    private String TAG = "MY_TAG";
    private ArrayList<String>mUserIds;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.inviation_activity);
        mUserIds = new ArrayList<>();
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mInterfaceRetrofitClinet = RetrofitAPIClient.getClient().create(RetrofitAPIInterface.class);
        mBinding.fabSelected.setOnClickListener(this);
        mBinding.fab.setOnClickListener(this);
        mBinding.invitationType.setOnCheckedChangeListener(this);
        mBinding.fab.setVisibility(View.VISIBLE);
        mFragment = getSupportFragmentManager().findFragmentById(R.id.invitation_content);
        if (mFragment == null) {
            mFragment = new CreateInvitationFragment();
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.invitation_content,mFragment);
        transaction.commit();

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

            mBinding.fab.setVisibility(View.VISIBLE);
        switch (i) {
            case R.id.wedding:
                if (!(mFragment instanceof CreateInvitationFragment)){

                    mFragment = new CreateInvitationFragment();
                }
                replaceFragment(mFragment);
                mChar = ((CreateInvitationFragment) mFragment).onWeddingClicked(true);
                break;

            case R.id.mehmoni:
                if (!(mFragment instanceof MeemonyFragment)){
                    mFragment = new MeemonyFragment();
                }

                replaceFragment(mFragment);
                mChar = ((MeemonyFragment) mFragment).onMemoniClicked(true);

                break;
            case R.id.meet_up:
//                mMeetUpListener.onMeetUpClicked(true);
                break;
            default:

                if (!(mFragment instanceof CreateInvitationFragment)){
                    mFragment = new CreateInvitationFragment();
                }
                replaceFragment(mFragment);
                mChar = ((CreateInvitationFragment) mFragment).onWeddingClicked(true);
                break;
        }

    }
    private void replaceFragment(Fragment fragment){
        if(fragment == null) return;

        if (mFragmentManager == null){
            mFragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (fragment instanceof CreateInvitationFragment){
            fragmentTransaction.add(R.id.invitation_content, fragment);
        }else {
            fragmentTransaction.replace(R.id.invitation_content, fragment);
        }

        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
        if(fragment instanceof CreateInvitationFragment){
            clearBackStack();
        }

    }

    public void clearBackStack() {

        if (mFragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = mFragmentManager.getBackStackEntryAt(0);
            mFragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void sendInvitation(boolean shouldSend, final View rootView) {
        if (shouldSend) {
            mBinding.fab.setVisibility(View.VISIBLE);
            mBinding.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        ActivityCompat.requestPermissions(CreateInvitationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                    }
                    String name = KeepRequiredData.getInstance().getCurrentUser().getFName();
                    saveBitmap(rootView);
                }
            });
        }
    }
    UsersListFragment usersDialog;
    public void launchUserListFragment() {
        if (usersDialog == null){
            usersDialog = new UsersListFragment();
        }
        replaceFragment(usersDialog);
        loadInvitation();
        mBinding.fab.setVisibility(View.VISIBLE);

    }

    public void saveBitmap(View view) {
        String filePath = Environment.getExternalStorageDirectory() + File.separator + "Pictures/screenshot.png";
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            screenShot(view).compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            sendMail(filePath);
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2222) {
            Toast.makeText(CreateInvitationActivity.this, "this back." + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    public void sendMail(String path) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(new Uri.Builder().scheme("mailto").build());
//        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ns.jeeb@gmail.com", "sn.jeeb@hotmail.ca"});
        Uri myUri = Uri.parse("file://" + path);
        emailIntent.putExtra(Intent.EXTRA_STREAM, myUri);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Truiton Test Mail");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "This is an autogenerated mail from Truiton's InAppMail app");
        try {
            startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), 2222);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CreateInvitationActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.invitation_content);
        if (count == 0) {
            super.onBackPressed();
            //additional code
        }if (fragment instanceof UsersListFragment){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            mBinding.invitationType.setVisibility(View.VISIBLE);
            mBinding.fabSelected.setVisibility(View.GONE);
            mBinding.fab.setVisibility(View.VISIBLE);
        }else {
            getFragmentManager().popBackStack();
            mBinding.invitationType.setVisibility(View.GONE);
            mBinding.fabSelected.setVisibility(View.VISIBLE);
            mBinding.fab.setVisibility(View.GONE);
        }
    }

    public void sendNotification(){

        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,notifyIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("channel_01", "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("this wedding notification");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setLightColor(Color.RED);

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(CreateInvitationActivity.this, "channel_01");
        notifyBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp).setContentText("this wedding notification").setContentTitle(
                "Wedding").setContentIntent(notifyPendingIntent);
        Notification myNotification = notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID, myNotification);

    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.fab) {
            launchUserListFragment();
            mBinding.fab.setVisibility(View.GONE);
            mBinding.invitationType.setVisibility(View.GONE);
//            sendNotification();
//
//            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                @Override
//                public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                    String msg = "wedding notification is for you";
//                    if (!task.isSuccessful()) {
//                        msg = "wedding notification is failed";
//                        Log.d(TAG, msg);
//                        Toast.makeText(CreateInvitationActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }else {
//                        Toast.makeText(CreateInvitationActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });

        }else if (view == mBinding.fabSelected){
            mBinding.invitationType.setVisibility(View.GONE);

            if (usersDialog != null && usersDialog.getUserIds() != null && usersDialog.getUserIds().size()>0){
                saveInvitation(usersDialog.getUserIds());
            }else {
                Toast.makeText(this, "Please select Gusts", Toast.LENGTH_SHORT).show();
                mBinding.fabSelected.setVisibility(View.GONE);
                mBinding.fab.setVisibility(View.VISIBLE);
            }
        }

    }

    public void saveInvitation(ArrayList<String> ids) {
        InvitationHelper invitationHelper = new InvitationHelper();
        AppUtil.showProgress(true, this, mBinding.createInviationProgress, mBinding.invitationType);
        invitationHelper.setCreateInvitation(this);
        invitationHelper.createInvitation(mInterfaceRetrofitClinet, mInvitation, ids, getIntent().getExtras().getString("token"));

    }
    public void loadInvitation(){
        if (mBinding.wedding.isChecked()){
            mChar = 'w';
        }

        if (mInterfaceRetrofitClinet != null) {
            if (mFragment instanceof CreateInvitationFragment && mChar == 'm') {
                mInvitation = ((MeemonyFragment) mFragment).createMemoniCard();
            } else if (mFragment instanceof CreateInvitationFragment && mChar == 'w') {
                mInvitation = ((CreateInvitationFragment) mFragment).createWeddingCard();

            } else {
                return;
            }
        }
    }

    @Override
    public void onSuccessCreateInvitation(boolean isCreated, String successMessage) {
        AppUtil.showProgress(false,this,mBinding.createInviationProgress,mBinding.invitationType);
        Toast.makeText(this, "invitation is created", Toast.LENGTH_LONG).show();
        sendNotification();
        Intent intent = new Intent();
        intent.putExtra(ConstraintValues.FINISHED_ACTIVITY,true);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void onFailedCreateInvitation(String errorMessage) {
        AppUtil.showProgress(false,this,mBinding.createInviationProgress,mBinding.invitationType);
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

    }
    public Invitation onPreviewInvation(){
        if (mInvitation == null){
            loadInvitation();
        }
        return mInvitation;
    }

    @Override
    public void onFragmentInteraction(String tag, Object data) {
        ArrayList<String> infos = new ArrayList<>();

        if (tag.equals("Wedding")&& mInvitation!= null) {
            mBinding.invitationType.setVisibility(View.GONE);

            mBinding.fabSelected.setVisibility(View.VISIBLE);
            replaceFragment(DisplayInvitationFragment.newInstance(infos));

        }else if (tag.equals("Meemony")){
        }
    }


}