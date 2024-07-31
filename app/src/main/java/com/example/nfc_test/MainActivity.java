package com.example.nfc_test;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.nfc_test.R;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Copyright (c) 2012-2020 Cawin Chan
//
//        Permission is hereby granted, free of charge, to any person obtaining
//        a copy of this software and associated documentation files (the
//        "Software"), to deal in the Software without restriction, including
//        without limitation the rights to use, copy, modify, merge, publish,
//        distribute, sublicense, and/or sell copies of the Software, and to
//        permit persons to whom the Software is furnished to do so, subject to
//        the following conditions:
//
//        The above copyright notice and this permission notice shall be
//        included in all copies or substantial portions of the Software.
//
//        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//        EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//        MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//        NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//        LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
//        OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
//        WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

class CardModel{
    int sectorID;
    byte[] sectorData;
    String sectorHexData;
    ArrayList<byte[]> allSectorData;
    int isSingle;
    public  CardModel(){

    }
    public CardModel(int sectorID,byte[] sectorData,String sectorHexData,ArrayList<byte[]> allSectorData,int isSingle){
        this.sectorID = sectorID;
        this.sectorData = sectorData;
        this.sectorHexData = sectorHexData;
        this.allSectorData = allSectorData;
        this.isSingle = isSingle;
    }
}

public class MainActivity extends AppCompatActivity {
    //Intialize attributes
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;

    TextView tag_data;

    final static String TAG = "nfc_test";

    StringBuilder finalString;

    Handler handler;
    IntentFilter[] intentFiltersArray;
    IntentFilter mifare;

    String[][] techListsArray;

    String KEY1 = "987654321987";
    String KEY2 = "543215432154";
    String KEY3 = "765432176543";

    String DATA1 = "";
    String DATA2 = "";
    String DATA3 = "";

    List<CardModel> lstCardData = new ArrayList<CardModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise NfcAdapter
        //nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        this.finalString = new StringBuilder();
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        this.pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);
        this.intentFiltersArray = new IntentFilter[]{new IntentFilter("android.nfc.action.TECH_DISCOVERED")};
        this.techListsArray = new String[][]{new String[]{NfcA.class.getName()}, new String[]{MifareClassic.class.getName()}};


        this.tag_data = findViewById(R.id.tag_data);

        tag_data.setText("");
        //If no NfcAdapter, display that the device has no NFC
        if (nfcAdapter == null) {
            Toast.makeText(this, "NO NFC Capabilities",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        //Create a PendingIntent object so the Android system can
        //populate it with the details of the tag when it is scanned.
        //PendingIntent.getActivity(Context,requestcode(identifier for
        //                           intent),intent,int)
        //pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        assert nfcAdapter != null;
        //nfcAdapter.enableForegroundDispatch(context,pendingIntent,
        //                                    intentFilterArray,
        //                                    techListsArray)
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    protected void onPause() {
        super.onPause();
        //Onpause stop listening
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
//        resolveIntent(intent);

    }
    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            assert tag != null;
            //String payload = detectTagData(tag);

            //tag_data.append("\n"+payload+"\n");

            //Log.v(TAG,"Payload: " + payload);
            //finalString.append("Payload: " + payload);

            readDataNew(tag);
            //readData(tag);

            /* show data */

            if(!lstCardData.isEmpty()) {
                ArrayList<CardModel> havingData = new ArrayList<CardModel>();
                for(int j=0;j< lstCardData.size();j++){

                    CardModel e = lstCardData.get(j);

                    //tag_data.append("\n-----Card data: "+ j + "----");

                    //.append("\nsectorNo : "+ e.sectorID);
                    //tag_data.append("\nsectordata hex: "+ e.sectorHexData);
                    /*try {
                        tag_data.append("\nsectordata : "+ new String(e.sectorData,"UTF-8"));
                    } catch (UnsupportedEncodingException unsupportedEncodingException) {
                        unsupportedEncodingException.printStackTrace();
                    }*/

                    //if(e.sectorHexData != "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00"){
                        //havingData.add(e);
                    //}

                    if(e.isSingle == 2){
                        //tag_data.append("\n-----Card data: "+ j + "----");


                        if(!e.allSectorData.isEmpty()){

                            int jj = 0;
                            for (int a = 0;a < e.allSectorData.size() - 1;a++) {

                                String hexData = toHex(e.allSectorData.get(a));
                                if(toDec(e.allSectorData.get(a)) == 0 || hexData == "ff ff ff ff ff ff 69 80 07 ff 00 00 00 00 00 00") {

                                } else{
                                    tag_data.append("" +  e.sectorID + "-" +  a  + "-" + hexData  + "\n");
                                    tag_data.append("" +  e.sectorID + "-" +  a  + "-" + (toHex2(e.allSectorData.get(a)))  + "\n");
                                    tag_data.append(toDec(e.allSectorData.get(a)) + "\n");
                                    //tag_data.append(hexToString(hexData) + "\n");
                                }
                            }

                        }
                    }
                }
            }
        }
    }

