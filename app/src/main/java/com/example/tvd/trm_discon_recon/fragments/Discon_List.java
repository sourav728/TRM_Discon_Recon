package com.example.tvd.trm_discon_recon.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.MainActivity;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.Discon_List_Adapter;
import com.example.tvd.trm_discon_recon.database.Database;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCONNECTION_DIALOG;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_LIST_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_LIST_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_SUCCESS;

public class Discon_List extends Fragment {
    ProgressDialog progressDialog;
    GetSetValues getsetvalues;
    RecyclerView recyclerview;
    ArrayList<GetSetValues> arraylist;
    ArrayList<GetSetValues> arrayList1;
    SendingData sendingData;
    FunctionCall functionCall;
    private Discon_List_Adapter discon_list_adapter;
    AlertDialog discon_dialog;
    String get_Discon_date = "";
    ProgressDialog pdialog;
    Database database;
    Cursor c1, c2;
    private final Handler mhandler;

    {
        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DISCON_LIST_SUCCESS:
                        progressDialog.dismiss();
                        insertDiscondata();
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        break;
                    case DISCON_LIST_FAILURE:
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Disconnection Data is not available for you!!", Toast.LENGTH_SHORT).show();
                        break;
                    case DISCON_SUCCESS:
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Account Disconnected..", Toast.LENGTH_SHORT).show();
                        Discon_List discon_list = new Discon_List();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, discon_list).addToBackStack(null).commit();
                        break;
                    case DISCON_FAILURE:
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Disconnection Failure!!", Toast.LENGTH_SHORT).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    public Discon_List() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discon__list, container, false);
        //database = ((MainActivity) getActivity()).get_discon_Database();
        database = new Database(getActivity());
        database.open();

        Bundle bundle = getArguments();
        if (bundle != null) {
            get_Discon_date = bundle.getString("Discon_Date");
            Log.d("Debug", "Discon_Date" + get_Discon_date);
        }
        pdialog = new ProgressDialog(getActivity());
        sendingData = new SendingData();
        functionCall = new FunctionCall();
        getsetvalues = new GetSetValues();
        recyclerview = (RecyclerView) view.findViewById(R.id.discon_recyclerview);

        arraylist = new ArrayList<>();
        arrayList1 = new ArrayList<>();

        getsetvalues = new GetSetValues();


        c2 = database.count_details();
        c2.moveToNext();
        String count = c2.getString(c2.getColumnIndex("_id"));
        int count1 = Integer.parseInt(count);
        if (count1 > 0) {
            Log.d("Debug", "Database is not empty so skipping service call..");
            //If database values exists then skipping service call and directly displaying values into recyclerview from local database
            show();
        } else {
            progressDialog = new ProgressDialog(getActivity(), R.style.MyProgressDialogstyle);
            progressDialog.setTitle("Connecting To Server");
            progressDialog.setMessage("Please Wait..");
            progressDialog.show();
            SendingData.Discon_List discon_list = sendingData.new Discon_List(mhandler, getsetvalues, arraylist);
            /********Below Mrcode and date is hardcoaded********/
            discon_list.execute("54003714", get_Discon_date);
        }



         /*Code: 54003717
                   Date: 2018/06/13*/


