package com.example.tvd.trm_discon_recon.fragments;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tvd.trm_discon_recon.R;

import com.example.tvd.trm_discon_recon.adapter.Recon_List_Adapter;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter;
import com.example.tvd.trm_discon_recon.database.Database;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECONNECTION_DIALOG;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECON_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECON_LIST_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECON_LIST_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECON_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVER_DATE_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVER_DATE_SUCCESS;


public class Recon_List extends Fragment {
    ProgressDialog progressDialog;
    GetSetValues getsetvalues;
    RecyclerView recyclerview;
    ArrayList<GetSetValues> arraylist;
    ArrayList<GetSetValues> arrayList1;
    ArrayList<GetSetValues> arrayList3;
    SendingData sendingData;
    FunctionCall functionCall;
    private Recon_List_Adapter recon_list_adapter;
    AlertDialog discon_dialog;
    String get_Discon_date = "";
    ProgressDialog pdialog;
    Database database;
    Cursor c1, c2, c3;
    RoleAdapter roleAdapter1;
    String selected_role = "", reconnection_date = "", reading = "", count = "";
    int dialog_position;
    TextView total_count, recon_count, remaining;

    private final Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RECON_LIST_SUCCESS:
                    progressDialog.dismiss();
                    insertReconData();
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    break;
                case RECON_LIST_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Reconnection Data is not available for you!!", Toast.LENGTH_SHORT).show();
                    HomeFragment homeFragment = new HomeFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, homeFragment).commit();
                    break;
                case RECON_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), getsetvalues.getDiscon_acc_id() + "Account Reconnected Successfully..", Toast.LENGTH_SHORT).show();
                    update_db_values();
                    discon_dialog.dismiss();
                    break;
                case RECON_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Reconnection Failure!!", Toast.LENGTH_SHORT).show();
                    discon_dialog.dismiss();
                    break;

                case SERVER_DATE_SUCCESS:
                    Date server_date = functionCall.selectiondate(functionCall.convertdateview(getsetvalues.getServer_date(), "dd", "/"));
                    Log.d("Debug", "Server_date" + server_date);
                    Date selected_date = functionCall.selectiondate(functionCall.convertdateview(functionCall.Parse_Date2(reconnection_date), "dd", "/"));
                    Log.d("Debug", "Got_Selected_date" + selected_date);

                    /**********Current Date is hardcoaded here later i have to pass selected_date in if condition******************/
                    /**************************************************/
                    /*************************************************/

                    Date selected_date1 = functionCall.selectiondate(functionCall.convertdateview(functionCall.Parse_Date2("2018/06/26"), "dd", "/"));
                    Log.d("Debug", "Hardcoaded" + selected_date1);

                    if (server_date.equals(selected_date1)) {
                        Log.d("Debug", "Date Matching..");

                        progressDialog = new ProgressDialog(getActivity(), R.style.MyProgressDialogstyle);
                        progressDialog.setTitle("Connecting To Server");
                        progressDialog.setMessage("Please Wait..");
                        progressDialog.show();
                        SendingData.Recon_List recon_list = sendingData.new Recon_List(mhandler, getsetvalues, arraylist);
                        /*******Below Mrcode and date is hardcoaded*******/
                        recon_list.execute("54003714", reconnection_date);
                    } else {
                        Log.d("Debug", "Date Not Matching..");
                        /*****************Database should be cleared if user enter 30 days more than the system date**************/
                        //database.delete_table();
                    }
                    break;
                case SERVER_DATE_FAILURE:
                    Log.d("Debug", "Server Date Failure!!");
                    break;
            }
            return false;
        }
    });

    public Recon_List() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recon__list, container, false);
        getActivity().setTitle("Reconnection List");
        setHasOptionsMenu(true);
        database = new Database(getActivity());
        database.open();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        reconnection_date = sharedPreferences.getString("RECONNECTION_DATE", "");

        total_count = (TextView) view.findViewById(R.id.txt_total_recon_count);
        recon_count = (TextView) view.findViewById(R.id.txt_recon_count);
        remaining = (TextView) view.findViewById(R.id.txt_remaining);

        pdialog = new ProgressDialog(getActivity());
        sendingData = new SendingData();
        functionCall = new FunctionCall();
        getsetvalues = new GetSetValues();
        recyclerview = (RecyclerView) view.findViewById(R.id.recon_recyclerview);

        arraylist = new ArrayList<>();
        arrayList1 = new ArrayList<>();

        getsetvalues = new GetSetValues();

       /* SendingData.Get_server_date get_server_date = sendingData.new Get_server_date(mhandler, getsetvalues);
        get_server_date.execute();*/

        progressDialog = new ProgressDialog(getActivity(), R.style.MyProgressDialogstyle);
        progressDialog.setTitle("Connecting To Server");
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        SendingData.Recon_List recon_list = sendingData.new Recon_List(mhandler, getsetvalues, arraylist);
        /*******Below Mrcode and date is hardcoaded*******/
        recon_list.execute("54003714", reconnection_date);

        return view;
    }

    public void show_reconnection_dialog(int id, final int position, ArrayList<GetSetValues> arrayList) {
        final AlertDialog alertDialog;
        final GetSetValues getSetValues = arrayList.get(position);
        switch (id) {
            case RECONNECTION_DIALOG:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Reconnection");
                dialog.setCancelable(false);
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.recon_layout, null);
                dialog.setView(view);
                final TextView accno = (TextView) view.findViewById(R.id.txt_account_no);
                final TextView prevread = (TextView) view.findViewById(R.id.txt_prevread);
                final TextView name = (TextView) view.findViewById(R.id.txt_name);
                final TextView address = (TextView) view.findViewById(R.id.txt_address);
                final TextView discon_date = (TextView) view.findViewById(R.id.txt_discon_date);

                final EditText curread = (EditText) view.findViewById(R.id.edit_curread);
                final EditText comments = (EditText) view.findViewById(R.id.edit_comments);
                final Button cancel_button = (Button) view.findViewById(R.id.dialog_negative_btn);
                final Button disconnect_button = (Button) view.findViewById(R.id.dialog_positive_btn);

                final Spinner remark = (Spinner) view.findViewById(R.id.spiner_remark);
                arrayList3 = new ArrayList<>();
                roleAdapter1 = new RoleAdapter(arrayList3, getActivity());
                remark.setAdapter(roleAdapter1);

                discon_dialog = dialog.create();
                discon_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        dialog_position = position + 1;
                        //Setting status spinner
                        for (int i = 0; i < getResources().getStringArray(R.array.remark).length; i++) {
                            GetSetValues getSetValues = new GetSetValues();
                            getSetValues.setRemark(getResources().getStringArray(R.array.remark)[i]);
                            arrayList3.add(getSetValues);
                            roleAdapter1.notifyDataSetChanged();
                        }
                        remark.setSelection(0);

                        remark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                TextView role = (TextView) view.findViewById(R.id.spinner_txt);
                                role.setBackgroundDrawable(null);
                                selected_role = role.getText().toString();
                                //Toast.makeText(getActivity(), "Selected Role" + selected_role, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        //This method is used to check about if user enter any input then only disconnect button will be enabled
                        curread.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                disconnect_button.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                disconnect_button.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                disconnect_button.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
                            }
                        });
                        accno.setText(getSetValues.getRecon_acc_id());
                        prevread.setText(getSetValues.getRecon_prevread());
                        name.setText(getSetValues.getRecon_date());
                        address.setText(getSetValues.getRecon_add1());
                        discon_date.setText(getSetValues.getRecon_date());
                        disconnect_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(curread.getText())) {
                                    if (!selected_role.equals("--SELECT--")) {
                                        reading = curread.getText().toString();
                                        if (Double.parseDouble(getSetValues.getRecon_prevread()) <= Double.parseDouble(reading)) {
                                            progressDialog = new ProgressDialog(getActivity(), R.style.MyProgressDialogstyle);
                                            progressDialog.setTitle("Updating Reconnection");
                                            progressDialog.setMessage("Please Wait..");
                                            progressDialog.show();
                                            SendingData.Reconnect_Update reconnect_update = sendingData.new Reconnect_Update(mhandler, getSetValues);
                                            reconnect_update.execute(getSetValues.getRecon_acc_id(), reconnection_date, reading, selected_role);

                                        } else {
                                            functionCall.setEdittext_error(curread, "Current Reading should be greater than Previous Reading!!");
                                        }
                                    } else
                                        Toast.makeText(getActivity(), "Please Select Remark!!", Toast.LENGTH_SHORT).show();

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

    public void insertReconData() {

        ContentValues cv = new ContentValues();
        try {
            for (int i = 0; i < arraylist.size(); i++) {
                getsetvalues = arraylist.get(i);

                cv.put("ACC_ID", getsetvalues.getAcc_id());
                Log.d("Debug", "ACC_ID" + getsetvalues.getAcc_id());
                cv.put("REDATE", getsetvalues.getRe_date());
                cv.put("PREVREAD", getsetvalues.getPrev_read());
                cv.put("CONSUMER_NAME", getsetvalues.getConsumer_name());
                cv.put("ADD1", getsetvalues.getAdd1());
                cv.put("LAT", getsetvalues.getLati());
                cv.put("LON", getsetvalues.getLongi());
                cv.put("MTR_READ", getsetvalues.getMtr_read());
                cv.put("FLAG", "N");
                cv.put("MTR_READING", "Null");
                cv.put("REMARK", "Null");
                database.insert_recon_data(cv);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        show();
    }

    public void show() {
        recon_list_adapter = new Recon_List_Adapter(getActivity(), arrayList1, Recon_List.this);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(recon_list_adapter);

        Cursor cursor = database.get_Recon_Data();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                GetSetValues getSetValues = new GetSetValues();
                getSetValues.setRecon_id(String.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))));
                getSetValues.setRecon_acc_id(String.valueOf(cursor.getString(cursor.getColumnIndex("ACC_ID"))));
                getSetValues.setRecon_date(String.valueOf(cursor.getString(cursor.getColumnIndex("REDATE"))));
                getSetValues.setRecon_prevread(String.valueOf(cursor.getString(cursor.getColumnIndex("PREVREAD"))));
                getSetValues.setRecon_consumer_name(String.valueOf(cursor.getString(cursor.getColumnIndex("CONSUMER_NAME"))));
                getSetValues.setRecon_add1(String.valueOf(cursor.getString(cursor.getColumnIndex("ADD1"))));
                getSetValues.setRecon_lat(String.valueOf(cursor.getString(cursor.getColumnIndex("LAT"))));
                getSetValues.setRecon_lon(String.valueOf(cursor.getString(cursor.getColumnIndex("LON"))));
                getSetValues.setRecon_mtr_read(String.valueOf(cursor.getString(cursor.getColumnIndex("MTR_READ"))));
                getSetValues.setRecon_flag(String.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

                Log.d("Debug", "Recon ID" + cursor.getInt(cursor.getColumnIndex("_id")));
                Log.d("Debug", "Recon Accid" + cursor.getString(cursor.getColumnIndex("ACC_ID")));
                Log.d("Debug", "Recon Date" + cursor.getString(cursor.getColumnIndex("REDATE")));
                Log.d("Debug", "Recon prevread" + cursor.getString(cursor.getColumnIndex("PREVREAD")));
                Log.d("Debug", "Recon_consumer_name" + cursor.getString(cursor.getColumnIndex("CONSUMER_NAME")));
                Log.d("Debug", "Recon_add1" + cursor.getString(cursor.getColumnIndex("ADD1")));
                Log.d("Debug", "Recon_lat" + cursor.getString(cursor.getColumnIndex("LAT")));
                Log.d("Debug", "Recon_lon" + cursor.getString(cursor.getColumnIndex("LON")));
                Log.d("Debug", "Recon_Mtr_Read" + cursor.getString(cursor.getColumnIndex("MTR_READ")));
                Log.d("Debug", "Recon_flag" + cursor.getString(cursor.getColumnIndex("FLAG")));
                arrayList1.add(getSetValues);
                recon_list_adapter.notifyDataSetChanged();
            }
        }
        c2 = database.count_details2();
        c2.moveToNext();
        count = c2.getString(c2.getColumnIndex("_id"));

        c3 = database.get_Total_Recon_Count();
        c3.moveToNext();
        String reconnection_count = c3.getString(c3.getColumnIndex("COUNT"));
        Log.d("Debug", "Recon_count" + reconnection_count);
        recon_count.setText(reconnection_count);
        total_count.setText(count);
        int remaining_count = Integer.parseInt(count) - Integer.parseInt(reconnection_count);
        remaining.setText(remaining_count + "");
    }

    public void update_db_values() {
        database.update_Recon_Data(dialog_position, reading, selected_role).moveToNext();
        Recon_List recon_list = new Recon_List();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, recon_list).addToBackStack(null).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater mi = getActivity().getMenuInflater();
        mi.inflate(R.menu.search, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        //below line is for searchview hint and hint color
        searchView.setQueryHint(Html.fromHtml("<font color = #212121>" + "Search by Account ID.." + "</font>"));
        search(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recon_list_adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
