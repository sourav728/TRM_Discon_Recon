package com.example.tvd.trm_discon_recon.invoke;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.tvd.trm_discon_recon.service.Apk_Update_Service;

public class WakeUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent discon_service = new Intent(context, Apk_Update_Service.class);
        context.startService(discon_service);
    }
}
