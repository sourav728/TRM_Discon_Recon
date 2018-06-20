package com.example.tvd.trm_discon_recon.fragments;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tvd.trm_discon_recon.MainActivity;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.database.Database;


public class HomeFragment extends Fragment {
    Button disconnect;
    Database database;
    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        database = ((MainActivity) getActivity()).get_discon_Database();

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
