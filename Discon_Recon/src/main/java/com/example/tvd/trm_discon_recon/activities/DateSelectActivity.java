package com.example.tvd.trm_discon_recon.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.database.Database;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.Calendar;

public class DateSelectActivity extends AppCompatActivity {
    ImageView date;
    String dd, date1;
    FunctionCall fcall;
    TextView show_date;
    private int day, month, year;
    Button disconnect;
    GetSetValues getSetValues;
    Database database;
    String date_selected="";
    private Calendar mcalender;
    private Toolbar toolbar;
    TextView toolbar_text;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_select);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("Select Date");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        date_selected = sharedPreferences.getString("SELECTED_DATE","");

        database = new Database(this);
        database.open();

        fcall = new FunctionCall();
        getSetValues = new GetSetValues();


        mcalender = Calendar.getInstance();
        day = mcalender.get(Calendar.DAY_OF_MONTH);
        year = mcalender.get(Calendar.YEAR);
        month = mcalender.get(Calendar.MONTH);

        show_date = (TextView) findViewById(R.id.txt_date);
        fcall = new FunctionCall();
        disconnect = (Button) findViewById(R.id.btn_disconnect);


        date = (ImageView) findViewById(R.id.img_fromdate);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog1();
            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!show_date.getText().toString().equals(""))
                {
                    SavePreferences("DISCONNECTION_DATE", date1);
                    Intent intent = new Intent(DateSelectActivity.this,DisconListActivity.class);
                    startActivity(intent);
                }else Toast.makeText(DateSelectActivity.this, "Please Select Disconnection Date!!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void DateDialog1()
    {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                dd = (year + "-" + (month + 1) + "-" + dayOfMonth);
                date1 = fcall.Parse_Date3(dd);
                getSetValues.setSelected_discon_date(date1);
                show_date.setText(date1);
            }
        };
        DatePickerDialog dpdialog = new DatePickerDialog(this, listener, year, month, day);
        //it will show dates upto current date
        dpdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        //below code will set calender min date to 30 days before from system date
        mcalender.add(Calendar.DATE, -30);
        dpdialog.getDatePicker().setMinDate(mcalender.getTimeInMillis());
        dpdialog.show();
    }
    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
