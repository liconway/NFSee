package com.example.nfctest;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class URLImageLoader extends AsyncTask<String, Void, Bitmap[]> {
    public URLImageLoader() {
        super();
    }

    @Override
    protected Bitmap[] doInBackground(String... urls) {
        Bitmap[] bmp = new Bitmap[urls.length];
        for (int i=0; i<urls.length; i++) {
            try {
                bmp[i] = Utils.getBitmap(urls[i]);
            } catch (Exception e) {
                System.err.println("Image not found! URL: " + urls[i]);
//                e.printStackTrace();
//                System.err.println(e.getMessage());
            }
        }
        return bmp;
    }
}
