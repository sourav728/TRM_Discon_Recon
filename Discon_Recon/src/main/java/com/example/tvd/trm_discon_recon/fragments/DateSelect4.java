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
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.values.FunctionCall;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DateSelect4 extends Fragment {
    TextView from_date, to_date;
    String dd, date1, date2;
    ImageView img_from, img_to;
    TextView show_date1, show_date2;
    private int day, month, year;
    private Calendar mcalender;
    Button report;
    FunctionCall fcall;


    public DateSelect4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_date_select4, container, false);
        from_date = (TextView) view.findViewById(R.id.txt_from_date);
        to_date = (TextView) view.findViewById(R.id.txt_to_dtae);
        report = (Button) view.findViewById(R.id.btn_Report);
        fcall = new FunctionCall();
        img_from = (ImageView) view.findViewById(R.id.img_fromdate);
        img_to = (ImageView) view.findViewById(R.id.img_todate);

        mcalender = Calendar.getInstance();
        day = mcalender.get(Calendar.DAY_OF_MONTH);
        year = mcalender.get(Calendar.YEAR);
        month = mcalender.get(Calendar.MONTH);

        img_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog1();
            }
        });
        img_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog2();
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!from_date.getText().toString().equals(""))
                {
                    if (!to_date.getText().toString().equals(""))
                    {
                        Reconnection_Report reconnection_report = new Reconnection_Report();
                        SavePreferences("RECONNECTION_FROM_DATE", date1);
                        SavePreferences("RECONNECTION_TO_DATE", date2);
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, reconnection_report).addToBackStack(null).commit();
                    }else Toast.makeText(getActivity(), "Please select To Date!!", Toast.LENGTH_SHORT).show();

                }else Toast.makeText(getActivity(), "Please select From Date!!", Toast.LENGTH_SHORT).show();

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
                date2 = fcall.Parse_Date3(dd);
                to_date.setText(date2);
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

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
