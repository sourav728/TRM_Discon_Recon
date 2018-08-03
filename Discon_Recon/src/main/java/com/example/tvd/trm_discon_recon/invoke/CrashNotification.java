package com.example.tvd.trm_discon_recon.invoke;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.ftp.FTPAPI;
import com.example.tvd.trm_discon_recon.values.FunctionCall;

import org.apache.commons.io.FileUtils;

import java.io.File;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.CRASH_REPORT_UPLOAD_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.CRASH_REPORT_UPLOAD_SUCCESS;

public class CrashNotification extends BroadcastReceiver {
    FunctionCall functionCalls;
    Context crash_context;
    FTPAPI ftpapi;
    boolean deleted = false;
    private static Handler handler = null;
    {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CRASH_REPORT_UPLOAD_SUCCESS:
                        Toast.makeText(crash_context, "Crash Report Uploaded Success..", Toast.LENGTH_SHORT).show();
                        try
                        {
                            File sdcard = new File(Environment.getExternalStorageDirectory(),"/Crash_Reports");
                            if (sdcard.exists())
                            {
                                FileUtils.forceDelete(sdcard);
                                functionCalls.logStatus("Crash_Reports Folder deleted..");
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        break;
                    case CRASH_REPORT_UPLOAD_FAILURE:
                        Toast.makeText(crash_context, "Crash Report Upload Failure!!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        functionCalls = new FunctionCall();
        crash_context = context;
        ftpapi = new FTPAPI();
        functionCalls.logStatus("Crash Report Notification Current Time: "+functionCalls.currentRecpttime());
        if (functionCalls.checkInternetConnection(context))
        {
            FTPAPI.Crash_Report_Upload crash_report_upload = ftpapi.new Crash_Report_Upload(crash_context,handler);
            crash_report_upload.execute();

        }else functionCalls.logStatus("No Internet Connection!!");
    }
}
