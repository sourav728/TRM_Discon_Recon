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
            mh = new MyHelper(context, databasepath, null, 1);
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
            db.execSQL("Create table DISCON_RECON(_id integer primary key, ACC_ID TEXT,ARREARS TEXT,DIS_DATE TEXT,PREVREAD TEXT, " +
                   "CONSUMER_NAME TEXT, ADD1 TEXT, LAT TEXT, LON TEXT, MTR_READ TEXT, FLAG TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
    public void insert_discon_data(ContentValues cv)
    {
        sdb.insert("DISCON_RECON",null,cv);
    }
    public void delete_table()
    {
        sdb.execSQL("DELETE FROM DISCON_RECON");
    }
    public Cursor count_details() {
        Cursor c1 = null;
        c1 = sdb.rawQuery("select count(_id)_id from DISCON_RECON ", null);
        return c1;
    }
    public Cursor get_Discon_Data()
    {
        Cursor c2 = null;
        c2 = sdb.rawQuery("SELECT * FROM DISCON_RECON",null);
        return c2;
    }
}
