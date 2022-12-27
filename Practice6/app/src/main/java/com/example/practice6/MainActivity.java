package com.example.practice6;

import static com.example.practice6.Constants.stations;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int SELECTED_STATION = -1;
    private static String NO_STATION = "Station not selected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connectListViewActivity(View view) {
        Intent intentCat = new Intent(this, ListViewActivity.class);
        startActivityForResult(intentCat, SELECTED_STATION);
        onActivityResult(SELECTED_STATION);
    }

    protected void onActivityResult(int resultCode) {
        TextView tv = (TextView) findViewById(R.id.textView);
        switch (SELECTED_STATION) {
            case (-1): {
                tv.setText(NO_STATION);
            }
            default: {
                tv.setText(stations[resultCode]);
            }
        }
    }
}