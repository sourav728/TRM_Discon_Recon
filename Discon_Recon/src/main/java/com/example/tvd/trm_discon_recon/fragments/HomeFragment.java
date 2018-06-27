package com.example.tvd.trm_discon_recon.fragments;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tvd.trm_discon_recon.MainActivity;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.activities.DateSelectActivity;
import com.example.tvd.trm_discon_recon.activities.DateSelectActivity2;
import com.example.tvd.trm_discon_recon.activities.DateSelectActivity3;
import com.example.tvd.trm_discon_recon.activities.DateSelectActivity4;

import com.example.tvd.trm_discon_recon.database.Database;


public class HomeFragment extends Fragment {
    Button disconnect,reconnect, discon_report, recon_report;
    Database database;
    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Home");
        database = ((MainActivity) getActivity()).get_discon_Database();

        disconnect = (Button) view.findViewById(R.id.btn_discon);
        reconnect = (Button) view.findViewById(R.id.btn_recon);
        discon_report = (Button) view.findViewById(R.id.btn_discon_report);
        recon_report = (Button) view.findViewById(R.id.btn_recon_report);

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),DateSelectActivity.class);
                startActivity(intent);
            }
        });
        reconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(getActivity(), DateSelectActivity2.class);
               startActivity(intent);
            }
        });

        discon_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent intent = new Intent(getActivity(), DateSelectActivity3.class);
              startActivity(intent);
            }
        });

        recon_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(getActivity(), DateSelectActivity4.class);
               startActivity(intent);
            }
        });


        return view;
    }


}