public String detectTagData(Tag tag) {
    StringBuilder sb = new StringBuilder();
    byte[] id = tag.getId();
    sb.append("ID (hex): ").append(toHex(id)).append('\n');
    sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
    sb.append("ID (dec): ").append(toDec(id)).append('\n');
    sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

    String prefix = "android.nfc.tech.";
    sb.append("Technologies: ");
    for (String tech : tag.getTechList()) {
        sb.append(tech.substring(prefix.length()));
        sb.append(", ");
    }

    sb.delete(sb.length() - 2, sb.length());

    for (String tech : tag.getTechList()) {

        if (tech.equals(MifareClassic.class.getName())) {
            sb.append('\n');
            String type = "Unknown";

            try {
                MifareClassic mifareTag = MifareClassic.get(tag);

                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');

                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize() + " bytes");
                sb.append('\n');

                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');

                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());
            } catch (Exception e) {
                sb.append("Mifare classic error: " + e.getMessage());
            }
        }

        if (tech.equals(MifareUltralight.class.getName())) {
            sb.append('\n');
            MifareUltralight mifareUlTag = MifareUltralight.get(tag);
            String type = "Unknown";
            switch (mifareUlTag.getType()) {
                case MifareUltralight.TYPE_ULTRALIGHT:
                    type = "Ultralight";
                    break;
                case MifareUltralight.TYPE_ULTRALIGHT_C:
                    type = "Ultralight C";
                    break;
            }
            sb.append("Mifare Ultralight type: ");
            sb.append(type);
        }

    }


    //finalString.append(sb.toString());



    return sb.toString();
}


