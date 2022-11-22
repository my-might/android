package com.example.practice2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CatActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat_activity);
    }

    public void connectMain(View view) {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }
}
