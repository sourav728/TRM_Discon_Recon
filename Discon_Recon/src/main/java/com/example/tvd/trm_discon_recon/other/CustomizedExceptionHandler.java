package com.example.tvd.trm_discon_recon.other;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomizedExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultUEH;
    private String localPath;
    private String mrcode;
    public CustomizedExceptionHandler(String localPath,String mrcode) {
        this.localPath = localPath;
        this.mrcode = mrcode;
        //Getting the the default exception handler
        //that's executed when uncaught exception terminates a thread
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread t, Throwable e) {

        //Write a printable representation of this Throwable
        //The StringWriter gives the lock used to synchronize access to this writer.
        final Writer stringBuffSync = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringBuffSync);
        e.printStackTrace(printWriter);
        String stacktrace = stringBuffSync.toString();
        printWriter.close();

        if (localPath != null) {
            writeToFile(stacktrace);
        }

        //Used only to prevent from any code getting executed.
        // Not needed in this example
        defaultUEH.uncaughtException(t, e);
    }

    private void writeToFile(String currentStacktrace) {
        try {

            //Gets the Android external storage directory & Create new folder Crash_Reports
            File dir = new File(Environment.getExternalStorageDirectory(),
                    "Crash_Reports");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();
            String filename = mrcode + "_" + dateFormat.format(date) + ".txt";
            File reportFile = new File(dir, filename);
            FileWriter fileWriter = new FileWriter(reportFile);
            fileWriter.append(currentStacktrace);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            Log.e("ExceptionHandler", e.getMessage());
        }
    }

}
