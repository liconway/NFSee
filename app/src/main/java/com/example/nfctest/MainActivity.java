package com.example.nfctest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentFilter;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.content.Intent;
import android.nfc.NdefMessage;
import static android.nfc.NdefRecord.createTextRecord;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;


public class MainActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    ToggleButton tglReadWrite;
    EditText txtTagContent;
    private boolean testNFCRead = true;
    JSONObject data;

    private static final String url = "http://10.13.106.210:8192/data?uuid=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        tglReadWrite = findViewById(R.id.tglReadWrite);
//        txtTagContent = (EditText) findViewById(R.id.txtTagContent);

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(nfcAdapter!=null && nfcAdapter.isEnabled()){
            Toast.makeText(this, "NFC available!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show();
        }
        configureButton();
    }

    private void configureButton(){
        Button backButton = findViewById(R.id.favorite);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
            }
        });
    }

    public void openBigBoy(String json) {
        Intent intent = new Intent(this, BigBoyActivity.class);
        intent.putExtra("JSON_STRING", json);
        startActivity(intent);
        TextView loading = findViewById(R.id.funtext);
        loading.setText("Ready to scan, boss");
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        if(intent.hasExtra(NfcAdapter.EXTRA_TAG)){
            Toast.makeText(this, "NfcIntent!", Toast.LENGTH_SHORT).show();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(parcelables != null && parcelables.length > 0){
                TextView loading = findViewById(R.id.funtext);
                loading.setText("Loading...");
                readTextFromMessage((NdefMessage) parcelables[0]);
            }
            else{
                Toast.makeText(this, "No NDEF Messages Found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public synchronized void setJSON(JSONObject j) {
        this.data = j;
    }


    private void readTextFromMessage(NdefMessage ndefMessage){
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length>0){
            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            Utils.getRequest(tagContent, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println(result);
                    openBigBoy(result);

                }
            }, this, url);
        }
        else{
            Toast.makeText(this, "No NDEF Records Found!", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private NdefMessage createNdefMessage(String content){
        NdefRecord ndefRecord = createTextRecord("en", content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] {ndefRecord});

        return ndefMessage;
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord)
    {
        String tagContent = null;
        try{
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length - languageSize -1, textEncoding);

        }catch(UnsupportedEncodingException e){
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

    private void enableForegroundDispatchSystem(){

        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[] {};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
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
        nfcAdapter.disableForegroundDispatch(this);
    }
}
