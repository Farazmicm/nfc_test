package com.example.nfc_test;

public class Demo {

    /*for (int k = 0; k < 16; k++) {
        boolean isAuthenticate = false;

        //if (mfc.authenticateSectorWithKeyA(k, bt1)) {
        //isAuthenticate = true;
        //Log.d("Auth-A:","YES");
        //} else if (mfc.authenticateSectorWithKeyB(k, bt1)) {
        //isAuthenticate = true;
        //Log.d("Auth-B:","YES");
        //} else
        if (mfc.authenticateSectorWithKeyA(k, MifareClassic.KEY_DEFAULT)) {
            isAuthenticate = true;
            Log.d("Auth-A:","KEY_DEFAULT");
        }

        if(isAuthenticate) {
            if(k == 2)
            {
                byte[] data =  mfc.readBlock(mfc.sectorToBlock(k));
                result[0] = data;
                Log.v("Result","D-" + 0 + result[0]);
                Log.v("Sector:","S-"+k + " Data:"+ toHex(data));
            }else if(k == 5) {
                byte[] data =  mfc.readBlock(mfc.sectorToBlock(k));
                result[1] = data;
                Log.v("Result","D-" + 1 + result[1]);
                Log.v("Sector:","S-"+k + " Data:"+ toHex(data));
            } else if(k == 8) {
                byte[] data =  mfc.readBlock(mfc.sectorToBlock(k));
                result[2] = data;
                Log.v("Result","D-" + 2 + result[2]);
                Log.v("Sector:","S-"+k + " Data:"+ toHex(data));
            }
        }

    }*/



    /*for (int i = 0; i < MyVariables.f17bt.length; i++) {
        byte[] bt1 = MyVariables.f17bt[i];
        for (int k = 0; k < 16; k++) {
            boolean isAuthenticate = false;

            //if (mfc.authenticateSectorWithKeyA(k, bt1)) {
            //isAuthenticate = true;
            //Log.d("Auth-A:","YES");
            //} else if (mfc.authenticateSectorWithKeyB(k, bt1)) {
            //isAuthenticate = true;
            //Log.d("Auth-B:","YES");
            //} else
            if (mfc.authenticateSectorWithKeyA(k, MifareClassic.KEY_DEFAULT)) {
                isAuthenticate = true;
                Log.d("Auth-A:","KEY_DEFAULT");
            }

            if(isAuthenticate) {
                if(k == 2)
                {
                    byte[] data =  mfc.readBlock(mfc.sectorToBlock(k));
                    result[i] = data;
                    //Log.v("Result","D-" + i + result[0]);
                    Log.v("Sector:","S-"+k + " Data:"+ toHex(data));
                }else if(k == 5) {
                    byte[] data =  mfc.readBlock(mfc.sectorToBlock(k));
                    result[i] = data;
                    //Log.v("Result","D-" + 1 + result[1]);
                    Log.v("Sector:","S-"+k + " Data:"+ toHex(data));
                } else if(k == 8) {
                    byte[] data =  mfc.readBlock(mfc.sectorToBlock(k));
                    result[i] = data;
                    //Log.v("Result","D-" + 2 + result[2]);
                    Log.v("Sector:","S-"+k + " Data:"+ toHex(data));
                }
            }
        }
    }*/



    /*for(int j=0;j< havingData.size();j++){

                    CardModel e = havingData.get(j);

                    tag_data.append("\n-----Having data: "+ j + "----");

                    tag_data.append("\nsectorNo : "+ e.sectorID);
                    tag_data.append("\nsectordata hex: "+ e.sectorHexData);
                    try {
                        tag_data.append("\nsectordata : "+ new String(e.sectorData,"UTF-8"));
                    } catch (UnsupportedEncodingException unsupportedEncodingException) {
                        unsupportedEncodingException.printStackTrace();
                    }
                }*/


