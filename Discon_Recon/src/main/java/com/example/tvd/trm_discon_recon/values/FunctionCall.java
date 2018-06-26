package com.example.tvd.trm_discon_recon.values;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.widget.EditText;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FunctionCall {
    public void logStatus(String message) {
        Log.d("debug", message);
    }
    /***********CHECKING INTERNET IS ON OR NOT****************/
    public final boolean isInternetOn(Activity activity) {
        ConnectivityManager connect = (ConnectivityManager) activity.getSystemService(activity.getBaseContext().CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connect.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                connect.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    @SuppressLint("DefaultLocale")
    public String convertdateview(String date, String format, String separation) {
        String s1, s2, s3, s4, s5="";
        if (date.length() == 10) {
            s1 = date.substring(0, 2);
            s2 = date.substring(3, 5);
            s3 = date.substring(6, 10);
            if (format.equals("DD") || format.equals("dd")) {
                s5 = s1 + separation + s2 + separation + s3;
            } else {
                s5 = s3 + separation + s2 + separation + s1;
            }
        } else if (date.length() == 9) {
            s4 = date.substring(1, 2);
            try {
                int i1 = Integer.parseInt(s4);
                s1 = date.substring(0, 2);
                s2 = date.substring(3, 4);
                s3 = date.substring(5, 9);
                if (format.equals("DD") || format.equals("dd")) {
                    s5 = s1 + separation + "0"+s2 + separation + s3;
                } else {
                    s5 = s3 + separation + "0"+s2 + separation + s1;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                s1 = date.substring(0, 1);
                s2 = date.substring(2, 4);
                s3 = date.substring(5, 9);
                if (format.equals("DD") || format.equals("dd")) {
                    s5 = "0"+s1 + separation + s2 + separation + s3;
                } else {
                    s5 = s3 + separation + s2 + separation + "0"+s1;
                }
            }
        } else if (date.length() == 8) {
            s1 = date.substring(0, 1);
            s2 = date.substring(2, 3);
            s3 = date.substring(4, 8);
            if (format.equals("DD") || format.equals("dd")) {
                s5 = "0"+s1 + separation + "0"+s2 + separation + s3;
            } else {
                s5 = s3 + separation + "0"+s2 + separation + "0"+s1;
            }
        }
        return s5;
    }
    public void setEdittext_error(EditText editText, String error_msg) {
        editText.setError(error_msg);
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
    }

    public String Parse_Date2(String time) {
        String input = "yyyy/MM/dd";
        String output = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(input, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(output, Locale.getDefault());

        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String Parse_Date3(String time) {
        String input = "yyyy-MM-d";
        String output = "yyyy/MM/dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(input);
        SimpleDateFormat outputFormat = new SimpleDateFormat(output);

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String Parse_Date4(String time) {
        String input = "dd-MM-yyyy hh:mm:ss";
        String output = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(input, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(output, Locale.getDefault());

        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public String Parse_Date5(String time) {
        String input = "yyyy/MM/dd";
        String output = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(input, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(output, Locale.getDefault());

        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public Date selectiondate(String date) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }
    public File filestorepath(String value, String file) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), Appfoldername()
                + File.separator + value);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, File.separator + file);
    }
    private String Appfoldername() {
        return "Discon_Recon";
    }
    public String filepath(String value) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), Appfoldername()
                + File.separator + value);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.toString();
    }
}
