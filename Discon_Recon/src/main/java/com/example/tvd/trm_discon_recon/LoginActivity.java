package com.example.tvd.trm_discon_recon;


import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.fragments.HomeFragment;
import com.example.tvd.trm_discon_recon.ftp.FTPAPI;
import com.example.tvd.trm_discon_recon.invoke.ApkNotification;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.io.File;
import java.util.Date;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.APK_FILE_DOWNLOADED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.APK_FILE_NOT_FOUND;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.CONNECTION_TIME_OUT;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DLG_APK_UPDATE_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DLG_APK_UPDATE_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVER_DATE_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVER_DATE_SUCCESS;

public class LoginActivity extends AppCompatActivity {
    Button login;
    FunctionCall fcall;
    String getMrcode = "", getpassword = "";
    EditText mrcode, password;
    LayoutInflater inflater;
    View layout;
    ProgressDialog progressdialog;
    GetSetValues getsetvalues;
    SendingData sendingdata;
    FragmentTransaction fragmentTransaction;
    ImageView select_date;
    String dd, date1, date2;
    private int day, month, year;
    EditText selected_date;
    int length;
    FTPAPI ftpapi;
    String cur_version="";
    Context context;
    private final Handler mhandler;

    {
        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case LOGIN_SUCCESS:
                        progressdialog.dismiss();
                        SavePreferences("MRCODE", getsetvalues.getMrcode());
                        SavePreferences("MRNAME", getsetvalues.getMrname());
                        SavePreferences("SUBDIVCODE", getsetvalues.getSubdivcode());
                        SavePreferences("USER_ROLE", getsetvalues.getUser_role());
                        SavePreferences("DEVICE_ID", getsetvalues.getMr_device_id());
                        SavePreferences("SUBDIVNAME", getsetvalues.getMr_subdiv_name());
                        SavePreferences("PASSWORD", getsetvalues.getMrpassword());
                        SavePreferences("APP_VERSION", getsetvalues.getApp_version());
                        start_version_check();
                       /* //call for new app version update
                        SendingData.Login login = sendingdata.new Login();
                        login.execute(getsetvalues.getMrcode(), getsetvalues.getMr_device_id(), getsetvalues.getMrpassword());
                        //end of call for new notification for apk*/

                        if (fcall.compare(cur_version, getsetvalues.getApp_version()))
                        showdialog(DLG_APK_UPDATE_SUCCESS);
                        else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            //Below code is for custom toast message
                            inflater = getLayoutInflater();
                            layout = inflater.inflate(R.layout.toast1,
                                    (ViewGroup) findViewById(R.id.toast_layout));
                            ImageView imageView = (ImageView) layout.findViewById(R.id.image);
                            imageView.setImageResource(R.drawable.tick);
                            TextView textView = (TextView) layout.findViewById(R.id.text);
                            textView.setText("Success");
                            textView.setTextSize(20);
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 0);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        }

                        //end of custom toast coding
                        break;
                    case LOGIN_FAILURE:
                        progressdialog.dismiss();
                        //below code is for custom toast
                        inflater = getLayoutInflater();
                        layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) findViewById(R.id.toast_layout));
                        ImageView imageView1 = (ImageView) layout.findViewById(R.id.image);
                        imageView1.setImageResource(R.drawable.invalid);
                        TextView textView1 = (TextView) layout.findViewById(R.id.text);
                        textView1.setText("Invalid Credentials!!");
                        textView1.setTextSize(20);
                        Toast toast1 = new Toast(getApplicationContext());
                        toast1.setGravity(Gravity.BOTTOM, 0, 0);
                        toast1.setDuration(Toast.LENGTH_SHORT);
                        toast1.setView(layout);
                        toast1.show();
                        //end of custom toast code
                        mrcode.setText("");
                        password.setText("");
                        mrcode.requestFocus();
                        break;

                    case APK_FILE_DOWNLOADED:
                        progressdialog.dismiss();
                        fcall.updateApp(LoginActivity.this, new File(fcall.filepath("ApkFolder") +
                                File.separator + "Discon_Recon_" + getsetvalues.getApp_version() + ".apk"));
                        break;
                    case APK_FILE_NOT_FOUND:
                        progressdialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Apk FIle not FOund!!", Toast.LENGTH_SHORT).show();
                        break;
                    case DLG_APK_UPDATE_FAILURE:
                        Toast.makeText(LoginActivity.this, "Apk Update Failure", Toast.LENGTH_SHORT).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;

        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            cur_version = packageInfo.versionName;
            Log.d("Debug","Got_Current_Version"+cur_version);
            // SavePreferences("CURRENT_VERSION", current_version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF",MODE_PRIVATE);
        /*cur_version = sharedPreferences.getString("CURRENT_VERSION","");
        Log.d("Debug","current_version"+cur_version);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        initialize();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fcall.isInternetOn(LoginActivity.this)) {
                    /*SavePreferences("Selected_Date",date1);
                    Log.d("Debug","Selected Date"+selected_date.getText().toString());*/

                    getMrcode = mrcode.getText().toString();

                    // String DeviceID ="863697039938021";
                    //Device ID for MR
                    //User ID 54003799
                    //String DeviceID ="354016070557564";
                    //Device id for AAO
                    //User ID 10540038
                   // String DeviceID = "866133033048564";
                   /*Code: 54003892
                   Date: 2018/06/13
                   Password: 123123*/
                   //String DeviceID = "352514083875934";
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    String DeviceID = telephonyManager.getDeviceId();
                    Log.d("Debug", "Device ID" + DeviceID);
                    getpassword = password.getText().toString();
                    if (mrcode.getText().length() <= 0) {
                        mrcode.setError("Please Enter MR code!!");
                    } else if (password.getText().length() <= 0) {
                        password.setError("Please Enter password!!");
                    } else {
                        progressdialog = new ProgressDialog(LoginActivity.this, R.style.MyProgressDialogstyle);
                        progressdialog.setTitle("Connecting To Server");
                        progressdialog.setMessage("Please Wait..");
                        progressdialog.show();
                        SendingData.Login login = sendingdata.new Login(mhandler, getsetvalues);
                        login.execute(getMrcode, DeviceID, getpassword);
                    }
                } else
                    Toast.makeText(LoginActivity.this, "Please Connect to Internet!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void initialize() {
        ftpapi = new FTPAPI();
        getsetvalues = new GetSetValues();
        sendingdata = new SendingData();
        login = (Button) findViewById(R.id.login_btn);
        fcall = new FunctionCall();
        mrcode = (EditText) findViewById(R.id.edit_mrcode);
        password = (EditText) findViewById(R.id.edit_password);

    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void start_version_check() {
        fcall.logStatus("Version_receiver Checking..");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ApkNotification.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmRunning) {
            fcall.logStatus("Version_receiver Started..");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (10000), pendingIntent);
        } else fcall.logStatus("Version_receiver Already running..");
    }

    public void showdialog(int id)
    {
        Dialog dialog = null;
        switch (id)
        {
            case DLG_APK_UPDATE_SUCCESS:
                AlertDialog.Builder appupdate = new AlertDialog.Builder(context);
                appupdate.setTitle("App Update");
                appupdate.setCancelable(false);
                appupdate.setMessage("New Version Available to Download");
                appupdate.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fcall.showprogressdialog("Apk Downloading Please Wait...", progressdialog);
                        FTPAPI.Download_apk downloadApk = ftpapi.new Download_apk(mhandler, getsetvalues.getApp_version());
                        downloadApk.execute();
                    }
                });
               /* appupdate.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });*/
                dialog = appupdate.create();
                dialog.show();
                break;
        }
    }

}
