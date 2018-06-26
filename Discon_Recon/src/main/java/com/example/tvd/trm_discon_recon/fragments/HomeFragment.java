package com.example.tvd.trm_discon_recon.fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tvd.trm_discon_recon.MainActivity;
import com.example.tvd.trm_discon_recon.R;
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
        database = ((MainActivity) getActivity()).get_discon_Database();

        disconnect = (Button) view.findViewById(R.id.btn_discon);
        reconnect = (Button) view.findViewById(R.id.btn_recon);
        discon_report = (Button) view.findViewById(R.id.btn_discon_report);
        recon_report = (Button) view.findViewById(R.id.btn_recon_report);

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date_select date_select = new Date_select();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, date_select).addToBackStack(null).commit();
            }
        });
        reconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelect2 date_select2 = new DateSelect2();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, date_select2).addToBackStack(null).commit();
            }
        });

        discon_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* DisconnectionReport disconnectionReport = new DisconnectionReport();

                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, disconnectionReport).addToBackStack(null).commit();*/
               DateSelect3 dateSelect3 = new DateSelect3();
               android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
               fragmentTransaction.replace(R.id.content_frame, dateSelect3).addToBackStack(null).commit();
            }
        });

        recon_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelect4 dateSelect4 = new DateSelect4();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, dateSelect4).addToBackStack(null).commit();
            }
        });
        return view;
    }


}
