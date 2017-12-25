package me.elsawaf.zcalllogs;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.elsawaf.thebrilliant.zcalllogs.R;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int CALLS_LOADER_ID = 22;
    public static final String TAG = "CallLog";
    private int mUserChoice;

    private Spinner mSpinner;
    private PieChart mChart;
    private StatsChart statsChart;

    private MobileOperatorsStats mobileOperatorsStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(spinnerListener);

        mChart = (PieChart) findViewById(R.id.chart);
        statsChart = new StatsChart(mChart, this);

        mobileOperatorsStats = new MobileOperatorsStats();
        getSupportLoaderManager().initLoader(CALLS_LOADER_ID, null, this);
    }

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String userChoiceString = getResources().getStringArray(R.array.spinner_values)[position];
            mUserChoice = Integer.valueOf(userChoiceString);

            switch(mUserChoice){
                case CursorQueryUtil.WEEK_STATISTICS:
                    CursorQueryUtil.setQueryForWeek();
                    break;
                case CursorQueryUtil.MONTH_STATISTICS:
                    CursorQueryUtil.setQueryForMonth();
                    break;
                case CursorQueryUtil.ALL_TIME_STATISTICS:
                    CursorQueryUtil.setQueryForAllTime();
                    break;
                default:
                    CursorQueryUtil.setQueryForToDay();
                    break;
            }
            getSupportLoaderManager().restartLoader(CALLS_LOADER_ID, null, MainActivity.this);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader() >> loaderID : " + id);


        return new CursorLoader(
                this,   // Parent activity context
                CallLog.Calls.CONTENT_URI,        // Table to query
                CursorQueryUtil.projection,     // Projection to return
                CursorQueryUtil.whereClause,            // No selection clause
                CursorQueryUtil.whereArgs,            // No selection arguments
                null             // Default sort order
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished()");

        if (data == null || !data.moveToNext()){
            Log.d(TAG, "No data");
            statsChart.displayEmptyDataMessage();
            return;
        }

        Log.d(TAG, "count # " + data.getCount());
        int number = data.getColumnIndex(CallLog.Calls.NUMBER);
        int date = data.getColumnIndex(CallLog.Calls.DATE);
        int duration = data.getColumnIndex(CallLog.Calls.DURATION);

        mobileOperatorsStats.clearStats();

        while (data.moveToNext()) {
            String phNumber = data.getString(number);
            String callDate = data.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = data.getString(duration);


            int callDurationInSecs = Integer.parseInt(callDuration);

            if (callDurationInSecs == 0){
                continue;
            }
            else {
                mobileOperatorsStats.addMinsToMobileOperator(phNumber, callDurationInSecs);
                Log.d(TAG,  //phNumber + " -011- " +
                        callDayTime + " - " +
                        callDuration);
            }

        }
        Log.d(TAG,  "Vodafone" + mobileOperatorsStats.getVodafoneMins());
        Log.d(TAG,  "Etisalat" + mobileOperatorsStats.getEtisalatMins());
        Log.d(TAG,  "Orange" + mobileOperatorsStats.getOrangeMins());
        Log.d(TAG,  "Other" + mobileOperatorsStats.getOtherMins());

        statsChart.displayDataOnChart(mobileOperatorsStats);

        data.close();

    }

        @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
