package com.dostal.loveapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Main_Fragment extends Fragment {
    private EditText editTextTextMessage;
    private Button buttonSend;
    private ArrayList<Messages> messagesArrayList;
    private RecyclerView recyclerView;
    private int listlength;
    final static String PATHMESSAGES = "Messages";
    final static String PATHUSER = "User";
    final static String PREFNAME_USER = "UserId";
    final static String KEY_USERROLE = "role";
    final static String ROLE_ADMIN = "Admin";
    private String userrole="Default";

    final private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messageRef = db.collection(PATHMESSAGES);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        checkforWrite();


        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        buttonSend = view.findViewById(R.id.buttonSend);
        recyclerView = view.findViewById(R.id.recyclerviewMessages);
        editTextTextMessage = view.findViewById(R.id.editTextTextMessage);
        messagesArrayList = new ArrayList<>();
        editTextTextMessage.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editTextTextMessage.setText("");
                return false;
            }
        });

        //simulation();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (userrole.equals("")){
                    checkforWrite();
                }
                if (userrole.equals(ROLE_ADMIN)){
                    sendmessage(view);
                }else{
                    Toast.makeText(getActivity(),"Du darfst keine Nachrichten schicken",Toast.LENGTH_SHORT).show();
                }

            }
        });

        try {
            messageRef.orderBy("counter").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    messagesArrayList.clear();

                    if (error != null) {
                        return;
                    }
                    for (QueryDocumentSnapshot documentSnapshot : value) {
                        Messages messages = documentSnapshot.toObject(Messages.class);
                        String messageContent = messages.getMessage();

                        messagesArrayList.add(new Messages(messageContent));

                    }
                    listlength = messagesArrayList.size();

                    setAdapter();

                }
            });
        } catch (Exception e) {
        }


        super.onViewCreated(view, savedInstanceState);
    }

    private void setAdapter() {

        RecyclerAdapter adapter = new RecyclerAdapter(messagesArrayList);
        adapter.notifyDataSetChanged();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(listlength);
    }


    private void simulation() {
        messagesArrayList.add(new Messages("Morgen wieder eine Hure"));
        messagesArrayList.add(new Messages("Gar kein Bock"));
        messagesArrayList.add(new Messages("Folgt mir auf Insta"));

    }

    private void sendmessage(View view) {

        String stringmessage = editTextTextMessage.getText().toString();
        String userId="";
        if (stringmessage.isEmpty()) {
            return;
        }
        if (stringmessage.equals("Nachricht")) {
            editTextTextMessage.setText("");
            return;
        }
        if (getArguments() != null) {
            Main_FragmentArgs args = Main_FragmentArgs.fromBundle(getArguments());
            userId = args.getUserIdfromDecision();
        }
        Messages newMessage = new Messages(stringmessage, listlength + 1,userId);

        messageRef.add(newMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(), "Hinzugefügt", Toast.LENGTH_SHORT).show();
            }
        });
        editTextTextMessage.setText("");

    }

    // TODO Ey bruder wir müssen noch checken wie wir das mit der Rolle machen viel spaß Zukunfts ich HDGDL
    private void checkforWrite() {

        if (getArguments() != null) {
            Main_FragmentArgs args = Main_FragmentArgs.fromBundle(getArguments());
            String userId = args.getUserIdfromDecision();
            DocumentReference userRef = db.collection(PATHUSER).document(userId);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        userrole = documentSnapshot.getString(KEY_USERROLE);
                        Toast.makeText(getActivity(), userrole, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}
