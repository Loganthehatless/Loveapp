package com.dostal.loveapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Main_Fragment extends Fragment {
    private EditText editTextTextMessage;
    private FloatingActionButton buttonSend;
    private ArrayList<Messages> messagesArrayList;
    private RecyclerView recyclerView;
    final private String path = "Messages";

    final private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messageRef = db.collection(path);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

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
                sendmessage(view);
            }
        });

        messageRef.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
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
                setAdapter();

            }
        });


        super.onViewCreated(view, savedInstanceState);
    }

    private void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(messagesArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    private void simulation() {
        messagesArrayList.add(new Messages("Morgen wieder eine Hure"));
        messagesArrayList.add(new Messages("Gar kein Bock"));
        messagesArrayList.add(new Messages("Folgt mir auf Insta"));

    }

    private void sendmessage(View view) {

        String stringmessage = editTextTextMessage.getText().toString();

        if (stringmessage.isEmpty()) {
            return;
        }
        Messages newMessage = new Messages(stringmessage);
        messageRef.add(newMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(), "Hinzugefügt", Toast.LENGTH_SHORT).show();
            }
        });
        editTextTextMessage.setText("");



    }

}
