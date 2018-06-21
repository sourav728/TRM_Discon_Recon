package com.example.tvd.trm_discon_recon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.database.Database;
import com.example.tvd.trm_discon_recon.fragments.Discon_List;
import com.example.tvd.trm_discon_recon.fragments.HomeFragment;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;
import java.util.Date;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVER_DATE_SUCCESS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView mrname,mrcode,logout;
    String Mrname="", Mrcode="",selected_date2="";
    FunctionCall fcall;
    GetSetValues getSetValues;
    SendingData sendingData;
    Database database;
    /*private final Handler mhandler;
    {
        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case SERVER_DATE_SUCCESS:
                        Date server_date = fcall.selectiondate(fcall.convertdateview(getSetValues.getServer_date(),"dd","/"));
                        Log.d("Debug","Server_date"+server_date);
                        Date selected_date = fcall.selectiondate(fcall.convertdateview(selected_date2, "dd", "/"));
                        Log.d("Debug","Got_Selected_date"+selected_date);
                        if (server_date.equals(selected_date))
                            Log.d("Debug","Date Matching..");
                        else
                        {
                            Log.d("Debug","Date Not Matching..");
                            database.delete_table();
                        }
                        break;

                }
                super.handleMessage(msg);
            }
        };
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new Database(this);
        database.open();

        replaceFragment(R.id.nav_home);
        fcall = new FunctionCall();
        getSetValues = new GetSetValues();
        sendingData = new SendingData();

       /* SendingData.Get_server_date get_server_date = sendingData.new Get_server_date(mhandler, getSetValues);
        get_server_date.execute();*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF",MODE_PRIVATE);
        Mrname = sharedPreferences.getString("MRNAME","");
        Mrcode = sharedPreferences.getString("MRCODE","");
       // selected_date2 = sharedPreferences.getString("Selected_Date","");
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        mrname = (TextView) view.findViewById(R.id.nav_mrname);
        mrcode = (TextView) view.findViewById(R.id.nav_mrcode);
        mrname.setText(Mrname);
        mrcode.setText(Mrcode);
        NavigationView logout_view = (NavigationView) findViewById(R.id.nav_view2);
        logout_view.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        onNavigationItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment;
        int id = item.getItemId();
        replaceFragment(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void replaceFragment(int id)
    {
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        }else if (id == R.id.nav_logout)
        {
            SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }

    }
    public Database get_discon_Database()
    {
        return this.database;
    }

}
