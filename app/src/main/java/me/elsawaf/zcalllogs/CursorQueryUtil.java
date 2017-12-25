package me.elsawaf.zcalllogs;

import android.provider.CallLog;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by The Brilliant on 21/12/2017.
 */

public class CursorQueryUtil {

    public static final String[] projection = new String [] {CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION};
    public static String whereClause;
    public static String[] whereArgs;

    public static final String TAG = "QueryUtil";

    public static final int ONE_DAY_STATISTICS = 11;
    public static final int WEEK_STATISTICS = 7;
    public static final int MONTH_STATISTICS = 30;
    public static final int ALL_TIME_STATISTICS = 100;


    public static void setQueryForToDay(){
        whereClause = CallLog.Calls.TYPE + "=? AND "
                + CallLog.Calls.DATE + ">=?" ;

        Date toDay = new Date();
        long oneDayTimestamp = subtractDays(toDay, 0);
        Log.d(TAG, "" + toDay);

        whereArgs = new String [] {String.valueOf(CallLog.Calls.OUTGOING_TYPE),
                String.valueOf(oneDayTimestamp)};
    }

    public static void setQueryForWeek(){
        whereClause = CallLog.Calls.TYPE + "=? AND "
                + CallLog.Calls.DATE + "<=? AND "
                + CallLog.Calls.DATE + ">=?";

        Date oneDay = new Date();
        long oneDayTimestamp = subtractDays(oneDay, -1);
        long before5Days = subtractDays(oneDay, 7);
        Log.d(TAG, "" + oneDay);

        whereArgs = new String [] {String.valueOf(CallLog.Calls.OUTGOING_TYPE),
                String.valueOf(oneDayTimestamp),
                String.valueOf(before5Days)};
    }

    public static void setQueryForMonth(){
        whereClause = CallLog.Calls.TYPE + "=? AND "
                + CallLog.Calls.DATE + "<=? AND "
                + CallLog.Calls.DATE + ">=?";

        Date oneDay = new Date();
        long oneDayTimestamp = subtractDays(oneDay, -1);
        long before5Days = subtractDays(oneDay, 30);
        Log.d(TAG, "" + oneDay);

        whereArgs = new String [] {String.valueOf(CallLog.Calls.OUTGOING_TYPE),
                String.valueOf(oneDayTimestamp),
                String.valueOf(before5Days)};
    }

    public static void setQueryForAllTime(){
        whereClause = CallLog.Calls.TYPE + "=?";

        whereArgs = new String [] {String.valueOf(CallLog.Calls.OUTGOING_TYPE)};
    }

    private static long subtractDays(Date oneDay, int i) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(oneDay);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -i);
        Log.d(TAG, "" + cal.getTime());
        return cal.getTimeInMillis();
    }
}
