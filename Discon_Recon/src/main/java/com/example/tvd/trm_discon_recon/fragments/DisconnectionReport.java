package com.example.tvd.trm_discon_recon.fragments;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.database.Database;
import com.example.tvd.trm_discon_recon.values.FunctionCall;

import static android.content.Context.MODE_PRIVATE;

public class DisconnectionReport extends Fragment {
    Database database;
    FunctionCall fcall;
    String from_date="",to_date="";
    public DisconnectionReport() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disconnection_report, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MY_SHARED_PREF",MODE_PRIVATE);
        from_date = sharedPreferences.getString("DISON_FROM_DATE","");
        to_date = sharedPreferences.getString("DISCON_TO_DATE","");
        Log.d("Debug","DISCONNECTION_FROM_DATE"+from_date);
        Log.d("Debug","DISCONNECTION_TO_DATE"+to_date);

        database = new Database(getActivity());
        database.open();
        getActivity().setTitle("Disconnection Report");
        fcall = new FunctionCall();

        TableLayout stk = (TableLayout) view.findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(getActivity());

        TextView tv0 = new TextView(getActivity());
        tv0.setText(" Diss Date ");
        tv0.setTextColor(Color.RED);
        tv0.setTextSize(15);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(getActivity());
        tv1.setText(" Tot_cnt ");
        tv1.setTextColor(Color.RED);
        tv1.setTextSize(15);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(getActivity());
        tv2.setText(" Tot_amt ");
        tv2.setTextColor(Color.RED);
        tv2.setTextSize(15);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(getActivity());
        tv3.setText(" Dis_cnt ");
        tv3.setTextColor(Color.RED);
        tv3.setTextSize(15);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(getActivity());
        tv4.setText(" Dis Amt ");
        tv4.setTextColor(Color.RED);
        tv4.setTextSize(15);
        tbrow0.addView(tv4);
        stk.addView(tbrow0);

        Cursor c1 = database.get_report(fcall.Parse_Date5(from_date),fcall.Parse_Date5(to_date));
        Log.d("Debug","After Parse discon_from_date"+fcall.Parse_Date5(from_date));
        Log.d("Debug","After Parse discon_to_date"+ fcall.Parse_Date5(to_date));
        if (c1.getCount()>0)
        {
            while (c1.moveToNext())
            {
                String dis_date = String.valueOf(c1.getString(c1.getColumnIndex("DisDate1")));
                String tot_cnt = String.valueOf(c1.getString(c1.getColumnIndex("tot_cnt")));
                String tot_amt = String.valueOf(c1.getString(c1.getColumnIndex("tot_amt")));
                String dis_cnt = String.valueOf(c1.getString(c1.getColumnIndex("Dis_cnt")));
                String dis_amt = String.valueOf(c1.getString(c1.getColumnIndex("Dis_Amt")));


                TableRow tbrow = new TableRow(getActivity());
                TextView t1v = new TextView(getActivity());
                t1v.setText(fcall.Parse_Date4(dis_date));
                t1v.setTextColor(Color.BLACK);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);

                TextView t2v = new TextView(getActivity());
                t2v.setText(tot_cnt);
                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);

                TextView t3v = new TextView(getActivity());
                t3v.setText(tot_amt);
                t3v.setTextColor(Color.BLACK);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);

                TextView t4v = new TextView(getActivity());
                t4v.setText(dis_cnt);
                t4v.setTextColor(Color.BLACK);
                t4v.setGravity(Gravity.CENTER);
                tbrow.addView(t4v);

                TextView t5v = new TextView(getActivity());
                t5v.setText(dis_amt);
                t5v.setTextColor(Color.BLACK);
                t5v.setGravity(Gravity.CENTER);
                tbrow.addView(t5v);

                stk.addView(tbrow);
            }
        }else {
            Toast.makeText(getActivity(), "Disconnection data is not available!!", Toast.LENGTH_SHORT).show();
            HomeFragment homeFragment = new HomeFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, homeFragment).commit();
        }


        return view;
    }

}
