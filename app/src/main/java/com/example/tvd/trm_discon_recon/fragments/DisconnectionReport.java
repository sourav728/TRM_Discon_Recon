package com.example.tvd.trm_discon_recon.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.database.Database;

public class DisconnectionReport extends Fragment {
    TextView text1,text2,text3,text4,text5;
    Database database;
    public DisconnectionReport() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disconnection_report, container, false);
        database = new Database(getActivity());
        database.open();
        text1 = (TextView) view.findViewById(R.id.txt1);
        text2 = (TextView) view.findViewById(R.id.txt2);
        text3 = (TextView) view.findViewById(R.id.txt3);
        text4 = (TextView) view.findViewById(R.id.txt4);
        text5 = (TextView) view.findViewById(R.id.txt5);

        Cursor c1 = database.get_report();
        c1.moveToNext();
        String t1 = String.valueOf(c1.getString(c1.getColumnIndex("DisDate1")));
        String t2 = String.valueOf(c1.getString(c1.getColumnIndex("tot_cnt")));
        String t3 = String.valueOf(c1.getString(c1.getColumnIndex("tot_amt")));
        String t4 = String.valueOf(c1.getString(c1.getColumnIndex("Dis_cnt")));
        String t5 = String.valueOf(c1.getString(c1.getColumnIndex("Dis_Amt")));

        text1.setText(t1);
        text2.setText(t2);
        text3.setText(t3);
        text4.setText(t4);
        text5.setText(t5);
        
        return view;
    }

}