public static final byte[] key1 = {-104, 118, 84, 50, 25, -121};
public static final byte[] key2 = {84, 50, 21, 67, 33, 84};
public static final byte[] key3 = {118, 84, 50, 23, 101, 67};
public static final byte[][] f17bt = {key1, key2, key3};
public byte[] CARD_AUTH_KEY = new byte[6];
public byte SECTOR_NUMBER;

    private void readDataNew(Tag tag){
    byte[][] result = new byte[3][];

    //if (!"android.nfc.action.TECH_DISCOVERED".equals(intent.getAction())) {-114, 74, 14, 74, 6, -62
    //  Log.v("BG: ","CARD_ERROR");
    //}
    MifareClassic mfc = MifareClassic.get(tag);
    //MifareClassic mfc = MifareClassic.get((Tag) intent.getParcelableExtra("android.nfc.extra.TAG"));
    try{
        mfc.close();
        mfc.connect();

        if(mfc.isConnected()) {
            for (int i = 0; i < f17bt.length; i++) {
                byte[] bt1 = f17bt[i];
                for (int k = 0; k < 16; k++) {
                    if (mfc.authenticateSectorWithKeyA(k, bt1)) {
                        result[i] = mfc.readBlock(mfc.sectorToBlock(k));
                    }

                }
            }
            tag_data.append("Result length: " + result.length);


            if (result[0] == null || result[1] == null || result[2] == null) {
                if (mfc.isConnected()) {
                    try {
                        mfc.close();
                    } catch (Exception e) {
                        //Log.v("BG: ","Result length: " + result.length);
                        if (!MyVariables.IsProduction) {Log.v("BG: Exception:",e.getMessage());}
                    }
                }
                //return "MASTER_CARD_ERROR";
                tag_data.append("MASTER_CARD_ERROR");
                if (!MyVariables.IsProduction) {Log.v("BG:","MASTER_CARD_ERROR");}
            } else {
                //tag_data.append("Data not found");
                if (!MyVariables.IsProduction) {Log.v("BG:","Data found");}
            }
            if (!MyVariables.IsProduction) {Log.v("BG:","Result found");}

            byte[] bt = calculateResult(result);
            //tag_data.append("All Result: " + toHex(bt));
            if (!MyVariables.IsProduction) {Log.v("BG: ll Result:",toHex(bt));}

            byte[] result1 = new byte[8];
            byte[] result2 = new byte[8];
            System.arraycopy(bt, 0, result1, 0, 8);
            System.arraycopy(bt, 8, result2, 0, 8);
            if (Arrays.equals(result1, result2)) {
                System.arraycopy(result1, 0, CARD_AUTH_KEY, 0, 6);
                SECTOR_NUMBER = result1[6];
                //tag_data.append("AUTH KEY - " + toHex(CARD_AUTH_KEY));
                //tag_data.append("Sector No - " + ((int)SECTOR_NUMBER));
                if (!MyVariables.IsProduction) {Log.v("BG","AUTH KEY: " + toHex(CARD_AUTH_KEY));
                    if (!MyVariables.IsProduction) {Log.v("BG","Sector No: " + ((int)SECTOR_NUMBER));}}

                if (mfc.isConnected()) {
                    try {
                        mfc.close();
                    } catch (Exception e2) {
                    }
                }
                //  return "SUCCESS";
                if (!MyVariables.IsProduction) {Log.v("BG:","SUCCESS");}
            }else {
                //tag_data.append("Bytes not match");
                if (!MyVariables.IsProduction) {Log.v("BG:","Bytes not match");}
            }
            if (mfc.isConnected()) {
                try {
                    mfc.close();
                } catch (Exception e3) {
                }
            }
            //return "ERROR";
            //tag_data.append("ERROR");
            if (!MyVariables.IsProduction) {Log.v("BG:","ERROR");}
            //mfc.close();
        }
    }catch(Exception e){
        if (!MyVariables.IsProduction) {Log.v("Exception:",e.getMessage());}
    }
}
    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append("");
            }
        }
        return sb.toString();
    }

    private String toHex2(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            String out = "";
            if (b < 0x10)
            {
                out = "0";
                //sb.append('0');
            }
            out += Integer.toHexString(b);
            //sb.append(Integer.toHexString(b));
            sb.append(hexToString(out));
            if (i > 0) {
                sb.append("");
            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }
    public void writeTag(MifareUltralight mifareUlTag) {
        try {
            mifareUlTag.connect();
            mifareUlTag.writePage(4, "get ".getBytes(Charset.forName("US-ASCII")));
            mifareUlTag.writePage(5, "fast".getBytes(Charset.forName("US-ASCII")));
            mifareUlTag.writePage(6, " NFC".getBytes(Charset.forName("US-ASCII")));
            mifareUlTag.writePage(7, " now".getBytes(Charset.forName("US-ASCII")));
        } catch (IOException e) {
            if (!MyVariables.IsProduction) {Log.e(TAG, "IOException while writing MifareUltralight...", e);}
        } finally {
            try {
                mifareUlTag.close();
            } catch (IOException e) {
                if (!MyVariables.IsProduction) {Log.e(TAG, "IOException while closing MifareUltralight...", e);}
            }
        }
    }
    public String readTag(MifareUltralight mifareUlTag) {
        try {
            mifareUlTag.connect();
            byte[] payload = mifareUlTag.readPages(4);
            return new String(payload, Charset.forName("US-ASCII"));
        } catch (IOException e) {
            if (!MyVariables.IsProduction) {Log.e(TAG, "IOException while reading MifareUltralight message...", e);}
        } finally {
            if (mifareUlTag != null) {
                try {
                    mifareUlTag.close();
                }
                catch (IOException e) {
                    if (!MyVariables.IsProduction) {Log.e(TAG, "Error closing tag...", e);}
                }
            }
        }
        return null;
    }

    private byte[] hexStringToByteArray(String s) {
        try{
            int len = s.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i+1), 16));
            }
            if (!MyVariables.IsProduction) {Log.v(s,data.toString());}
            return data;
        }catch (Exception e){
            if (!MyVariables.IsProduction) {Log.v("hexStringToByteArray",e.toString());}
            return new byte[0];
        }
    }

    private String hexToString(String hexS){
        String result = new String();
        char[] charArray = hexS.toCharArray();
        for(int i = 0; i < charArray.length; i=i+2) {
            String st = ""+charArray[i]+""+charArray[i+1];
            char ch = (char)Integer.parseInt(st, 16);
            result = result + ch;
        }
        return result;
    }

    public byte[] calculateResult(byte[][] bt) {
        byte[] b1 = bt[0];
        byte[] b2 = bt[1];
        byte[] b3 = bt[2];
        byte[] r1 = new byte[b1.length];
        byte[] r2 = new byte[b3.length];
        for (int i = 0; i < b1.length; i++) {
            r1[i] = (byte) (b1[i] & b2[i]);
        }
        for (int i2 = 0; i2 < b3.length; i2++) {
            r2[i2] = (byte) (r1[i2] | b3[i2]);
        }
        return r2;
    }


}



