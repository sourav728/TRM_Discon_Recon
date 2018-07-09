package com.example.tvd.trm_discon_recon.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.tvd.trm_discon_recon.MainActivity;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.activities.DateSelectActivity;
import com.example.tvd.trm_discon_recon.activities.DateSelectActivity2;
import com.example.tvd.trm_discon_recon.activities.DateSelectActivity3;
import com.example.tvd.trm_discon_recon.activities.DateSelectActivity4;

import com.example.tvd.trm_discon_recon.activities.DateSelectActivity5;
import com.example.tvd.trm_discon_recon.activities.DateSelectActivity6;
import com.example.tvd.trm_discon_recon.activities.DisconnectionEfficiency;
import com.example.tvd.trm_discon_recon.activities.Select_FDR_Details_Activity;
import com.example.tvd.trm_discon_recon.activities.Select_FDR_Fetch_Activity;
import com.example.tvd.trm_discon_recon.activities.TcDetails;
import com.example.tvd.trm_discon_recon.database.Database;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {
   /* Button disconnect,reconnect, discon_report, recon_report,discon_efficiency,recon_efficiency,tc_details;*/
    RelativeLayout disconnect,reconnect,discon_report,recon_report,discon_efficiency,recon_efficiency,feeder_details,tc_details;
    Database database;
    CardView cardView1,cardView2;
    String user_length="";
    Button office;
    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout2, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().setTitle("Home");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MY_SHARED_PREF",MODE_PRIVATE);
        String user_role = sharedPreferences.getString("USER_ROLE","");
        Log.d("Debug","User_Role"+user_role);
        database = ((MainActivity) getActivity()).get_discon_Database();

        disconnect = view.findViewById(R.id.relatibe_discon);
        reconnect = view.findViewById(R.id.relative_recon);
        discon_report = view.findViewById(R.id.relative_dis_rep);
        recon_report = view.findViewById(R.id.relative_re_rep);
        discon_efficiency = view.findViewById(R.id.relative_dis_eff);
        recon_efficiency = view.findViewById(R.id.relative_re_eff);
        feeder_details = view.findViewById(R.id.relative_feeder);
        tc_details = view.findViewById(R.id.relative_tc);
        office = view.findViewById(R.id.btn_office);

        cardView1 = view.findViewById(R.id.card1);
        cardView2 = view.findViewById(R.id.card2);


        if (user_role.equals("MR"))
        {
            cardView1.setVisibility(View.VISIBLE);
            //cardView2.setVisibility(View.VISIBLE);
            office.setVisibility(View.GONE);

        }
        else {
            cardView1.setVisibility(View.GONE);
            //cardView2.setVisibility(View.GONE);
            office.setVisibility(View.VISIBLE);
        }

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

        feeder_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Select_FDR_Details_Activity.class);
                startActivity(intent);
            }
        });
        tc_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), TcDetails.class);
                startActivity(intent);*/
                Intent intent = new Intent(getActivity(), Select_FDR_Fetch_Activity.class);
                startActivity(intent);
            }
        });
        office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DateSelectActivity6.class);
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
