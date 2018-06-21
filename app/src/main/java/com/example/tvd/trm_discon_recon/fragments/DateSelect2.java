package com.example.tvd.trm_discon_recon.fragments;


import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.values.FunctionCall;

import static android.content.Context.MODE_PRIVATE;

public class DateSelect2 extends Fragment {

    ImageView date;
    String dd, date1, date2;
    FunctionCall fcall;
    TextView show_date;
    private int day, month, year;
    Button reconnect;

    public DateSelect2() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_select2, container, false);
        show_date = (TextView) view.findViewById(R.id.txt_date);
        fcall = new FunctionCall();
        reconnect = (Button) view.findViewById(R.id.btn_Reconnect);


        date = (ImageView) view.findViewById(R.id.img_fromdate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog1();
            }
        });
        reconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recon_List recon_list = new Recon_List();
                SavePreferences("RECONNECTION_DATE", date1);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, recon_list).addToBackStack(null).commit();
            }
        });

        return view;
    }

    public void DateDialog1() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                dd = (year + "-" + (month + 1) + "-" + dayOfMonth);
                date1 = fcall.Parse_Date3(dd);
                show_date.setText(date1);
            }
        };
        DatePickerDialog dpdialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        //it will show dates upto current date
        dpdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpdialog.show();
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
