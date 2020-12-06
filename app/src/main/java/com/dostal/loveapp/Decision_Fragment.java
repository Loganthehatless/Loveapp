package com.dostal.loveapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decision, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final NavController navController=Navigation.findNavController(view);;

        if (firstlogin()==true){
            Decision_FragmentDirections.actionDecisionFragmentToStartFragment();
            navController.navigate(R.id.action_decision_Fragment_to_start_Fragment);

        }else{
            Decision_FragmentDirections.ActionDecisionFragmentToMainFragment actionDecisionFragmentToMainFragment=Decision_FragmentDirections.actionDecisionFragmentToMainFragment();
            actionDecisionFragmentToMainFragment.setUserIdfromDecision(userId);
            navController.navigate(actionDecisionFragmentToMainFragment);


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
            userId=preferences.getString(PREFNAME_USER,"hallo");
            Toast.makeText(getActivity(),userId,Toast.LENGTH_LONG).show();
            return false;
        }
    }



}
