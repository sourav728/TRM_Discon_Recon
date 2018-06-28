package com.example.tvd.trm_discon_recon.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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

import com.example.tvd.trm_discon_recon.activities.DateSelectActivity5;
import com.example.tvd.trm_discon_recon.activities.DisconnectionEfficiency;
import com.example.tvd.trm_discon_recon.database.Database;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {
    Button disconnect,reconnect, discon_report, recon_report,discon_efficiency,recon_efficiency;
    Database database;
    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().setTitle("Home");
        database = ((MainActivity) getActivity()).get_discon_Database();

        disconnect = (Button) view.findViewById(R.id.btn_discon);
        reconnect = (Button) view.findViewById(R.id.btn_recon);
        discon_report = (Button) view.findViewById(R.id.btn_discon_report);
        recon_report = (Button) view.findViewById(R.id.btn_recon_report);
        discon_efficiency = view.findViewById(R.id.btn_discon_efficiency);
        recon_efficiency = view.findViewById(R.id.btn_recon_efficiency);

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

        discon_efficiency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getActivity(), DateSelectActivity5.class);
              SavePreferences("CLICK","discon_efficiency");
              startActivity(intent);
            }
        });

        recon_efficiency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DateSelectActivity5.class);
                SavePreferences("CLICK","recon_efficiency");
                startActivity(intent);
            }
        });
        return view;
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
