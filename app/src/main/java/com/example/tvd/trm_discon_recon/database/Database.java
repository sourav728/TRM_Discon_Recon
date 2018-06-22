package com.example.tvd.trm_discon_recon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tvd.trm_discon_recon.values.FunctionCall;

import java.io.File;

public class Database {
    private MyHelper mh;
    private SQLiteDatabase sdb;
    private String databasepath = "";
    private String databasefolder = "database";
    private String database_name = "discon_recon.db";
    private File databasefile = null;
    private FunctionCall fcall = new FunctionCall();

    public Database(Context context)
    {
        try {
            databasefile = fcall.filestorepath(databasefolder, database_name);
            fcall.logStatus("Discon Database does not exists!!");
            databasepath = fcall.filepath(databasefolder) + File.separator + database_name;
            mh = new MyHelper(context, databasepath, null, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void open()
    {
        sdb= mh.getWritableDatabase();
    }
    public void close()
    {
        sdb.close();
    }
    public class MyHelper extends SQLiteOpenHelper{

        public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            /*db.execSQL("Create table DISCON(_id integer primary key, ACC_ID TEXT ,ARREARS TEXT,DIS_DATE TEXT,PREVREAD TEXT, " +
                   "CONSUMER_NAME TEXT, ADD1 TEXT, LAT TEXT, LON TEXT, MTR_READ TEXT, FLAG TEXT, MTR_READING, REMARK);");
            db.execSQL("Create table RECON(_id integer primary key, ACC_ID TEXT ,REDATE TEXT,PREVREAD TEXT, " +
                    "CONSUMER_NAME TEXT, ADD1 TEXT, LAT TEXT, LON TEXT, MTR_READ TEXT, FLAG TEXT, MTR_READING, REMARK);");*/
            db.execSQL("Create table DISCON(_id integer primary key, ACC_ID TEXT ,ARREARS TEXT,DIS_DATE TEXT,PREVREAD TEXT, " +
                    "CONSUMER_NAME TEXT, ADD1 TEXT, LAT TEXT, LON TEXT, MTR_READ TEXT, FLAG TEXT, MTR_READING, REMARK, UNIQUE(ACC_ID,DIS_DATE));");
            db.execSQL("Create table RECON(_id integer primary key, ACC_ID TEXT ,REDATE TEXT,PREVREAD TEXT, " +
                    "CONSUMER_NAME TEXT, ADD1 TEXT, LAT TEXT, LON TEXT, MTR_READ TEXT, FLAG TEXT, MTR_READING, REMARK, UNIQUE(ACC_ID,REDATE));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
    public void insert_discon_data(ContentValues cv)
    {
        sdb.insert("DISCON",null,cv);
    }
    public void insert_recon_data(ContentValues cv)
    {
        sdb.insert("RECON",null,cv);
    }
    public void delete_table()
    {
        sdb.execSQL("DELETE FROM DISCON");
        sdb.execSQL("DELETE FROM RECON");
    }
    public Cursor count_details() {
        Cursor c1 = null;
        c1 = sdb.rawQuery("select count(_id)_id from DISCON ", null);
        return c1;
    }

    public Cursor count_details2()
    {
        Cursor c5 = null;
        c5 = sdb.rawQuery("select count(_id)_id from RECON ", null);
        return c5;
    }
    public Cursor get_Discon_Data()
    {
        Cursor c2 = null;
        c2 = sdb.rawQuery("SELECT * FROM DISCON",null);
        return c2;
    }
    public Cursor get_Recon_Data()
    {
        Cursor c3 = null;
        c3 = sdb.rawQuery("SELECT * FROM RECON",null);
        return  c3;
    }
    public Cursor get_Total_Discon_Count()
    {
        return sdb.rawQuery("SELECT COUNT(ACC_ID)COUNT FROM DISCON WHERE FLAG = 'Y'",null);
    }
    public Cursor get_Total_Recon_Count()
    {
        Cursor c4 = null;
        c4 = sdb.rawQuery("SELECT COUNT(ACC_ID)COUNT FROM RECON WHERE FLAG = 'Y'",null);
        return c4;
    }
    public Cursor check_discon_accid()
    {
        Cursor c6 = null;
        c6 = sdb.rawQuery("SELECT * FROM DISCON",null);
        return c6;
    }
    public Cursor check_recon_accid()
    {
        Cursor c7 = null;
        c7 = sdb.rawQuery("SELECT * FROM RECON", null);
        return c7;
    }
    public Cursor update_Discon_Data(int id, String mtr_reading, String remark) {
        return sdb.rawQuery("UPDATE DISCON set MTR_READING = '" + mtr_reading + "' , REMARK = '" + remark + "' , FLAG = 'Y' where _id = '" + id + "'", null);
    }
    public Cursor update_Recon_Data(int id, String mtr_reading, String remark) {
        return sdb.rawQuery("UPDATE RECON set MTR_READING = '" + mtr_reading + "' , REMARK = '" + remark + "' , FLAG = 'Y' where _id = '" + id + "'", null);
    }

}
