package com.example.tvd.trm_discon_recon.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.TCCode_Adapter;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_CODE_FOUND;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_CODE_NOTFOUND;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_CODE_NOTUPDATE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_CODE_UPDATE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_DETAILS_UPDATE;


public class TC_Details2 extends AppCompatActivity {
    SendingData sendingData;
    TCCode_Adapter tcCode_adapter;
    GetSetValues getSetValues;
    RecyclerView recyclerview;
    ArrayList<GetSetValues> arraylist;
    String MRCODE = "", DATE = "", cur_reading = "";
    ProgressDialog progressdialog;
    AlertDialog tc_details_update_dialog;
    FunctionCall fcall;
    private Toolbar toolbar;
    TextView toolbar_text;
    private SearchView searchView;
    //************************************************************************************************************
    private final Handler handler;

    {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case TC_CODE_FOUND:
                        progressdialog.dismiss();
                        Toast.makeText(TC_Details2.this, "Success", Toast.LENGTH_SHORT).show();
                        break;
                    case TC_CODE_NOTFOUND:
                        progressdialog.dismiss();
                        Toast.makeText(TC_Details2.this, "Data not Found!!", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case TC_CODE_UPDATE:
                        progressdialog.dismiss();
                        Toast.makeText(TC_Details2.this, "Updated..", Toast.LENGTH_SHORT).show();
                        tc_details_update_dialog.dismiss();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        break;
                    case TC_CODE_NOTUPDATE:
                        progressdialog.dismiss();
                        Toast.makeText(TC_Details2.this, "Update Failure!!", Toast.LENGTH_SHORT).show();
                        tc_details_update_dialog.dismiss();
                        break;

                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tc__details2);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("TC List");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchView = toolbar.findViewById(R.id.search_view);
        searchView.setQueryHint("Enter Tc Code..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tcCode_adapter.getFilter().filter(newText);
                return false;
            }
        });

        fcall = new FunctionCall();
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        MRCODE = sharedPreferences.getString("TCMRCODE", "");
        DATE = sharedPreferences.getString("TCMRDATE", "");

        sendingData = new SendingData();
        recyclerview = (RecyclerView) findViewById(R.id.tccode_recyclerview);
        arraylist = new ArrayList<>();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        progressdialog = new ProgressDialog(TC_Details2.this, R.style.MyProgressDialogstyle);
        progressdialog.setCancelable(false);
        progressdialog.setTitle("Fetching Tc codes");
        progressdialog.setMessage("Please Wait.......");
        progressdialog.show();
        tcCode_adapter = new TCCode_Adapter(this, arraylist, TC_Details2.this);
        recyclerview.setAdapter(tcCode_adapter);

        SendingData.Search_Tccode search_tccode = sendingData.new Search_Tccode(handler, getSetValues,
                arraylist, tcCode_adapter);
        search_tccode.execute(MRCODE, fcall.Parse_Date5(DATE));
    }

    public void show_tc_details_update_dialog2(int id, final int position, ArrayList<GetSetValues> arrayList) {
        final AlertDialog alertDialog;
        final GetSetValues getSetValues = arrayList.get(position);
        switch (id) {
            case TC_DETAILS_UPDATE:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("TC UPDATE");
                dialog.setCancelable(false);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.tc_details_update_layout2, null);
                dialog.setView(view);
                final TextView tc_code = view.findViewById(R.id.txt_tccode);
                final TextView mr_code = view.findViewById(R.id.txt_mrcode);
                final TextView date = view.findViewById(R.id.txt_date);
                final TextView tc_ir = view.findViewById(R.id.txt_ir);

                final EditText current_reading = view.findViewById(R.id.edit_current_reading);
                final Button cancel_button = view.findViewById(R.id.dialog_negative_btn);
                final Button update_button = view.findViewById(R.id.dialog_positive_btn);

                tc_details_update_dialog = dialog.create();
                tc_details_update_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        tc_code.setText(getSetValues.getTc_code());
                        mr_code.setText(MRCODE);
                        date.setText(DATE);
                        tc_ir.setText(getSetValues.getTcir());
                        current_reading.setText(getSetValues.getTcfr());
                       /* current_reading.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                update_button.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                update_button.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                update_button.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
                            }
                        });*/
                        update_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cur_reading = current_reading.getText().toString();
                                if (Double.parseDouble(getSetValues.getTcir()) < Double.parseDouble(cur_reading)) {
                                    progressdialog = new ProgressDialog(TC_Details2.this, R.style.MyProgressDialogstyle);
                                    progressdialog.setTitle("Updating Tc details..");
                                    progressdialog.setMessage("Please Wait..");
                                    progressdialog.show();
                                    SendingData.Update_Tcdetails update_tcdetails = sendingData.new Update_Tcdetails(handler, getSetValues);
                                    update_tcdetails.execute(MRCODE, getSetValues.getTc_code(), fcall.Parse_Date5(DATE), cur_reading);
                                } else
                                    Toast.makeText(TC_Details2.this, "Current reading should be greater than previous reading!!", Toast.LENGTH_SHORT).show();

                            }
                        });

                        cancel_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tc_details_update_dialog.dismiss();
                            }
                        });
                    }
                });
                tc_details_update_dialog.show();
                break;
        }
    }
}
