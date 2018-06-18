package com.example.tvd.trm_discon_recon.fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tvd.trm_discon_recon.R;


public class HomeFragment extends Fragment {
    Button disconnect;

    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        disconnect = (Button) view.findViewById(R.id.btn_discon);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date_select date_select = new Date_select();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, date_select).addToBackStack(null).commit();
            }
        });


        return view;
    }


}