        return view;
    }

    public void show_disconnection_dialog(int id, int position, ArrayList<GetSetValues> arrayList) {
        final AlertDialog alertDialog;
        final GetSetValues getSetValues = arrayList.get(position);
        switch (id) {
            case DISCONNECTION_DIALOG:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Disconnection");
                dialog.setCancelable(false);
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.discon_layout, null);
                dialog.setView(view);
                final TextView accno = (TextView) view.findViewById(R.id.txt_account_no);
                final TextView arrears = (TextView) view.findViewById(R.id.txt_arrears);
                final TextView prevread = (TextView) view.findViewById(R.id.txt_prevread);
                final EditText curread = (EditText) view.findViewById(R.id.edit_curread);
                final Button cancel_button = (Button) view.findViewById(R.id.dialog_negative_btn);
                final Button disconnect_button = (Button) view.findViewById(R.id.dialog_positive_btn);
                discon_dialog = dialog.create();
                discon_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        //This method is used to check about if user enter any input then only disconnect button will be enabled
                        curread.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                if (s.toString().trim().length() < 1) {
                                    disconnect_button.setEnabled(false);
                                } else {
                                    disconnect_button.setEnabled(true);
                                }
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                        accno.setText(getSetValues.getAcc_id());
                        arrears.setText(String.format("%s %s", getActivity().getResources().getString(R.string.rupee), getSetValues.getArrears()));
                        prevread.setText(getSetValues.getPrev_read());
                        disconnect_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(curread.getText())) {
                                    String reading = curread.getText().toString();
                                    if (Double.parseDouble(getSetValues.getPrev_read()) <= Double.parseDouble(reading)) {
                                        progressDialog = new ProgressDialog(getActivity(), R.style.MyProgressDialogstyle);
                                        progressDialog.setTitle("Updating Disconnection");
                                        progressDialog.setMessage("Please Wait..");
                                        progressDialog.show();
                                        SendingData.Disconnect_Update disconnect_update = sendingData.new Disconnect_Update(mhandler, getSetValues);
                                        disconnect_update.execute(getSetValues.getAcc_id(), functionCall.convertdateview("27-04-2018", "yy", "-"), reading);
                                    } else {
                                        functionCall.setEdittext_error(curread, "Current Reading should be greater than Previous Reading!!");
                                    }
                                } else
                                    functionCall.setEdittext_error(curread, "Enter Current Reading!!");
                            }
                        });

                        cancel_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                discon_dialog.dismiss();
                            }
                        });
                    }
                });
                discon_dialog.show();
                break;
        }
    }

    public void insertDiscondata() {
        Log.d("Debug", "Arraylist" + arraylist.size());
        c1 = database.count_details();
        c1.moveToNext();
        String count = c1.getString(c1.getColumnIndex("_id"));
        int count1 = Integer.parseInt(count);
        if (count1 > 0) {
            Log.d("Debug", "Database is not empty");
        } else {
            ContentValues cv = new ContentValues();
            try {
                for (int i = 0; i < arraylist.size(); i++) {
                    getsetvalues = arraylist.get(i);
                    cv.put("ACC_ID", getsetvalues.getAcc_id());
                    Log.d("Debug", "ACC_ID" + getsetvalues.getAcc_id());
                    cv.put("ARREARS", getsetvalues.getArrears());
                    Log.d("Debug", "ARREARS" + getsetvalues.getArrears());
                    cv.put("DIS_DATE", getsetvalues.getDis_date());
                    cv.put("PREVREAD", getsetvalues.getPrev_read());
                    cv.put("CONSUMER_NAME", getsetvalues.getConsumer_name());
                    cv.put("ADD1", getsetvalues.getAdd1());
                    cv.put("LAT", getsetvalues.getLati());
                    cv.put("LON", getsetvalues.getLongi());
                    cv.put("MTR_READ", getsetvalues.getMtr_read());
                    cv.put("FLAG", "N");
                    database.insert_discon_data(cv);
                }
                //Once value is inserted into database then show() will be called to display values in recycler view
                //It will be called once for first time downloading data
                show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void show() {
        discon_list_adapter = new Discon_List_Adapter(getActivity(), arrayList1, Discon_List.this);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(discon_list_adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        Cursor cursor = database.get_Discon_Data();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                GetSetValues getSetValues = new GetSetValues();
                getSetValues.setDiscon_id(String.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))));
                getSetValues.setDiscon_acc_id(String.valueOf(cursor.getString(cursor.getColumnIndex("ACC_ID"))));
                getSetValues.setDiscon_arrears(String.valueOf(cursor.getString(cursor.getColumnIndex("ARREARS"))));
                getSetValues.setDiscon_date(String.valueOf(cursor.getString(cursor.getColumnIndex("DIS_DATE"))));
                getSetValues.setDiscon_prevread(String.valueOf(cursor.getString(cursor.getColumnIndex("PREVREAD"))));
                getSetValues.setDiscon_consumer_name(String.valueOf(cursor.getString(cursor.getColumnIndex("CONSUMER_NAME"))));
                getSetValues.setDiscon_add1(String.valueOf(cursor.getString(cursor.getColumnIndex("ADD1"))));
                getSetValues.setDiscon_lat(String.valueOf(cursor.getString(cursor.getColumnIndex("LAT"))));
                getSetValues.setDiscon_lon(String.valueOf(cursor.getString(cursor.getColumnIndex("LON"))));
                getSetValues.setDiscon_mtr_read(String.valueOf(cursor.getString(cursor.getColumnIndex("MTR_READ"))));
                getSetValues.setDiscon_flag(String.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

                Log.d("Debug", "Discon ID" + cursor.getInt(cursor.getColumnIndex("_id")));
                Log.d("Debug", "Discon Accid" + cursor.getString(cursor.getColumnIndex("ACC_ID")));
                Log.d("Debug", "Discon Arrears" + cursor.getString(cursor.getColumnIndex("ARREARS")));
                Log.d("Debug", "Discon Date" + cursor.getString(cursor.getColumnIndex("DIS_DATE")));
                Log.d("Debug", "Discon prevread" + cursor.getString(cursor.getColumnIndex("PREVREAD")));
                Log.d("Debug", "Discon_consumer_name" + cursor.getString(cursor.getColumnIndex("CONSUMER_NAME")));
                Log.d("Debug", "Discon_add1" + cursor.getString(cursor.getColumnIndex("ADD1")));
                Log.d("Debug", "Discon_lat" + cursor.getString(cursor.getColumnIndex("LAT")));
                Log.d("Debug", "Discon_lon" + cursor.getString(cursor.getColumnIndex("LON")));
                Log.d("Debug", "Discon_Mtr_Read" + cursor.getString(cursor.getColumnIndex("MTR_READ")));
                Log.d("Debug", "Discon_flag" + cursor.getString(cursor.getColumnIndex("FLAG")));
                arrayList1.add(getSetValues);
                discon_list_adapter.notifyDataSetChanged();
            }
        }
    }

}
