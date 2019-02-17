package com.example.nfctest;

import android.annotation.TargetApi;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.content.Intent;
import android.nfc.Tag;
import android.nfc.NdefMessage;

import java.io.UnsupportedEncodingException;

import static android.nfc.NdefRecord.createTextRecord;

public class MainActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    ToggleButton tglReadWrite;
    EditText txtTagContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        tglReadWrite = findViewById(R.id.tglReadWrite);
//        txtTagContent = (EditText) findViewById(R.id.txtTagContent);
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        if(intent.hasExtra(NfcAdapter.EXTRA_TAG)){
            Toast.makeText(this, "NfcIntent!", Toast.LENGTH_SHORT).show();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(parcelables != null && parcelables.length > 0){
                readTextFromMessage((NdefMessage) parcelables[0]);
            }
            else{
                Toast.makeText(this, "No NDEF Messages Found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage){
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length>0){
            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            txtTagContent.setText(tagContent);
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
}
