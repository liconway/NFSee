package com.example.nfctest;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

        System.out.println("------------Bus Boy!");

        json = getJSON();

        final Button share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String message = json.optString("title") + "\n"
                        + json.optString("address") + "\n";
                JSONArray vehicles = json.optJSONArray("vehicles");
                if (vehicles != null && vehicles.length() > 0)
                    for (int i=0; i<vehicles.length(); i++) {
                        JSONObject bus = vehicles.optJSONObject(i);
                        if (bus != null) message += bus.optString("name") + ":\n\n"
                                + bus.optString("number1") + ", "
                                + bus.optString("number2") + ", "
                                + bus.optString("number3") + "\n\n";
                    }

                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share this location"));
            }
        });
    }

    private JSONObject getJSON() {
        JSONObject json;
        try {
            json = new JSONObject(getIntent().getStringExtra("JSON_STRING"));
            System.out.println(json.toString());

            JSONArray buses = json.optJSONArray("vehicles");
            if (buses != null && buses.length() > 0) {
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
                t1 = findViewById(R.id.r3c1);
                t2 = findViewById(R.id.r3c2);
                t3 = findViewById(R.id.r3c3);
                break;
        }
        nameText.setText(busName);
        t1.setText(time1);
        t2.setText(time2);
        t3.setText(time3);
    }

    // prevents NFC from working on this activity

    private void enableForegroundDispatchSystem(){

        Intent intent = new Intent(this, BusBoyActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[] {};

        Utils.nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }

    @Override
    protected void onResume() {
        super.onResume();

        enableForegroundDispatchSystem();
    }

    private void disableForegroundDispatchSystem(){
        Utils.nfcAdapter.disableForegroundDispatch(this);
    }

}
