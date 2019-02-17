package com.example.nfctest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
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
            System.out.println(json.toString());
            displayData(json);
            JSONArray alerts = json.optJSONArray("alerts");

            if (alerts != null) {
                JSONObject alert = getAlerts(alerts);
                notifyAlert(alert);
            } else {
                System.out.println("no alerts");
            }

        } catch (JSONException e) {
            json = null;
            e.printStackTrace();
        }

        ImageView companyLogo = findViewById(R.id.company_logo);

        try {
            Bitmap bmp = Utils.getBitmap(json.optString("logo"));
            companyLogo.setImageBitmap(bmp);
        } catch (Exception e) {
            System.err.println("Image not found!");
        }

    }

    private void displayData(JSONObject data) {
        TextView field = (TextView) findViewById(R.id.company_title);

        field.setText(data.optString("title"));

        field = findViewById(R.id.address);
        field.setText(data.optString("address"));

        field = findViewById(R.id.phone);
        field.setText(data.optString("phone"));

        field = findViewById(R.id.url);
        field.setText(data.optString("url"));

        field = findViewById(R.id.description);
        field.setText(data.optString("description"));
    }

    private JSONObject getAlerts(JSONArray alerts) {
//        JSONObject object;
//        for (int i=0; i < alerts.length(); i++) {
//            object = alerts.optJSONObject(i);
//            alerts.optString(i);
//        }
//        return arr;
        JSONObject object = alerts.optJSONObject(0);
        return object;
    }



    private void notifyAlert(final JSONObject jsonObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BigBoyActivity.this);

        System.out.println("-------------------------notifyAlert");

        builder.setCancelable(true);
        builder.setTitle("Alert Notification");
        builder.setMessage(jsonObject.optString("title"));

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
                intent.putExtra("JSON_STRING", jsonObject.toString());
                startActivity(intent);
            }
        });

        builder.show();
    }

}
