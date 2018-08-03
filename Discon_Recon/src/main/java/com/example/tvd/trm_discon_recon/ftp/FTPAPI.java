package com.example.tvd.trm_discon_recon.ftp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.example.tvd.trm_discon_recon.values.FunctionCall;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.APK_FILE_DOWNLOADED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.APK_FILE_NOT_FOUND;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.CRASH_REPORT_UPLOAD_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.CRASH_REPORT_UPLOAD_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FTP_HOST;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.FTP_HOST1;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FTP_PASS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FTP_PASS1;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FTP_PORT;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.FTP_PORT1;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FTP_USER;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FTP_USER1;

public class FTPAPI {
    FunctionCall fcall = new FunctionCall();

    public class Download_apk  extends AsyncTask<String, String, String> {
        boolean dwnldCmplt=false, downloadapk=false;
        Handler handler;
        FileOutputStream fos = null;
        String mobilepath = fcall.filepath("ApkFolder") + File.separator;
        String update_version="";

        public Download_apk(Handler handler, String update_version) {
            this.handler = handler;
            this.update_version = update_version;
        }

        @Override
        protected String doInBackground(String... params) {
            fcall.logStatus("Main_Apk 1");
            FTPClient ftp_1 = new FTPClient();
            fcall.logStatus("Main_Apk 2");
            try {
                fcall.logStatus("Main_Apk 3");
                ftp_1.connect(FTP_HOST, FTP_PORT);
                fcall.logStatus("Main_Apk 4");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fcall.logStatus("Main_Apk 5");
                ftp_1.login(FTP_USER, FTP_PASS);
                downloadapk = ftp_1.login(FTP_USER, FTP_PASS);
                fcall.logStatus("Main_Apk 6");
            } catch (FTPConnectionClosedException e) {
                e.printStackTrace();
                try {
                    downloadapk = false;
                    ftp_1.disconnect();
                    //handler.sendEmptyMessage(DOWNLOAD_FILE_DELETE_CONNECTION_ERROR);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (downloadapk) {
                fcall.logStatus("Apk download billing_file true");
                try {
                    fcall.logStatus("Main_Apk 7");
                    ftp_1.setFileType(FTP.BINARY_FILE_TYPE);
                    ftp_1.enterLocalPassiveMode();
                    fcall.logStatus("Main_Apk 8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fcall.logStatus("Main_Apk 9");
                    ftp_1.changeWorkingDirectory("/Android/Apk/");
                    fcall.logStatus("Main_Apk 10");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fcall.logStatus("Main_Apk 11");
                    FTPFile[] ftpFiles = ftp_1.listFiles("/Android/Apk/");
                    fcall.logStatus("Main_Apk 12");
                    int length = ftpFiles.length;
                    fcall.logStatus("Main_Apk 13");
                    fcall.logStatus("Main_Apk_length = " + length);
                    for (int i = 0; i < length; i++) {
                        String namefile = ftpFiles[i].getName();
                        fcall.logStatus("Main_Apk_namefile : " + namefile);
                        boolean isFile = ftpFiles[i].isFile();
                        if (isFile) {
                            fcall.logStatus("Main_Apk_File: " + "Discon_Recon_"+update_version+".apk");
                            if (namefile.equals("Discon_Recon_"+update_version+".apk")) {
                                fcall.logStatus("Main_Apk File found to download");
                                try {
                                    fos = new FileOutputStream(mobilepath + "Discon_Recon_"+update_version+".apk");
                                }
                                catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    dwnldCmplt = ftp_1.retrieveFile("/Android/Apk/" + "Discon_Recon_"+update_version+".apk", fos);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            downloadapk = false;
            try {
                ftp_1.logout();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dwnldCmplt)
                handler.sendEmptyMessage(APK_FILE_DOWNLOADED);
            else handler.sendEmptyMessage(APK_FILE_NOT_FOUND);
        }
    }

    //For Sending App Crash Report To FTP

    @SuppressLint("StaticFieldLeak")
    public class Crash_Report_Upload extends AsyncTask<String, String, String> {
        FileInputStream fis = null;
        boolean result = false;
        Context context;
        Handler handler;
        boolean collection_upload = false;

        public Crash_Report_Upload(Context context, Handler handler) {
            this.context = context;
            this.handler = handler;
        }

        @Override
        protected String doInBackground(String... params) {

            fcall.logStatus("Crash_Report_Upload Started");
            FTPClient client = new FTPClient();
            fcall.logStatus("Crash_Report_Upload 1");
            try {
                fcall.logStatus("Crash_Report_Upload 2");
                client.connect(FTP_HOST, FTP_PORT);
                fcall.logStatus("Crash_Report_Upload 3");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                fcall.logStatus("Crash_Report_Upload 4");
                collection_upload = client.login(FTP_USER, FTP_PASS);
                fcall.logStatus("Crash_Report_Upload 5");
            } catch (FTPConnectionClosedException e) {
                e.printStackTrace();
                try {
                    collection_upload = false;
                    client.disconnect();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (collection_upload) {
                fcall.logStatus("Crash_Report_Upload true");
                try {
                    fcall.logStatus("Crash_Report_Upload 7");
                    client.setFileType(FTP.BINARY_FILE_TYPE);
                    fcall.logStatus("Crash_Report_Upload 8");
                    client.enterLocalPassiveMode();
                    fcall.logStatus("Crash_Report_Upload 9");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    fcall.logStatus("Crash_Report_Upload 10");
                    client.changeWorkingDirectory("/Android/crash_report/");
                    fcall.logStatus("Crash_Report_Upload 11");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {

                     File sdcard = new File(Environment.getExternalStorageDirectory(),"/Crash_Reports");
                     for (File f:sdcard.listFiles())
                     {
                         if (f.isFile())
                         {
                             fcall.logStatus("Crash_Report_Upload 13");
                             String name = f.getName();
                             fcall.logStatus("FileName"+name);
                             fcall.logStatus("Crash_Report_Upload 14");
                             File sdcard2 = new File(Environment.getExternalStorageDirectory(),"/Crash_Reports" + File.separator + name);
                             FileInputStream fis = new FileInputStream(sdcard2);
                             fcall.logStatus("Crash_Report_Upload 15");
                             result = client.storeFile(name,fis);
                             fcall.logStatus("Crash_Report_Upload 16");
                         }
                     }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (result)
                handler.sendEmptyMessage(CRASH_REPORT_UPLOAD_SUCCESS);
            else handler.sendEmptyMessage(CRASH_REPORT_UPLOAD_FAILURE);
            return null;
        }
    }
}
