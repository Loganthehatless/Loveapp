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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Main_Fragment extends Fragment {
    private EditText editTextTextMessage;
    private Button buttonSend;
    private ArrayList<Messages> messagesArrayList;
    private ArrayList<Messages> messagesTempArrayList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String userrole = "Default";
    private String userId = "";
    final static String PATHMESSAGES = "Messages";
    final static String PATHUSER = "User";
    final static String PREFNAME_USER = "UserId";
    final static String KEY_USERROLE = "role";
    final static String ROLE_ADMIN = "Admin";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseFirestore updateUserDb = FirebaseFirestore.getInstance();
    private final CollectionReference userRef = db.collection(PATHUSER);
    private ArrayList<User> userarrayList;


    private final CollectionReference messageRef = db.collection(PATHMESSAGES);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        checkforWrite();
        recyclerView = view.findViewById(R.id.recyclerviewMessages);


        return view;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        buttonSend = view.findViewById(R.id.buttonSend);
        editTextTextMessage = view.findViewById(R.id.editTextTextMessage);
        messagesArrayList = new ArrayList<>();
        messagesTempArrayList = new ArrayList<>();

        userarrayList = new ArrayList<>();
        messagesArrayList.clear();
        setAdapter();



        editTextTextMessage.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editTextTextMessage.setText("");
                recyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                return false;
            }
        });


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                while (userrole.equals("")) {
                    checkforWrite();
                }
                if (userrole.equals(ROLE_ADMIN)|| HelperClass.checkOneTime(getUserId())) {
                    sendmessage(view);
                    if (HelperClass.checkOneTime(getUserId()))
                    {
                        HelperClass.updateUser(getActivity(),getUserId(),false,"onetime");
                    }

                } else {
                    Toast.makeText(getActivity(), "Du darfst keine Nachrichten schicken", Toast.LENGTH_SHORT).show();
                }

            }
        });
        buttonSend.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                HelperClass.updateUserloop(true, getActivity(),"onetime");
                //Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();

                return false;
            }
        });


        messageRef.orderBy("counter").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                messagesTempArrayList.clear();
                if (error != null) {
                    return;
                }
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    //Messages messages = documentSnapshot.toObject(Messages.class);
                    messagesTempArrayList.add(documentSnapshot.toObject(Messages.class));


                }
                messagesToRecycler(messagesTempArrayList);
            }

        });


        super.onViewCreated(view, savedInstanceState);
    }



    private void messagesToRecycler(ArrayList<Messages> messagesTempArrayList) {

        String message;
        String id;
        if (messagesTempArrayList.size() == 0) {

            mAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "leer", Toast.LENGTH_SHORT).show();

        }

        for (int i = messagesArrayList.size(); i < messagesTempArrayList.size(); i++) {
            id = messagesTempArrayList.get(i).getSentuserId();
            message = messagesTempArrayList.get(i).getMessage();

            insertMessage(message, id);
        }

    }

    public void insertMessage(String messege, String id) {
        messagesArrayList.add(new Messages(messege, id));
        mAdapter.notifyItemInserted(messagesArrayList.size());
        recyclerView.smoothScrollToPosition(mAdapter.getItemCount());

    }


    private void setAdapter() {

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerAdapter(messagesArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }


    private void sendmessage(View view) {

        String stringmessage = editTextTextMessage.getText().toString();

        if (stringmessage.isEmpty()) {
            return;
        }
        if (stringmessage.equals("Nachricht")) {
            editTextTextMessage.setText("");
            return;
        }
        Messages newMessage = new Messages(stringmessage, messagesTempArrayList.size(), getUserId());



        messageRef.add(newMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //Toast.makeText(getActivity(), "Hinzugefügt", Toast.LENGTH_SHORT).show();
            }
        });
        editTextTextMessage.setText("");

    }

    // TODO Ey bruder wir müssen noch checken wie wir das mit der Rolle machen viel spaß Zukunfts ich HDGDL
    private void checkforWrite() {


        DocumentReference singleuserRef = db.collection("User").document(getUserId());
        singleuserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    userrole = documentSnapshot.getString(KEY_USERROLE);
                    Toast.makeText(getActivity(), userrole, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private String getUserId() {
        if (userId.equals("")) {
            setUserId();
        }
        //Toast.makeText(getActivity(),userId,Toast.LENGTH_LONG).show();
        return userId;
    }

    private void setUserId() {
        String string = "";
        if (getArguments() != null) {
            Main_FragmentArgs args = Main_FragmentArgs.fromBundle(getArguments());
            string = args.getUserIdfromDecision();
        }
        this.userId = string;
    }
}
