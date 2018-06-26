package com.example.tvd.trm_discon_recon.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.ServiceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_LIST_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_LIST_SUCCESS;

public class Reconnection extends Fragment {
    ProgressDialog progressDialog;
    SendingData sendingData;
    FunctionCall functionCall;
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
                        Toast.makeText(getActivity(), "Reconnection Data is not available for you!!", Toast.LENGTH_SHORT).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }
    public Reconnection() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reconnection, container, false);
        sendingData = new SendingData();
        functionCall = new FunctionCall();
        return view;
    }

}
