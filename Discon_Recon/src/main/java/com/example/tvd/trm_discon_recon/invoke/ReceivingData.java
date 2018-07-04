package com.example.tvd.trm_discon_recon.invoke;

import android.os.Handler;

import com.example.tvd.trm_discon_recon.adapter.Feeder_details_Adapter;
import com.example.tvd.trm_discon_recon.adapter.Recon_Memo_Adapter;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter;
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
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FDR_FETCH_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FDR_FETCH_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FDR_UPDATE_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FDR_UPDATE_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FEEDER_DETAILS_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FEEDER_DETAILS_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.PRINT_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.PRINT_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECON_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECON_LIST_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECON_LIST_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECON_MEMO_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECON_MEMO_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECON_SUCCESS;
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
                    getSetValues.setUser_role(jsonObject.getString("USER_ROLE"));
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

    public void getDisconnection_update_status(String result, Handler handler, GetSetValues getSetValues) {
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
                } else handler.sendEmptyMessage(SERVER_DATE_FAILURE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getReconcon_List(String result, Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList) {
        result = parseServerXML(result);
        functionCall.logStatus("RECON LIST" + result);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    getSetValues = new GetSetValues();
                    String ACCT_ID = jsonObject.getString("ACCT_ID");
                    String RE_DATE = jsonObject.getString("RE_DATE");
                    String PREVREAD = jsonObject.getString("PREVREAD");
                    String CONSUMER_NAME = jsonObject.getString("CONSUMER_NAME");
                    String ADD1 = jsonObject.getString("ADD1");
                    String LAT = jsonObject.getString("LAT");
                    String LON = jsonObject.getString("LON");
                    String MTR_READ = jsonObject.getString("MTR_READ");

                    if (!ACCT_ID.equals(""))
                        getSetValues.setAcc_id(ACCT_ID);
                    else getSetValues.setAcc_id("NA");

                    if (!RE_DATE.equals(""))
                        getSetValues.setRe_date(RE_DATE);

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
                handler.sendEmptyMessage(RECON_LIST_SUCCESS);
            } else handler.sendEmptyMessage(RECON_LIST_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            functionCall.logStatus("JSON Exception Failure!!");
            handler.sendEmptyMessage(RECON_LIST_FAILURE);
        }
    }

    public void getReconnectionUpdateStatus(String result, Handler handler, GetSetValues getSetValues) {
        result = parseServerXML(result);
        functionCall.logStatus("Disconnection Update: " + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (StringUtils.startsWithIgnoreCase(jsonObject.getString("message"), "Success"))
                handler.sendEmptyMessage(RECON_SUCCESS);
            else handler.sendEmptyMessage(RECON_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(RECON_FAILURE);
        }
    }

    //getting Feeder details
    public void get_Feeder_details(String result, Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList, Feeder_details_Adapter feeder_details_adapter) {
        result = parseServerXML(result);
        functionCall.logStatus("Feeder Details:" + result);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    getSetValues = new GetSetValues();
                    String FDRCODE = jsonObject.getString("FDRCODE");
                    String FDRIR = jsonObject.getString("FDRIR");
                    String FDRFR = jsonObject.getString("FDRFR");
                    String FDRMF = jsonObject.getString("FDRMF");
                    if (!FDRCODE.equals(""))
                        getSetValues.setFdr_code(FDRCODE);
                    else getSetValues.setFdr_code("NA");
                    if (!FDRIR.equals(""))
                        getSetValues.setFdr_ir(FDRIR);
                    else getSetValues.setFdr_ir("NA");
                    if (!FDRFR.equals(""))
                        getSetValues.setFdr_fr(FDRFR);
                    else getSetValues.setFdr_mf("NA");
                    arrayList.add(getSetValues);
                    if (!FDRMF.equals(""))
                        getSetValues.setFdr_mf(FDRMF);
                    else getSetValues.setFdr_mf("NA");

                    arrayList.add(getSetValues);
                    feeder_details_adapter.notifyDataSetChanged();
                }
                handler.sendEmptyMessage(FEEDER_DETAILS_SUCCESS);
            } else handler.sendEmptyMessage(FEEDER_DETAILS_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            functionCall.logStatus("JSON Exception Failure!!");
            handler.sendEmptyMessage(FEEDER_DETAILS_FAILURE);
        }
    }

    //getting_fdr_update_status
    public void get_fdr_update_status(String result, Handler handler, GetSetValues getSetValues) {
        result = parseServerXML(result);
        functionCall.logStatus("FDR Update Status: " + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (StringUtils.startsWithIgnoreCase(jsonObject.getString("message"), "Success"))
                handler.sendEmptyMessage(FDR_UPDATE_SUCCESS);
            else handler.sendEmptyMessage(FDR_UPDATE_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(FDR_UPDATE_FAILURE);
        }
    }
    //Get FDR Status
    public void get_fdr_fetch(String result, Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues>arrayList, RoleAdapter roleAdapter)
    {
        result = parseServerXML(result);
        functionCall.logStatus("Feeder Details:"+ result);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    getSetValues = new GetSetValues();
                    String FEEDERCODE = jsonObject.getString("FDRCODE");
                    getSetValues.setFeeder_code(FEEDERCODE);
                    arrayList.add(getSetValues);
                    roleAdapter.notifyDataSetChanged();
                }
                handler.sendEmptyMessage(FDR_FETCH_SUCCESS);
            } else handler.sendEmptyMessage(FDR_FETCH_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            functionCall.logStatus("JSON Exception Failure!!");
            handler.sendEmptyMessage(FDR_FETCH_FAILURE);
        }
    }


    public void get_reconMemo_details(String result, Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList, Recon_Memo_Adapter recon_memo_adapter) {
        result = parseServerXML(result);
        functionCall.logStatus("Recon Memo Details:" + result);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    getSetValues = new GetSetValues();
                    String ACCT_ID = jsonObject.getString("ACCT_ID");
                    String LEG_RRNO = jsonObject.getString("LEG_RRNO");
                    String TARIFF = jsonObject.getString("TARIFF");
                    String SO = jsonObject.getString("SO");
                    String RE_DATE = jsonObject.getString("RE_DATE");
                    String CONSUMER_NAME = jsonObject.getString("CONSUMER_NAME");
                    String ADD1 = jsonObject.getString("ADD1");
                    String subdivcode = jsonObject.getString("subdivcode");
                    String ARREARS = jsonObject.getString("ARREARS");
                    String DR_FEE = jsonObject.getString("DR_FEE");

                    if (!ACCT_ID.equals(""))
                        getSetValues.setRecon_memo_acc_id(ACCT_ID);
                    else getSetValues.setRecon_acc_id("NA");
                    if (!LEG_RRNO.equals(""))
                        getSetValues.setRecon_memo_rrno(LEG_RRNO);
                    else getSetValues.setRecon_memo_rrno("NA");
                    if (!TARIFF.equals(""))
                        getSetValues.setReoon_memo_tariff(TARIFF);
                    else getSetValues.setReoon_memo_tariff("NA");
                    if (!SO.equals(""))
                        getSetValues.setRecon_memo_so(SO);
                    else getSetValues.setRecon_memo_so("NA");

                    if (!RE_DATE.equals(""))
                        getSetValues.setRecon_memo_reconnection_date(RE_DATE);
                    else getSetValues.setRecon_memo_reconnection_date(RE_DATE);
                    if (!CONSUMER_NAME.equals(""))
                        getSetValues.setRecon_memo_customer_name(CONSUMER_NAME);
                    else getSetValues.setRecon_memo_customer_name("NA");
                    if (!ADD1.equals(""))
                        getSetValues.setRecon_memo_add1(ADD1);
                    else getSetValues.setRecon_memo_add1("NA");
                    if (!subdivcode.equals(""))
                        getSetValues.setRecon_memo_subdiv(subdivcode);
                    else getSetValues.setRecon_memo_subdiv("NA");
                    if (!ARREARS.equals(""))
                        getSetValues.setRecon_memo_arrears(ARREARS);
                    else getSetValues.setRecon_memo_arrears("NA");
                    if (!DR_FEE.equals(""))
                        getSetValues.setRecon_memo_dr_fee(DR_FEE);
                    else getSetValues.setRecon_memo_dr_fee("NA");
                    arrayList.add(getSetValues);
                    recon_memo_adapter.notifyDataSetChanged();
                }
                handler.sendEmptyMessage(RECON_MEMO_SUCCESS);
            } else handler.sendEmptyMessage(RECON_MEMO_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            functionCall.logStatus("JSON Exception Failure!!");
            handler.sendEmptyMessage(RECON_MEMO_FAILURE);
        }
    }

    public void get_print_update_status(String result, Handler handler) {
        result = parseServerXML(result);
        functionCall.logStatus("PRINT Update Status: " + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (StringUtils.startsWithIgnoreCase(jsonObject.getString("message"), "Success"))
                handler.sendEmptyMessage(PRINT_SUCCESS);
            else handler.sendEmptyMessage(PRINT_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(PRINT_FAILURE);
        }
    }
}
