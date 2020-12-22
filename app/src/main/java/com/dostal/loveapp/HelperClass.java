package com.dostal.loveapp;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HelperClass {
    private static ArrayList<User> arrayList = new ArrayList<>();
    private final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final static FirebaseFirestore updateUserDb = FirebaseFirestore.getInstance();
    private final static String PATHUSER="User";
    private final static CollectionReference userRef = db.collection(PATHUSER);
    private final static String KEY_ONETIME = "onetime";
    private static boolean status = false;



    public static ArrayList<User> getArrayList() {

        if (arrayList.size()==0){
            fillUserList();
        }
        return arrayList;
    }


    private static void fillUserList()
    {
        arrayList.clear();
        userRef
                .whereEqualTo("role", "User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //User user = document.toObject(User.class);
                            //user.setId(document.getId());
                            //userarrayList.add(user);
                            arrayList.add(document.toObject(User.class).setId(document.getId()));
                            //userarrayList.add(new User(user.getName(),user.getId(),user.isonetime()));
                            //Toast.makeText(getActivity(),user.getName(),Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
    public static void updateUser(Activity activity,String userId,Boolean bool,String fieldName){
        DocumentReference updateUser = updateUserDb.collection(PATHUSER).document(userId);
        updateUser.update(fieldName, bool).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(activity, "yay", Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void updateUserloop(boolean bool,Activity activity,String fieldName){
        if (arrayList.size()==0){
            fillUserList();
        }

        for (int i=0;i<arrayList.size();i++){
            //Toast.makeText(activity,arrayList.get(i).getName()+" "+arrayList.get(i).getId(),Toast.LENGTH_LONG).show();
            updateUser(activity,arrayList.get(i).getId(),bool,fieldName);
        }
    }


    public static Boolean checkOneTime(String userId){

        DocumentReference singleUserRef=db.collection(PATHUSER).document(userId);
        singleUserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    status=documentSnapshot.getBoolean(KEY_ONETIME);
                }
            }
        });



        return status;
    }

}
