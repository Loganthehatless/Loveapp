package com.dostal.loveapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Start_Fragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("User");
    private Spinner spinner;
    private Button buttonConfirmUser;
    private String userid;
    private String user;

    final String prefNameFirstStart="firstappstart";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        //simulation();

        return view;
    }

    private void simulation() {
        User user = new User("Maik", "User", false, false);
        userRef.add(user);
        User user2 = new User("Sandra", "User", false, false);
        userRef.add(user2);
        User user3 = new User("Lukas", "User", false, false);
        userRef.add(user3);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final NavController navController = Navigation.findNavController(view);
        buttonConfirmUser = view.findViewById(R.id.buttonConfirmUser);
        spinner = view.findViewById(R.id.spinnernames);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Bitte Benutzer wählen");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fillSpinner(view);
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = spinner.getSelectedItem().toString();
                userid = getuserId(name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonConfirmUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changestatus(userid);
                final NavController navController = Navigation.findNavController(view);

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    private void changestatus(String getuserId) {
        FirebaseFirestore updateUserDb = FirebaseFirestore.getInstance();
        DocumentReference updateUser = updateUserDb.collection("User").document(getuserId);

        updateUser.update("isclaimed", true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "yay", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getuserId(String name) {
        userRef
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            user.setId(document.getId());
                            userid = user.getId();
                            Toast.makeText(getActivity(), user.getId(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return userid;
    }

    private void fillSpinner(View view) {
        ArrayList<String> usernames;
        usernames = new ArrayList<>();

        spinner = view.findViewById(R.id.spinnernames);
        try {
            userRef.whereEqualTo("isclaimed", false).addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    usernames.clear();
                    if (error != null) {
                        return;
                    }

                    for (QueryDocumentSnapshot documentSnapshot : value) {
                        User user = documentSnapshot.toObject(User.class);
                        String stringname = user.getName();
                        usernames.add(stringname);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, usernames);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinner.setAdapter(arrayAdapter);

                }
            });

        } catch (Exception e) {
        }
    }
    private boolean firstlogin() { //TODO first login checken und davon abhängig machen in welches fragment
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (preferences.getBoolean(prefNameFirstStart, true)) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(prefNameFirstStart, false);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }
}
