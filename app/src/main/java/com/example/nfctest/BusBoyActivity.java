package com.example.nfctest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class BusBoyActivity extends AppCompatActivity {


    JSONObject json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_boy);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        json = getJSON();
    }

    private JSONObject getJSON() {
        JSONObject json;
        try {
            json = new JSONObject(getIntent().getStringExtra("JSON_STRING"));
            System.out.println(json.toString());

            JSONArray buses = json.optJSONArray("vehicles");
            if (buses.length() > 0) {
                for(int i = 0; i < 4; i++){
                    JSONObject bus = buses.getJSONObject(i);
                    writeBusTimes(bus, i);
                }
            } else {
                System.out.println("no alerts");
            }

        } catch (JSONException e) {
            json = null;
            e.printStackTrace();
        }
        return json;
    }

    private void writeBusTimes(JSONObject bus, int i){
        String busName = bus.optString("name");
        String time1 = bus.optString("number1");
        String time2 = bus.optString("number2");
        String time3 = bus.optString("number3");

        TextView nameText = null;
        TextView t1 = null;
        TextView t2 = null;
        TextView t3 = null;
        TextView t4 = null;
        switch(i){
            case 0:
                nameText = findViewById(R.id.r0c0);
                t1 = findViewById(R.id.r0c1);
                t2 = findViewById(R.id.r0c2);
                t3 = findViewById(R.id.r0c3);
                break;
            case 1:
                nameText = findViewById(R.id.r1c0);
                t1 = findViewById(R.id.r1c1);
                t2 = findViewById(R.id.r1c2);
                t3 = findViewById(R.id.r1c3);
                break;
            case 2:
                nameText = findViewById(R.id.r2c0);
                t1 = findViewById(R.id.r2c1);
                t2 = findViewById(R.id.r2c2);
                t3 = findViewById(R.id.r2c3);
                break;
            case 3:
                nameText = findViewById(R.id.r3c0);
                t1 = findViewById(R.id.r0c1);
                t2 = findViewById(R.id.r0c2);
                t3 = findViewById(R.id.r0c3);
                break;
        }
        nameText.setText(busName);
        t1.setText(time1);
        t2.setText(time2);
        t3.setText(time3);
    }

}
