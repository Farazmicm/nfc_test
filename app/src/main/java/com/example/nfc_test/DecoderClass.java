package com.example.nfc_test;

import androidx.core.view.MotionEventCompat;

import org.xmlpull.v1.XmlPullParser;

public class DecoderClass {

    /* renamed from: A */
    private final String f10A = "1010";

    /* renamed from: B */
    private final String f11B = "1011";

    /* renamed from: C */
    private final String f12C = "1100";
    private int CARD_NUMBER = 0;

    /* renamed from: D */
    private final String f13D = "1101";

    /* renamed from: E */
    private final String f14E = "1110";
    private final String EIGHT = "1000";

    /* renamed from: F */
    private final String f15F = "1111";
    private int FACILITY_CODE = 0;
    private int FIRST_PARITY = 0;
    private final String FIVE = "0101";
    private final String FOUR = "0100";
    private int IGNORED_BIT = 0;
    private int ISSUE_LEVEL = 0;
    private final String NINE = "1001";
    private final String ONE = "0001";
    private int SECOND_PARITY = 0;
    private final String SEVEN = "0111";
    private final String SIX = "0110";
    private byte[] SRC = null;
    private final String THREE = "0011";
    private final String TWO = "0010";
    private final String ZERO = "0000";

    public DecoderClass(byte[] src) {
        this.SRC = src;
        processData(this.SRC);
    }

    private void processData(byte[] data) {
        try {
            if (data.length >= 7) {
                byte[] bt = new byte[5];
                System.arraycopy(data, 2, bt, 0, 5);
                String str = toBinary(bytesToHex(bt).toCharArray()).substring(0, 37);
                this.IGNORED_BIT = Integer.parseInt(str.substring(0, 1), 2);
                this.FACILITY_CODE = Integer.parseInt(str.substring(1, 12), 2);
                this.FIRST_PARITY = Integer.parseInt(str.substring(12, 13), 2);
                String raw_card_number = str.substring(13, 33);
                int length = raw_card_number.length();
                String rts = XmlPullParser.NO_NAMESPACE;
                for (int i = length - 1; i >= 0; i--) {
                    rts = String.valueOf(rts) + raw_card_number.charAt(i);
                }
                this.CARD_NUMBER = Integer.parseInt(rts, 2);
                this.SECOND_PARITY = Integer.parseInt(str.substring(33, 34), 2);
                this.ISSUE_LEVEL = Integer.parseInt(str.substring(34, 37), 2);
                return;
            }
            this.IGNORED_BIT = 0;
            this.FACILITY_CODE = 0;
            this.FIRST_PARITY = 0;
            this.CARD_NUMBER = 0;
            this.SECOND_PARITY = 0;
            this.ISSUE_LEVEL = 0;
        } catch (Exception e) {
            this.IGNORED_BIT = 0;
            this.FACILITY_CODE = 0;
            this.FIRST_PARITY = 0;
            this.CARD_NUMBER = 0;
            this.SECOND_PARITY = 0;
            this.ISSUE_LEVEL = 0;
        }
    }

    private String toBinary(char[] ch) throws Exception, IllegalArgumentException {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (i < ch.length) {
            try {
                switch (ch[i]) {
                    case '0':
                        sb.append("0000");
                        break;
                    case '1':
                        sb.append("0001");
                        break;
                    case '2':
                        sb.append("0010");
                        break;
                    case '3':
                        sb.append("0011");
                        break;
                    case '4':
                        sb.append("0100");
                        break;
                    case '5':
                        sb.append("0101");
                        break;
                    case '6':
                        sb.append("0110");
                        break;
                    case '7':
                        sb.append("0111");
                        break;
                    case '8':
                        sb.append("1000");
                        break;
                    case '9':
                        sb.append("1001");
                        break;
                    case 65:
                        sb.append("1010");
                        break;
                    case 66:
                        sb.append("1011");
                        break;
                    case 67:
                        sb.append("1100");
                        break;
                    case 68:
                        sb.append("1101");
                        break;
                    case 'E':
                        sb.append("1110");
                        break;
                    case 'F':
                        sb.append("1111");
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                i++;
            } catch (Exception e) {
                throw new Exception(e);
            }
        }
        return sb.toString();
    }

    public String bytesToHex(byte[] bytes) {
        char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & MotionEventCompat.ACTION_MASK;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }

    public int getIngoredBit() {
        return this.IGNORED_BIT;
    }

    public int getFacilityCode() {
        return this.FACILITY_CODE;
    }

    public int getFirstParity() {
        return this.FIRST_PARITY;
    }

    public int getCardNumber() {
        return this.CARD_NUMBER;
    }

    public int getSecondParity() {
        return this.SECOND_PARITY;
    }

    public int getIssueLevel() {
        return this.ISSUE_LEVEL;
    }
}

