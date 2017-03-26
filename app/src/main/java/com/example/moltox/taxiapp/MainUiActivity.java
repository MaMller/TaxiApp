package com.example.moltox.taxiapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class MainUiActivity extends AppCompatActivity
        implements
        MainUiFragment.OnButtonClickedListener {

    public static final String TAG_ORDERACTIVITYFRAGMENT = "TAG_ORDERACTIVITY";
    private static final String TAG = "vOut: MainUiActivity";
    public static final String MAINUIFRAGMENT_TAG = "ID00";
    private static final String ONTIMEORDERFRAG_TAG = "ID20";
    private static final String DIRECTORDERFRAG_TAG = "ID10";
    private static final String DEBUGFRAG_TAG = "ID30";
    public static final String SETTINGSFRAG_TAG = "ID30";
    private ActionBar ab;

    GetSettings getSettings;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        context = this;
        Toolbar mToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolBar);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
        setTitle(R.string.title_activity_mainUI);
        Log.v(TAG, "vOut: setTitle set to: " + getTitle());

        if (savedInstanceState == null) {
            MainUiFragment mainUiFragment = new MainUiFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.MainFragmentContainer, mainUiFragment, TAG_ORDERACTIVITYFRAGMENT);
            transaction.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSettingsFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettingsFragment() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onButtonClicked(int button, View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (v.getId()) {
            case R.id.ib_taxi_toOrderActivity:
            case R.id.ib_arrowRight_toOrderActivity:
            case R.id.rl_cc_outerrl:
                Log.v(TAG, "Order Taxi Field pressed");
                OrderActivityFragment orderActivityFragment = new OrderActivityFragment();
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setDisplayShowHomeEnabled(true);
                ab.setTitle(R.string.title_activity_order);
                transaction.replace(R.id.MainFragmentContainer,orderActivityFragment,DIRECTORDERFRAG_TAG);
                break;
            case R.id.ib_arrowRight_toOnTimeOrderActivity:
            case R.id.rl_cc_outerrl2:
                DebugFragment debugFragment = new DebugFragment();
                transaction.replace(R.id.MainFragmentContainer, debugFragment, ONTIMEORDERFRAG_TAG);
                Log.v(TAG, "Debug Page Field Pressed");
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static Bundle jsonToBundle(JSONObject jsonObject) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator iter = jsonObject.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = jsonObject.getString(key);
            bundle.putString(key, value);
        }
        return bundle;

    }


}

