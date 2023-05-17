package com.example.garbagecollectingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class AdminHomeFragment extends Fragment {
    private View view;
    private LinearLayout linearLayout1, linearLayout2;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        linearLayout1 = view.findViewById(R.id.linearaboutus);
        linearLayout2 = view.findViewById(R.id.linearadddriver);

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),DriverRegisterActivity.class));
            }
        });
        return view;
    }
}