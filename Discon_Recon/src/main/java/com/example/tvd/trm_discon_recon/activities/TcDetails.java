package com.example.tvd.trm_discon_recon.activities;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.FDR_FETCH_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FDR_UPDATE_FAILURE;


public class TcDetails extends AppCompatActivity {
     Spinner fdr_spinner;
     Button submit;
     SendingData sendingData;
     ArrayList<GetSetValues>arrayList;
     GetSetValues getSetValues;
     String fdr_fetch_subdiv_code="",fdr_fetch_date="";
     RoleAdapter roleAdapter;
     FunctionCall fcall;
    private final Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case FDR_FETCH_SUCCESS:
                    //progressDialog.dismiss();
                    Toast.makeText(TcDetails.this, "Success", Toast.LENGTH_SHORT).show();
                    break;
                case FDR_UPDATE_FAILURE:
                   // progressDialog.dismiss();
                    Toast.makeText(TcDetails.this, "Failure..", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tc_details);

        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF",MODE_PRIVATE);
        fdr_fetch_subdiv_code = sharedPreferences.getString("FDR_FETCH_SUB_DIVCODE","");
        fdr_fetch_date = sharedPreferences.getString("FDR_FETCH_DATE","");

        fcall = new FunctionCall();
        fdr_spinner = findViewById(R.id.spinner);
        submit = findViewById(R.id.btn_Search);

        sendingData = new SendingData();
        getSetValues = new GetSetValues();
        arrayList = new ArrayList<>();

        roleAdapter = new RoleAdapter(arrayList,this);
        fdr_spinner.setAdapter(roleAdapter);

        fdr_spinner.setSelection(0);
        Log.d("Debug","Date_Parse"+fcall.Parse_Date6(fdr_fetch_date));

        SendingData.FDR_Fetch fdr_fetch = sendingData.new FDR_Fetch(mhandler, getSetValues,arrayList,roleAdapter);
        fdr_fetch.execute(fdr_fetch_subdiv_code, fcall.Parse_Date6(fdr_fetch_date));
    }
}
