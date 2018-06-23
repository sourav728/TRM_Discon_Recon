package com.example.tvd.trm_discon_recon.fragments;


import android.app.DatePickerDialog;
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

import java.util.Calendar;

public class DateSelect3 extends Fragment {
    TextView from_date,to_date;
    String dd, date1, date2;
    TextView show_date1,show_date2;
    private int day, month, year;
    private Calendar mcalender;
    Button report;
    FunctionCall fcall;
    ImageView fromdate,todate;
    public DateSelect3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_date_select3, container, false);
        from_date = (TextView) view.findViewById(R.id.txt_from_date);
        to_date = (TextView) view.findViewById(R.id.txt_to_dtae);
        report = (Button) view.findViewById(R.id.btn_Report);
        fcall = new FunctionCall();

        mcalender = Calendar.getInstance();
        day = mcalender.get(Calendar.DAY_OF_MONTH);
        year = mcalender.get(Calendar.YEAR);
        month = mcalender.get(Calendar.MONTH);
        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog1();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog2();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                from_date.setText(date1);
            }
        };
        DatePickerDialog dpdialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        //it will show dates upto current date
        dpdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        //below code will set calender min date to 30 days before from system date
        mcalender.add(Calendar.DATE, -30);
        dpdialog.getDatePicker().setMinDate(mcalender.getTimeInMillis());
        dpdialog.show();
    }
    public void DateDialog2() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                dd = (year + "-" + (month + 1) + "-" + dayOfMonth);
                date1 = fcall.Parse_Date3(dd);
                to_date.setText(date1);
            }
        };
        DatePickerDialog dpdialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        //it will show dates upto current date
        dpdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        //below code will set calender min date to 30 days before from system date
        mcalender.add(Calendar.DATE, -30);
        dpdialog.getDatePicker().setMinDate(mcalender.getTimeInMillis());
        dpdialog.show();
    }
}
