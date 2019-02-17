package com.example.nfctest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URL;

public class Utils {
    public static NfcAdapter nfcAdapter;

    public static void getRequest(String uuid, final VolleyCallback callback, Activity activity, String url) {



        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+uuid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("http request works");
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.err.println("nono bad");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static Bitmap getBitmap(String imgURL) throws Exception {
        if (imgURL == null) throw new Exception();
        URL url = new URL(imgURL);
        return BitmapFactory.decodeStream(url.openConnection().getInputStream());
    }
}
