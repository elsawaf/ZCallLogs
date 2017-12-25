package me.elsawaf.zcalllogs;

import android.util.Log;

/**
 * Created by The Brilliant on 22/12/2017.
 */

public class MobileOperatorsStats {
    private int vodafoneMins;
    private int orangeMins;
    private int etisalatMins;
    private int otherMins;
    private int totalMins;

    private int mins;

    public static final String TAG = "MobileOperator";

    public void addMinsToMobileOperator(String phNumber, int callDurationInSecs){

        int networkCode = Integer.parseInt(phNumber.substring(1,3));

        mins = callDurationInSecs / 60;
        mins++; // if duration < 60 or reminder < 60 the divide will be traction

        switch (networkCode) {
            case 10:
                vodafoneMins += mins;
                Log.d(TAG,  phNumber + " -010- " +
                        mins);
                break;

            case 11:
                etisalatMins += mins;
                Log.d(TAG,  phNumber + " -011- " +
                        mins);
                break;

            case 12:
                orangeMins += mins;
                Log.d(TAG,  phNumber + " -012- " +
                        mins);
                break;
            default: // if number start with +2 or 002 will go here, we should fix that
                otherMins += mins;
                Log.d(TAG,  phNumber + " -000- " +
                        mins);
                break;
        }
    }

    public void clearStats(){
        vodafoneMins = 0;
        etisalatMins = 0;
        orangeMins = 0;
        otherMins = 0;
        totalMins = 0;
        mins = 0;
    }

    public int getVodafoneMins() {
        return vodafoneMins;
    }

    public int getOrangeMins() {
        return orangeMins;
    }

    public int getEtisalatMins() {
        return etisalatMins;
    }

    public int getOtherMins() {
        return otherMins;
    }

    public int getTotalMins() {
        totalMins = vodafoneMins + etisalatMins + orangeMins + otherMins;
        return totalMins;
    }
}
