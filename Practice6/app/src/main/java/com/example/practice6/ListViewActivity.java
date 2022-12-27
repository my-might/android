package com.example.practice6;

import static com.example.practice6.Constants.stations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ListViewActivity extends AppCompatActivity {
    ListView l = findViewById(R.id.listView);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        l = findViewById(R.id.listView);
        ArrayAdapter<String> arr;
        arr
                = new ArrayAdapter<String>(
                this,
                R.layout.list_view,
                stations);
        l.setAdapter(arr);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                                "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
                Intent result = new Intent();
                result.setType(stations[position]);
                setResult(RESULT_OK, result);
                connectMain(view);
            }
        });
    }

    public void connectMain(View view) {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }

}
