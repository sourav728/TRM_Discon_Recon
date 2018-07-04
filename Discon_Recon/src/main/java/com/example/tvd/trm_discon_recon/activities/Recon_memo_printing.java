package com.example.tvd.trm_discon_recon.activities;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_prof_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.example.tvd.trm_discon_recon.MainActivity;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.service.BluetoothService;
import com.example.tvd.trm_discon_recon.values.FunctionCall;

import com.lvrenyang.io.Pos;
import com.ngx.BluetoothPrinter;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;


import static com.example.tvd.trm_discon_recon.values.ConstantValues.PRINT_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.PRINT_SUCCESS;


public class Recon_memo_printing extends AppCompatActivity {
    Bluetooth_Printer_3inch_prof_ThermalAPI api;
    BluetoothPrinter mBtp;
    Pos mPos = BluetoothService.mPos;
    ExecutorService es = BluetoothService.es;
    float yaxis = 0;
    FunctionCall functionCall;
    Button print;
    AnalogicsThermalPrinter conn = DateSelectActivity6.conn;
    private ArrayList<String> res;
    TextView text_subdiv,text_acc_id,text_rrno,text_name,text_address,text_tariff,text_recon_date,text_bill_amt,text_so,text_dr_fee;
    String memo_subdiv="",memo_acc_id="",memo_rrno="",memo_name="",memo_address="",memo_tariff="",memo_recon_date="",memo_bill_amt="",memo_section="",memo_dr_fee="";
    SendingData sendingData;
    private final Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PRINT_SUCCESS:
                    Toast.makeText(Recon_memo_printing.this, "Print Status updated successfully!!", Toast.LENGTH_SHORT).show();
                    break;
                case PRINT_FAILURE:
                    Toast.makeText(Recon_memo_printing.this, "Print Status Updation Failure!!", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recon_memo_printing);
        functionCall = new FunctionCall();
        sendingData = new SendingData();
        text_acc_id = findViewById(R.id.txt_acc_id);
        text_subdiv = findViewById(R.id.txt_subdiv_code);
        text_rrno = findViewById(R.id.txt_rrno);
        text_name = findViewById(R.id.txt_name);
        text_address = findViewById(R.id.txt_address);
        text_tariff = findViewById(R.id.txt_tariff);
        text_recon_date = findViewById(R.id.txt_recon_date);
        text_bill_amt = findViewById(R.id.txt_bill_amt);
        text_so = findViewById(R.id.txt_so);
        text_dr_fee = findViewById(R.id.txt_dr_fee);

        Intent intent = getIntent();

        memo_acc_id = intent.getStringExtra("ACCT_ID");
        memo_rrno = intent.getStringExtra("LEG_RRNO");
        memo_name = intent.getStringExtra("CONSUMER_NAME");
        memo_address = intent.getStringExtra("ADD1");
        memo_tariff = intent.getStringExtra("TARIFF");
        memo_recon_date = intent.getStringExtra("RE_DATE");
        memo_bill_amt = intent.getStringExtra("ARREARS");
        memo_section = intent.getStringExtra("SO");
        memo_dr_fee = intent.getStringExtra("DR_FEE");
        memo_subdiv = intent.getStringExtra("subdivcode");
        Log.d("Debug","Memo_Subdiv"+memo_subdiv);

        text_acc_id.setText(memo_acc_id);
        text_subdiv.setText(memo_subdiv);
        text_rrno.setText(memo_rrno);
        text_name.setText(memo_name);
        text_address.setText(memo_address);
        text_tariff.setText(memo_tariff);
        text_recon_date.setText(functionCall.Parse_Date4(memo_recon_date));
        text_bill_amt.setText(memo_bill_amt);
        text_so.setText(memo_section);
        text_dr_fee.setText(memo_dr_fee);

