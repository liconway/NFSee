package com.example.nfctest;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BigBoyActivity extends AppCompatActivity {
    ViewPager viewPager;
    SlideAdapter adapter;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_boy);

        viewPager = findViewById(R.id.viewPager_id);
        System.out.println("Big Boy is Running!!");
        json = getJSON();

        configureButtons();
    }

    private void configureButtons(){
        ImageButton facebookButton = findViewById(R.id.faceBook);
        ImageButton twitterButton = findViewById(R.id.twitter);
        ImageButton yelpButton = findViewById(R.id.yelp);
        Button jobButton = findViewById(R.id.jobListing);

        twitterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(json.optString("twurl")));
                startActivity(browserIntent);
            }
        });

        yelpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(json.optString("yurl")));
                startActivity(browserIntent);
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(json.optString("fburl")));
                startActivity(browserIntent);
            }
        });

        jobButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(json.optString("inurl")));
                startActivity(browserIntent);
            }
        });


    }

    private Bitmap[] getBitmaps(String[] urls) {
        try {
            URLImageLoader loader = new URLImageLoader();
            loader.execute(urls);
            return loader.get();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Trouble loading company logo");
            return null;
        }
    }

    private JSONObject getJSON() {
        JSONObject json;
        try {
            json = new JSONObject(getIntent().getStringExtra("JSON_STRING"));
            System.out.println(json.toString());
            displayData(json);

            JSONArray alerts = json.optJSONArray("alerts");
            if (alerts.length() > 0) {
                JSONObject alert = getAlerts(alerts);
                notifyAlert(alert);
            } else {
                System.out.println("no alerts");
            }

        } catch (JSONException e) {
            json = null;
            e.printStackTrace();
        }
        return json;
    }

    public static String formatPhone(String number) {
        if (number.length() != 10) return number;
        char[] c = number.toCharArray();
        return "(" + c[0] + c[1] + c[2] + ") " + c[3] + c[4] + c[5] + " - " + c[6] + c[7] + c[8] + c[9];
    }

    private void displayData(JSONObject data) {
        TextView field = findViewById(R.id.company_title);
        field.setText(data.optString("title"));

        field = findViewById(R.id.address);
        field.setText(data.optString("address"));
        Linkify.addLinks(field, Linkify.MAP_ADDRESSES);

        field = findViewById(R.id.phone);
        field.setText(formatPhone(data.optString("phone")));
        Linkify.addLinks(field, Linkify.PHONE_NUMBERS);

        field = findViewById(R.id.url);
        field.setText(data.optString("url"));
        Linkify.addLinks(field, Linkify.WEB_URLS);

        field = findViewById(R.id.description);
        field.setText(data.optString("description"));


        JSONArray photos = data.optJSONArray("photos");
        String[] urls = new String[photos.length()];
        Bitmap[] images;
        String[] captions = new String[photos.length()];


        for(int i = 0; i < photos.length(); i++) {
            JSONObject obj = photos.optJSONObject(i);
            urls[i] = obj.optString("url");
            captions[i] = obj.optString("caption");
        }
        images = getBitmaps(urls);

        adapter = new SlideAdapter(this, images, captions);
        viewPager.setAdapter(adapter);

        ImageView image = (ImageView) findViewById(R.id.company_logo);
        Bitmap bitmap = getBitmaps(new String[] { data.optString("logo") })[0];
        image.setImageBitmap(bitmap);
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


    // prevents NFC from working on this activity

    private void enableForegroundDispatchSystem(){

        Intent intent = new Intent(this, BigBoyActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
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
