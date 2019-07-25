package com.jeeb.mycommunity.authintication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthenticateHelper {

    public static void createUserDb (final Context context, String userEmail, String userPass){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userCollection = db.collection("User");
        User user = new User();
        user.setEmail(userEmail);
        user.setPassword(userPass);
//        Query userQuery = userCollection.orderBy("email").whereEqualTo(userEmail,user.getEmail());
        userCollection.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, "user is created",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Cannot create user",Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void authenticateUser(){

    }
    public static User getUser(final Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
//        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(context, "user is created",Toast.LENGTH_LONG).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Cannot create user",Toast.LENGTH_LONG).show();
//            }
//        });
//        return null;
        ValueEventListener getUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Toast.makeText(context,"Value == "+dataSnapshot.getValue().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Value == "+databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        };
        userRef.addValueEventListener(getUserListener);
        return null;
    }

}
