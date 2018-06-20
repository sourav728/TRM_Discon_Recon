package com.example.tvd.trm_discon_recon.invoke;

import android.os.Handler;

import com.example.tvd.trm_discon_recon.adapter.Discon_List_Adapter;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_LIST_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_LIST_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVER_DATE_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVER_DATE_SUCCESS;


public class ReceivingData {
    private FunctionCall functionCall = new FunctionCall();

    private String parseServerXML(String result) {
        String value = "";
        XmlPullParserFactory pullParserFactory;
        InputStream res;
        try {
            res = new ByteArrayInputStream(result.getBytes());
            pullParserFactory = XmlPullParserFactory.newInstance();
            pullParserFactory.setNamespaceAware(true);
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(res, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        switch (name) {
                            case "string":
                                value = parser.nextText();
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    //FOR getting result based on MR LOGIN
    public void getMR_Details(String result, Handler handler, GetSetValues getSetValues) {
        result = parseServerXML(result);
        functionCall.logStatus("MR_Login" + result);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String message = jsonObject.getString("message");
                if (StringUtils.startsWithIgnoreCase(message, "Success!")) {
                    getSetValues.setMrcode(jsonObject.getString("MRCODE"));
                    getSetValues.setMrname(jsonObject.getString("MRNAME"));
                    getSetValues.setSubdivcode(jsonObject.getString("SUBDIVCODE"));
                    handler.sendEmptyMessage(LOGIN_SUCCESS);
                } else handler.sendEmptyMessage(LOGIN_FAILURE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            functionCall.logStatus("JSON Exception Failure!!");
            handler.sendEmptyMessage(LOGIN_FAILURE);
        }
    }

    public void getDiscon_List(String result, Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList) {
        result = parseServerXML(result);
        functionCall.logStatus("DISCON_LIST" + result);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    getSetValues = new GetSetValues();
                    String ACCT_ID = jsonObject.getString("ACCT_ID");
                    String ARREARS = jsonObject.getString("ARREARS");
                    String DIS_DATE = jsonObject.getString("DIS_DATE");
                    String PREVREAD = jsonObject.getString("PREVREAD");
                    String CONSUMER_NAME = jsonObject.getString("CONSUMER_NAME");
                    String ADD1 = jsonObject.getString("ADD1");
                    String LAT = jsonObject.getString("LAT");
                    String LON = jsonObject.getString("LON");
                    String MTR_READ = jsonObject.getString("MTR_READ");

                    if (!ACCT_ID.equals(""))
                        getSetValues.setAcc_id(ACCT_ID);
                    else getSetValues.setAcc_id("NA");
                    if (!ARREARS.equals(""))
                        getSetValues.setArrears(ARREARS);
                    else getSetValues.setArrears("NA");
                    if (!DIS_DATE.equals(""))
                    getSetValues.setDis_date(DIS_DATE);
                    else getSetValues.setDis_date("NA");
                    if (!PREVREAD.equals(""))
                    getSetValues.setPrev_read(PREVREAD);
                    else getSetValues.setPrev_read("NA");
                    if (!CONSUMER_NAME.equals(""))
                    getSetValues.setConsumer_name(CONSUMER_NAME);
                    else getSetValues.setConsumer_name("NA");
                    if (!ADD1.equals(""))
                    getSetValues.setAdd1(ADD1);
                    else getSetValues.setAdd1("NA");
                    if (!LAT.equals(""))
                    getSetValues.setLati(LAT);
                    else getSetValues.setLati("NA");
                    if (!LON.equals(""))
                    getSetValues.setLongi(LON);
                    else getSetValues.setLongi("NA");
                    if (!MTR_READ.equals(""))
                    getSetValues.setMtr_read(MTR_READ);
                    else getSetValues.setMtr_read("NA");
                    arrayList.add(getSetValues);
                    //discon_list_adapter.notifyDataSetChanged();
                }
                handler.sendEmptyMessage(DISCON_LIST_SUCCESS);
            } else handler.sendEmptyMessage(DISCON_LIST_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            functionCall.logStatus("JSON Exception Failure!!");
            handler.sendEmptyMessage(DISCON_LIST_FAILURE);
        }
    }

    public void getDisconnection_update_status(String result, Handler handler) {
        result = parseServerXML(result);
        functionCall.logStatus("Disconnection Update: " + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (StringUtils.startsWithIgnoreCase(jsonObject.getString("message"), "Success"))
                handler.sendEmptyMessage(DISCON_SUCCESS);
            else handler.sendEmptyMessage(DISCON_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(DISCON_FAILURE);
        }
    }

    public void get_Server_Date_status(String result, Handler handler, GetSetValues getSetValues) {
        result = parseServerXML(result);
        functionCall.logStatus("Server Date Status:" + result);
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject != null) {
                    String serverdate = jsonObject.getString("message");
                    getSetValues.setServer_date(serverdate.substring(1, serverdate.length() - 1));
                    handler.sendEmptyMessage(SERVER_DATE_SUCCESS);
                }else handler.sendEmptyMessage(SERVER_DATE_FAILURE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