       /*
            try {

                MifareClassic mfc = MifareClassic.get((Tag) intent.getParcelableExtra("android.nfc.extra.TAG"));
                mfc.connect();

                try{
                    if (mfc.authenticateSectorWithKeyA(15,hexStringToByteArray(KEY1))) {
                        Log.d("Authenticate: ", "Authorization granted ");

                        int block_index = mfc.sectorToBlock(15);

                        byte[] block = mfc.readBlock(block_index);
                        String s_block = toHex(block);
                        //DATA1 = s_block;
                        Log.d("Data 1: ", s_block);
                    }

                    if (mfc.authenticateSectorWithKeyA(15,hexStringToByteArray(KEY2))) {
                        Log.d("Authenticate: ", "Authorization granted ");

                        int block_index = mfc.sectorToBlock(15);

                        byte[] block = mfc.readBlock(block_index);
                        String s_block = toHex(block);
                        //DATA1 = s_block;
                        Log.d("Data 2: ", s_block);
                    }

                    if (mfc.authenticateSectorWithKeyA(15,hexStringToByteArray(KEY3))) {
                        Log.d("Authenticate: ", "Authorization granted ");

                        int block_index = mfc.sectorToBlock(15);

                        byte[] block = mfc.readBlock(block_index);
                        String s_block = toHex(block);
                        //DATA1 = s_block;
                        Log.d("Data 3: ", s_block);
                    }

                    for(int i=0; i< mfc.getSectorCount(); i++){

                        boolean isAuthenticated = false;

                        if (mfc.authenticateSectorWithKeyA(i,hexStringToByteArray(KEY1))) {
                            isAuthenticated = true;
                        }

                        if (mfc.authenticateSectorWithKeyA(i, MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
                            isAuthenticated = true;
                        } else if (mfc.authenticateSectorWithKeyA(i, MifareClassic.KEY_DEFAULT)) {
                            isAuthenticated = true;
                        } else if (mfc.authenticateSectorWithKeyA(i,MifareClassic.KEY_NFC_FORUM)) {
                            isAuthenticated = true;
                        } else {
                            Log.d("TAG", "Authorization denied ");
                        }
                        if(!mfc.isConnected()){
                            mfc.connect();
                        }
                        if(isAuthenticated) {
                            int block_index = mfc.sectorToBlock(i);

                            byte[] block = mfc.readBlock(block_index);
                            String s_block = toHex(block);
                            //DATA1 = s_block;
                            Log.d("Sector: " + i, s_block);

                            //String iteid = s_block.substring(0, 2);
                            //String customer = s_block.substring(2, 6);
                            //String card_type = s_block.substring(6, 8);
                            //String identity = s_block.substring(8, 9);
                            //String school_id = s_block.substring(9, 12);
                        }
                    }
                }catch (Exception e){
                    Log.v("Exx: ","Block Data: : " + e.toString());
                    mfc.close();
                }

                if (mfc.authenticateSectorWithKeyA(15, MifareClassic.KEY_DEFAULT))//new byte[]{-114, 74, 14, 74, 6, -62}
                    {
                        Log.v("Message1", "Card Authenticate");
                    String str = new String(mfc.readBlock(60)).trim();
                    String iteid = str.substring(0, 2);
                    String customer = str.substring(2, 6);
                    String card_type = str.substring(6, 8);
                    String identity = str.substring(8, 9);
                    String school_id = str.substring(9, 12);
                    if (!iteid.equals("01") || !customer.equals("9999") || !identity.equals("S") || card_type.equals("22") || !school_id.equals("001")) {
                        mfc.close();
                        Log.v("Message", "Please read student card.");

                    } else {
                        byte[] data = mfc.readBlock(61);
                        if (data != null) {
                            Log.v("StudentData",new String(data).trim());
                        } else {
                            Log.v("Message", "Activity already done.");
                            mfc.close();
                        }
                    }
                } else {
                    Log.v("INFO", "Authentication failed");
                    mfc.close();

                }

            } catch (Exception e2) {
                Log.v("Exception", "E2: " + e2.toString());
            }
*/
}
