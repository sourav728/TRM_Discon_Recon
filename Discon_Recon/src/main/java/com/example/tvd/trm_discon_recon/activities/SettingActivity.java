package com.example.tvd.trm_discon_recon.activities;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_prof_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.example.tvd.trm_discon_recon.MainActivity;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter;
import com.example.tvd.trm_discon_recon.service.BluetoothService;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.ANALOGICS_PRINTER_CONNECTED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.ANALOGICS_PRINTER_DISCONNECTED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.ANALOGICS_PRINTER_PAIRED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.BLUETOOTH_RESULT;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.CONNECTED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCONNECTED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TURNED_OFF;

public class SettingActivity extends AppCompatActivity {
    Spinner printer_spinner;
    ArrayList<GetSetValues> arrayList;
    RoleAdapter roleAdapter;
    String selected_role = "";
    Button save;
    private Toolbar toolbar;
    TextView toolbar_text;


    FunctionCall fcall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        fcall = new FunctionCall();
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("Select Printer");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        printer_spinner = findViewById(R.id.spinner);
        arrayList = new ArrayList<>();
        roleAdapter = new RoleAdapter(arrayList, this);
        printer_spinner.setAdapter(roleAdapter);
        save = findViewById(R.id.btn_save);

        for (int i = 0; i < getResources().getStringArray(R.array.printer).length; i++) {
            GetSetValues getSetValues = new GetSetValues();
            getSetValues.setRemark(getResources().getStringArray(R.array.printer)[i]);
            arrayList.add(getSetValues);
            roleAdapter.notifyDataSetChanged();
        }
        printer_spinner.setSelection(0);

        printer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView role = (TextView) view.findViewById(R.id.spinner_txt);
                role.setBackgroundDrawable(null);
                selected_role = role.getText().toString();
                Toast.makeText(SettingActivity.this, "Selected Role" + selected_role, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}