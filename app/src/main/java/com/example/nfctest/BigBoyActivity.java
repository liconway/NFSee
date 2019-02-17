package com.example.nfctest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

public class BigBoyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_boy);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        System.out.println("Big Boy is Running!!");

        Intent callingIntent = getIntent();
        JSONObject json;
        try {
            json = new JSONObject(callingIntent.getStringExtra("JSON_STRING"));
        } catch (JSONException e) {
            json = null;
            e.printStackTrace();
        }
    }

    private void notifyAlert(final String json){
        AlertDialog.Builder builder = new AlertDialog.Builder(BigBoyActivity.this);

        builder.setCancelable(true);
        builder.setTitle("Alert Notification");

        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(BigBoyActivity.this, AlertActivity.class);
                intent.putExtra("JSON_STRING", json);
                startActivity(intent);
            }
        });
    }

}
