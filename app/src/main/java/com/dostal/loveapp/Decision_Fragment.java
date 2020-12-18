package com.dostal.loveapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class Decision_Fragment extends Fragment {
    final static String PREF_FIRSTSTART="firstappstart";
    final static String PREFNAME_USER="UserId";
    private String userId;
    private boolean start=false;
    private TextView tvDennis;
    private ImageView imageView;
    private int i=0;
    private Button buttonBackToChat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decision, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final NavController navController=Navigation.findNavController(view);;
        tvDennis=view.findViewById(R.id.dennisbacktext);
        imageView=view.findViewById(R.id.gulliapproved);
        buttonBackToChat=view.findViewById(R.id.buttonBackToChat);
        tvDennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=i+1;
                switch (i){
                    case 1:
                        tvDennis.setText("Selbst Schuld Drück noch mal");
                        break;
                    case 2:
                        tvDennis.setText("Dennis ist ein Spast");
                        imageView.setVisibility(View.VISIBLE);
                        break;

                }


            }
        });
        buttonBackToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Decision_FragmentDirections.actionDecisionFragmentToMainFragment();
                navController.navigate(R.id.action_decision_Fragment_to_main_Fragment);
            }
        });

        if (firstlogin()==true){
            Decision_FragmentDirections.actionDecisionFragmentToStartFragment();
            navController.navigate(R.id.action_decision_Fragment_to_start_Fragment);


        }else{
            if (start==false){
                Decision_FragmentDirections.ActionDecisionFragmentToMainFragment actionDecisionFragmentToMainFragment=Decision_FragmentDirections.actionDecisionFragmentToMainFragment();
                actionDecisionFragmentToMainFragment.setUserIdfromDecision(userId);
                navController.navigate(actionDecisionFragmentToMainFragment);
                start=true;
            }

        }

        super.onViewCreated(view, savedInstanceState);

    }

    private boolean firstlogin() {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (preferences.getBoolean(PREF_FIRSTSTART, true)) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREF_FIRSTSTART, false);
            editor.apply();
            return true;
        } else {
            userId=preferences.getString(PREFNAME_USER,"");
            if (userId.equals("")){
                return true;//TODO eleganterern weg suchen um zu verhindern dass man in den Weiteren Bildschirm ohne Login Kommt
            }
            return false;
        }
    }



}