        res = new ArrayList<>();
        api = new Bluetooth_Printer_3inch_prof_ThermalAPI();
        functionCall = new FunctionCall();
        print = findViewById(R.id.btn_print);

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BluetoothService.printerconnected)
                        es.submit(new TaskPrint(mPos));
                else Toast.makeText(Recon_memo_printing.this, "Please Connect to Printer and proceed!!", Toast.LENGTH_SHORT).show();
                // printngx();
                //printanalogics();
            }
        });
    }


    private class TaskPrint implements Runnable {
        Pos pos;

        private TaskPrint(Pos pos) {
            this.pos = pos;
        }

        @Override
        public void run() {
            final boolean bPrintResult = PrintGpt();
            final boolean bIsOpened = pos.GetIO().IsOpened();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), bPrintResult ? getResources().getString(R.string.printsuccess)
                            : getResources().getString(R.string.printfailed), Toast.LENGTH_SHORT).show();
                    if (bIsOpened)
                    {
                        yaxis = 0;
                        SendingData.Print_Update print_update = sendingData.new Print_Update(mhandler);
                        print_update.execute(memo_acc_id);
                    }
                }
            });
        }

        /*************************************GPT PRINTING*************************/
        public boolean PrintGpt() {
            boolean bPrintResult;
            int pre_normal_text_length = 21;
            pos.POS_FeedLine();
            pos.POS_S_Align(1);
            printdoubleText("HUBLI ELECTRICITY SUPPLY COMPANY LTD");
            printdoubleText("RECONNECTION MEMO");
            printText("");
            pos.POS_S_Align(0);
            printText(functionCall.space("  Sub Division", pre_normal_text_length) + ":" + " " + memo_subdiv);
            printdoubleText(functionCall.space("  Account ID", pre_normal_text_length) + ":" + " " + memo_acc_id);
            printText(functionCall.space("  RR NO", pre_normal_text_length) + ":" + " " + memo_rrno);
            printText(functionCall.space("Receipt No", pre_normal_text_length) + ":" );
            printText(functionCall.space("Date", pre_normal_text_length) + ":" );
            printText(functionCall.space("Amount", pre_normal_text_length) + ":");
            pos.POS_S_Align(1);
            printText("Name and Address");
            pos.POS_S_Align(0);
            printText("  " + memo_name);
            printText("  " + memo_address);
            printdoubleText(functionCall.space("  Tariff", pre_normal_text_length) + ":" + " " + memo_tariff);
            printText(functionCall.space("  Reconnection Date", pre_normal_text_length) + ":" + " " + memo_recon_date);
            printText(functionCall.space("  Bill Amount", pre_normal_text_length) + ":" + " " + memo_bill_amt);
            printText(functionCall.space("  Section", pre_normal_text_length) + ":" + " " + memo_section);
            printdoubleText(functionCall.space("  D & R Fee", pre_normal_text_length) + ":" + " " + memo_dr_fee);
            pos.POS_FeedLine();
            printText("---------------------------------------------");
            printText("  " + "NOTE: Pay bill before due date to avoid");
            printText("  " + "Dis-Reconnection charges.");

            pos.POS_FeedLine();
            pos.POS_FeedLine();
            bPrintResult = pos.GetIO().IsOpened();
            return bPrintResult;
        }

        private void printText(String msg) {
            pos.POS_S_TextOut(msg + "\r\n", 0, 0, 0, 0, 4);
        }

        private void printdoubleText(String msg) {
            pos.POS_S_TextOut(msg + "\r\n", 0, 0, 1, 0, 4);
        }
    }


    //*****************************************Analogics print***************************************************************
    public void printanalogics() {
        StringBuilder stringBuilder = new StringBuilder();
        analogics_header__double_print(functionCall.aligncenter("HUBLI ELECTRICITY SUPPLY COMPANY LTD", 38), 4);
        analogics_header__double_print(functionCall.aligncenter("RECONNECTION MEMO", 38), 4);
        analogicsprint(functionCall.space("", 12), 4);
        analogicsprint(functionCall.space("Sub Division", 12) + ":" + " " + memo_subdiv, 4);
        analogics_double_print(functionCall.space("Account ID", 12) + ":" + " " + memo_acc_id, 4);
        analogicsprint(functionCall.space("RRNO", 12) + ":" + " " + memo_rrno, 4);
        analogics_48_print(functionCall.aligncenter("Name and Address", 48), 6);
        analogics_48_print(memo_name, 3);
        analogics_48_print(memo_address, 3);
        analogicsprint(functionCall.space("Tariff", 12) + ":" + " " + memo_tariff, 4);
        analogicsprint(functionCall.space("Reconnection Date", 12) + ":" + " " + memo_recon_date, 4);
        analogicsprint(functionCall.space("Bill Amount", 12) + ":" + " " + memo_bill_amt, 4);
        analogicsprint(functionCall.space("Section", 12) + ":" + " " + memo_section, 4);
        analogics_double_print(functionCall.space("D&R Fee", 12) + ":" + " " + memo_dr_fee, 4);
        analogicsprint(functionCall.space("", 12), 4);
        stringBuilder.setLength(0);
        stringBuilder.append("\n");
        stringBuilder.append("\n");

        analogics_48_print("-----------------------------------------------", 3);
        analogics_48_print("NOTE: Pay bill before due date to avoid", 3);
        analogics_48_print("Dis-Reconnection charges.", 3);

        stringBuilder.setLength(0);
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        analogicsprint(stringBuilder.toString(), 4);
    }

    public void analogicsprint(String Printdata, int feed_line) {
        conn.printData(api.font_Courier_30_VIP(Printdata));
        text_line_spacing(feed_line);
    }

    public void analogics_double_print(String Printdata, int feed_line) {
        conn.printData(api.font_Double_Height_On_VIP());
        analogicsprint(Printdata, feed_line);
        conn.printData(api.font_Double_Height_Off_VIP());
    }

    public void analogics_header_print(String Printdata, int feed_line) {
        conn.printData(api.font_Courier_38_VIP(Printdata));
        text_line_spacing(feed_line);
    }

    public void analogics_header__double_print(String Printdata, int feed_line) {
        conn.printData(api.font_Double_Height_On_VIP());
        analogics_header_print(Printdata, feed_line);
        conn.printData(api.font_Double_Height_Off_VIP());
    }

    public void analogics_48_print(String Printdata, int feed_line) {
        conn.printData(api.font_Courier_48_VIP(Printdata));
        text_line_spacing(feed_line);
    }

    public void text_line_spacing(int space) {
        conn.printData(api.variable_Size_Line_Feed_VIP(space));
    }
}
