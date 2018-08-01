package com.example.tvd.trm_discon_recon.invoke;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.LoginActivity;
import com.example.tvd.trm_discon_recon.MainActivity;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.Date;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.DATE_FROM_SERVER;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DATE_IS_NOT_COMING_SERVER;


public class ChangeDateNotification extends BroadcastReceiver {
    FunctionCall functionCalls;
    GetSetValues getSetValues ;
    SendingData sendingData;
    Context Notification_context;
    LoginActivity loginActivity;
    private static Handler handler = null;
    {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DATE_FROM_SERVER:
                        functionCalls.logStatus("Date Coming from Server..");
                        Date server_date = functionCalls.selectiondate(functionCalls.convertdateview(getSetValues.getServer_date(), "dd", "/"));
                        Log.d("Debug", "Server_date" + server_date);
                        Date selected_date = functionCalls.selectiondate(functionCalls.convertdateview(functionCalls.currentDate(), "dd", "/"));
                        Log.d("Debug", "Selected_date" + selected_date);

                        if (server_date.equals(selected_date))
                        {
                            functionCalls.logStatus("Date is Matching..");
                        }else {
                            functionCalls.logStatus("Date not Matching!!");
                            Toast.makeText(Notification_context, "Please check the date and login again!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Notification_context, LoginActivity.class);
                            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Notification_context.startActivity(intent);

                            //todo it will redirect user to date settings page
                            Notification_context.startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                        }
                        break;
                    case DATE_IS_NOT_COMING_SERVER:
                        functionCalls.logStatus("Date is not Coming from Server!!");
                        break;
                }
            }
        };
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification_context = context;
        loginActivity = new LoginActivity();
        functionCalls = new FunctionCall();
        getSetValues = new GetSetValues();
        sendingData = new SendingData();
        functionCalls.logStatus("Date Checking Notification Current Time: "+functionCalls.currentRecpttime());
        if (functionCalls.checkInternetConnection(context))
        {
            functionCalls.logStatus("Checking date..");
            SendingData.Get_server_date get_server_date = sendingData.new Get_server_date(handler, getSetValues);
            get_server_date.execute();
        }else functionCalls.logStatus("No Internet Connection!!");
    }
    private Context getContext() {
        return this.Notification_context;
    }

}
