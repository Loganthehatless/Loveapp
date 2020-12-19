package com.dostal.loveapp;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HelperClass {
    private FirebaseFirestore getUserDb = FirebaseFirestore.getInstance();
    private final CollectionReference userRef = getUserDb.collection("User");

    private ArrayList<String> userarrayList = new ArrayList<>();

    public ArrayList<String> getUserarrayList() {
        this.userarrayList.clear();
        userRef
                .whereEqualTo("role", "User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            user.setId(document.getId());
                            String useridforUpdate = user.getId();
                            userarrayList.add(useridforUpdate);
                            userarrayList.add("asdasd");
                        }

                    }
                });
        return userarrayList;
    }




    public void updateUser(ArrayList<String> arrayList, String setUpdateRole) {
        FirebaseFirestore updateUserDb = FirebaseFirestore.getInstance();


        for (int i = 0; i < arrayList.size(); i++) {
            DocumentReference updateUser = updateUserDb.collection("User").document(arrayList.get(i));
            updateUser.update("role", setUpdateRole).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });

        }


    }
}
