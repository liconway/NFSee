package com.example.nfctest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class BigBoyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_boy);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Intent callingIntent = getIntent();
        JSONObject json;
        try {
            json = new JSONObject(callingIntent.getStringExtra("JSON_STRING"));
        } catch (JSONException e) {
            json = null;
            e.printStackTrace();
        }
    }

}
