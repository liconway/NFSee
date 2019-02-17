package com.example.nfctest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class AlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent callingIntent = getIntent();
        JSONObject json;
        try {
            json = new JSONObject(callingIntent.getStringExtra("JSON_STRING"));
            TextView description = findViewById(R.id.description);
            TextView alertTitle = findViewById(R.id.alertTitle);
            TextView alertDate = findViewById(R.id.alertDate);
            alertTitle.setText(json.optString("title"));
            java.util.Date time=new java.util.Date((long)Integer.parseInt(json.optString("start"))*1000);
            alertDate.setText(time.toString());
            description.setText(json.optString("description"));

        } catch (JSONException e) {
            json = null;
            e.printStackTrace();
        }
    }

}
