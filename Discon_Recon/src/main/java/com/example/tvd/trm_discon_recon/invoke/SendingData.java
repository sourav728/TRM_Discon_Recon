package com.example.tvd.trm_discon_recon.invoke;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.example.tvd.trm_discon_recon.adapter.Feeder_details_Adapter;
import com.example.tvd.trm_discon_recon.adapter.Recon_Memo_Adapter;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter2;
import com.example.tvd.trm_discon_recon.adapter.TCCode_Adapter;
import com.example.tvd.trm_discon_recon.adapter.TcDetailsAdapter;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SendingData {
    private ReceivingData receivingData = new ReceivingData();
    private FunctionCall functionCall = new FunctionCall();
    private Handler handler;
    private String UrlPostConnection(String Post_Url, HashMap<String, String> datamap) throws IOException {
        try
        {
            String response = "";
            URL url = new URL(Post_Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(60000);
            conn.setConnectTimeout(60000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(datamap));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
            return response;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Debug","SERVER TIME OUT");

        }
       return null;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private String UrlGetConnection(String Get_Url) throws IOException {
        String response;
        URL url = new URL(Get_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(60000);
        conn.setConnectTimeout(60000);
        int responseCode=conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            while ((line=br.readLine()) != null) {
                responseBuilder.append(line);
            }
            response = responseBuilder.toString();
        }
        else response="";
        return response;
    }
    //For MR Login
    public class Login extends AsyncTask<String,String,String>
    {
        String response="";
        Handler handler;
        GetSetValues getSetValues;
        public Login(Handler handler,GetSetValues getSetValues)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap();
            datamap.put("MRCode",params[0]);
            datamap.put("DeviceId",params[1]);
            datamap.put("PASSWORD",params[2]);
            functionCall.logStatus("MRCODE "+params[0] + "\n" + "DEVICE ID"+ params[1] + "\n" + "PASSWORD" + params[2]);
            try
            {
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/Service.asmx/MRDetails",datamap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.getMR_Details(result, handler, getSetValues);
        }
    }
    //Disconnection List
    public class Discon_List extends AsyncTask<String,String,String>
    {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues>arrayList;
        public Discon_List(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues>arrayList)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap<>();
            datamap.put("MRCode",params[0]);
            datamap.put("Date",params[1]);
            functionCall.logStatus("Discon_Mrcoe"+params[0]+"\n"+"Discon_Date"+params[1]);
            try {
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/DisConList",datamap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.getDiscon_List(result, handler,getSetValues,arrayList);
        }
    }

    //Disconnection Update
    @SuppressLint("StaticFieldLeak")
    public class Disconnect_Update extends AsyncTask<String, String, String> {
        String response="";
        Handler handler;
        GetSetValues getSetValues;
        public Disconnect_Update(Handler handler,GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("Acc_id", params[0]);
            datamap.put("Dis_Date", params[1]);
            datamap.put("CURREAD", params[2]);
            datamap.put("Remarks",params[3]);
            datamap.put("Comment",params[4]);
            functionCall.logStatus("Acc_id: "+params[0] + "\n" + "Dis_Date: "+params[1] + "\n" + "CURREAD: "+params[2] + "\n" + "Remarks:"+ params[3] + "\n" + "Comment:"+params[4]);
            try {
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/DisConUpdate", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.getDisconnection_update_status(result, handler, getSetValues);
        }
    }

    //Reconnection List
    public class Recon_List extends AsyncTask<String,String,String>
    {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues>arrayList;
        public Recon_List(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues>arrayList)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap<>();
            datamap.put("MRCode",params[0]);
            datamap.put("Date",params[1]);
            functionCall.logStatus("Recon_MrCode"+params[0]+"\n"+"Recon_Date"+params[1]);
            try {
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/ReConList",datamap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.getReconcon_List(result, handler,getSetValues,arrayList);
        }
    }

    //Reconnection Update
    @SuppressLint("StaticFieldLeak")
    public class Reconnect_Update extends AsyncTask<String, String, String> {
        String response="";
        Handler handler;
        GetSetValues getSetValues;
        public Reconnect_Update(Handler handler,GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("Acc_id", params[0]);
            datamap.put("Dis_Date", params[1]);
            datamap.put("CURREAD", params[2]);
            datamap.put("Remarks",params[3]);
            datamap.put("Comment",params[4]);
            functionCall.logStatus("Acc_id: "+params[0] + "\n" + "Dis_Date: "+params[1] + "\n" + "CURREAD: "+params[2] + "\n" + "Remarks:"+ params[3]);
            try {
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/ReConUpdate", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.getReconnectionUpdateStatus(result, handler, getSetValues);
        }
    }
    //Checking Server Date
    public class Get_server_date extends AsyncTask<String,String,String>
    {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues>arrayList;
        public Get_server_date(Handler handler,GetSetValues getSetValues)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                response = UrlGetConnection("http://bc_service2.hescomtrm.com/Service.asmx/systemDate");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_Server_Date_status(result,handler,getSetValues);
            super.onPostExecute(result);
        }
    }
    //Send Feeder Details
    public class SendFeeder_Details extends AsyncTask<String,String,String>{
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues>arrayList;
        Feeder_details_Adapter feeder_details_adapter;
        public SendFeeder_Details(Handler handler, GetSetValues getSetValues,ArrayList<GetSetValues>arrayList,Feeder_details_Adapter feeder_details_adapter)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.feeder_details_adapter = feeder_details_adapter;
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap<>();
            //Send TC Code
            datamap.put("SUB_DIVCODE", params[0]);
            datamap.put("DATE", params[1]);
            try{
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/FDR_DETAILS", datamap);
            }catch (Exception e){
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_Feeder_details(result,handler,getSetValues,arrayList, feeder_details_adapter);
            super.onPostExecute(result);
        }
    }

    //FDR_FR_UPDATE
    public class FDR_Fr_Update extends AsyncTask<String,String,String>{
        String response="";
        Handler handler;
        GetSetValues getSetValues;
        public FDR_Fr_Update(Handler handler, GetSetValues getSetValues)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap<>();
            datamap.put("FDRCODE",params[0]);
            datamap.put("DATE",params[1]);
            datamap.put("FDRFR",params[2]);
            try {
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/FDRFR_Update",datamap);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_fdr_update_status(result,handler,getSetValues);
            super.onPostExecute(result);
        }
    }

    //FDR FETCH
    public class FDR_Fetch extends AsyncTask<String,String,String>{
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues>arrayList;
        RoleAdapter2 roleAdapter;
        public FDR_Fetch(Handler handler,GetSetValues getSetValues,ArrayList<GetSetValues>arrayList,RoleAdapter2 roleAdapter)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.roleAdapter = roleAdapter;
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap<>();
            datamap.put("SUBDIV_CODE",params[0]);
            datamap.put("DATE",params[1]);
            try{
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/FDR_FETCH",datamap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_fdr_fetch(result,handler,getSetValues,arrayList,roleAdapter);
            super.onPostExecute(result);
        }
    }
   //Recon memo details
   public class Recon_Memo_details extends AsyncTask<String,String,String>
   {
        String response ="";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues>arrayList;
        Recon_Memo_Adapter recon_memo_adapter;
        public Recon_Memo_details(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues>arrayList, Recon_Memo_Adapter recon_memo_adapter)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.recon_memo_adapter = recon_memo_adapter;
        }
       @Override
       protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap<>();
            datamap.put("AccountId",params[0]);
            datamap.put("SUBDIVCODE",params[1]);
            try
            {
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/ReConMemo",datamap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
           return response;
       }

       @Override
       protected void onPostExecute(String result) {
           receivingData.get_reconMemo_details(result,handler,getSetValues,arrayList,recon_memo_adapter);
           super.onPostExecute(result);
       }
   }
    //recon memo update
   public class Print_Update extends AsyncTask<String,String,String>
   {
        String response ="";
        Handler handler;
        public Print_Update(Handler handler)
        {
            this.handler = handler;
        }
       @Override
       protected String doInBackground(String... params) {
           HashMap<String,String>datamap = new HashMap<>();
           datamap.put("ACCT_ID",params[0]);
           datamap.put("paid_amount",params[1]);
           datamap.put("rcpt_num",params[2]);
           try{
               response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/ReconMemo_Update",datamap);
           }
           catch (Exception e)
           {
               e.printStackTrace();
           }
           return response;
       }

       @Override
       protected void onPostExecute(String result) {
           receivingData.get_print_update_status(result, handler);
           super.onPostExecute(result);
       }
   }

   public class Send_Tc_details extends AsyncTask<String,String,String>
   {
        String response="";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues>arrayList;
        TcDetailsAdapter tcDetailsAdapter;
        public Send_Tc_details(Handler handler,GetSetValues getSetValues,ArrayList<GetSetValues>arrayList,TcDetailsAdapter tcDetailsAdapter)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.tcDetailsAdapter = tcDetailsAdapter;
        }
       @Override
       protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap<>();
            datamap.put("SUB_DIVCODE",params[0]);
            datamap.put("DATE",params[1]);
            datamap.put("FDCRCODE",params[2]);
            try {
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/TC_DETAILS",datamap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
           return response;
       }

       @Override
       protected void onPostExecute(String result) {
            receivingData.get_tc_details_status(result,handler,getSetValues,arrayList, tcDetailsAdapter);
           super.onPostExecute(result);
       }
   }

   //Tc Details update
   //FDR_FR_UPDATE
   public class TC_Update extends AsyncTask<String,String,String>{
       String response="";
       Handler handler;
       GetSetValues getSetValues;
       public TC_Update(Handler handler, GetSetValues getSetValues)
       {
           this.handler = handler;
           this.getSetValues = getSetValues;
       }
       @Override
       protected String doInBackground(String... params) {
           HashMap<String,String>datamap = new HashMap<>();
           datamap.put("TCCODE",params[0]);
           datamap.put("DATE",params[1]);
           datamap.put("TCFR",params[2]);
           try {
               response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/TCFR_Update",datamap);
           }catch (Exception e)
           {
               e.printStackTrace();
           }
           return response;
       }

       @Override
       protected void onPostExecute(String result) {
           receivingData.get_tc_update_status(result,handler,getSetValues);
           super.onPostExecute(result);
       }
   }

/*    //FDR FETCH Based on TC_DETAILS_MR
    public class FDR_Fetch_Basedon_Tc_details_MR extends AsyncTask<String,String,String>{
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues>arrayList;
        RoleAdapter2 roleAdapter;
        public FDR_Fetch_Basedon_Tc_details_MR(Handler handler,GetSetValues getSetValues,ArrayList<GetSetValues>arrayList,RoleAdapter2 roleAdapter)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.roleAdapter = roleAdapter;
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap<>();
            datamap.put("MRCODE",params[0]);
            datamap.put("DATE",params[1]);
            try{
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/Service.asmx/TC_DETAILS_MR",datamap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_fdr_fetch_basedon_MR(result,handler,getSetValues,arrayList,roleAdapter);
            super.onPostExecute(result);
        }
    }*/

    //For TC Search
    public class Search_Tccode extends AsyncTask<String,String,String> {
        String response="";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues>arrayList;
        TCCode_Adapter tcCode_adapter;

        public Search_Tccode(Handler handler,GetSetValues getSetValues,ArrayList<GetSetValues>arrayList,
                             TCCode_Adapter tcCode_adapter) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.tcCode_adapter = tcCode_adapter;
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap<>();
            datamap.put("MRCODE",params[0]);
            datamap.put("DATE",params[1]);
            try {
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/Service.asmx/TC_DETAILS_MR",datamap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_tc_code(result,handler,getSetValues,arrayList, tcCode_adapter);
            super.onPostExecute(result);
        }
    }
    //Update TC details
    public class Update_Tcdetails extends AsyncTask<String,String,String> {
        String response="";
        Handler handler;
        GetSetValues getSetValues;

        public Update_Tcdetails(Handler handler,GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap<>();
            datamap.put("MRCODE",params[0]);
            datamap.put("TCCODE",params[1]);
            datamap.put("DATE",params[2]);
            datamap.put("TCFR",params[3]);
            try {
                response = UrlPostConnection("http://bc_service2.hescomtrm.com/Service.asmx/TCFR_Update_MR",datamap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_tc_update(result,handler,getSetValues);
            super.onPostExecute(result);
        }
    }

}
