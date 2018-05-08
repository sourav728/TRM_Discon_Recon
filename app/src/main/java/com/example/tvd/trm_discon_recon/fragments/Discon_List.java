package com.example.tvd.trm_discon_recon.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.Discon_List_Adapter;
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
    ArrayList<GetSetValues>arraylist;
    SendingData sendingData;
    FunctionCall functionCall;
    private Discon_List_Adapter discon_list_adapter;
    AlertDialog discon_dialog;
    private final Handler mhandler;
    {
        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case DISCON_LIST_SUCCESS:
                        progressDialog.dismiss();
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
                        fragmentTransaction.replace(R.id.content_frame,discon_list).addToBackStack(null).commit();
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
        View view =  inflater.inflate(R.layout.fragment_discon__list, container, false);
        sendingData = new SendingData();
        functionCall = new FunctionCall();
        getsetvalues = new GetSetValues();
        recyclerview = (RecyclerView) view.findViewById(R.id.discon_recyclerview);
        arraylist = new ArrayList<>();
        getsetvalues = new GetSetValues();

        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setHasFixedSize(true);
        discon_list_adapter = new Discon_List_Adapter(getActivity(), arraylist, getsetvalues, Discon_List.this);
        recyclerview.setAdapter(discon_list_adapter);

        progressDialog = new ProgressDialog(getActivity(), R.style.MyProgressDialogstyle);
        progressDialog.setTitle("Connecting To Server");
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        SendingData.Discon_List discon_list = sendingData.new Discon_List(mhandler, getsetvalues,arraylist,discon_list_adapter);
        /********Below Mrcode and date is hardcoaded********/
        discon_list.execute("14000521","2018-04-27");

        return view;
    }
    public void show_disconnection_dialog(int id, int position, ArrayList<GetSetValues>arrayList)
    {
        final AlertDialog alertDialog;
        final GetSetValues getSetValues = arrayList.get(position);
        switch (id)
        {
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
                                if (s.toString().trim().length() == 0) {
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

}
