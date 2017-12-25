package me.elsawaf.zcalllogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.elsawaf.thebrilliant.zcalllogs.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The Brilliant on 22/12/2017.
 */

public class StatsChart implements OnChartValueSelectedListener {
    private PieChart mChart;
    private Context mContext;
    private int mTotalMins;

    public StatsChart(PieChart chart, Context context) {
        mChart = chart;
        mContext = context;
        setupChartStyle();
    }

    private void setupChartStyle() {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

//        mChart.setCenterTextTypeface(mTfLight);
//        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

//        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        mChart.setDrawEntryLabels(false);
        // entry label styling
/*        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);*/

        mChart.setOnChartValueSelectedListener(this);
    }

    public void displayDataOnChart(MobileOperatorsStats stats) {
        List<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        if ( stats.getVodafoneMins() > 0 ){
            entries.add(new PieEntry(stats.getVodafoneMins(), mContext.getString(R.string.title_vodafone)));
            colors.add(ContextCompat.getColor(mContext, R.color.red_stats));
        }
        if ( stats.getEtisalatMins() > 0 ){
            entries.add(new PieEntry(stats.getEtisalatMins(), mContext.getString(R.string.title_etisalat)));
            colors.add(ContextCompat.getColor(mContext, R.color.green_stats));
        }
        if ( stats.getOrangeMins() > 0 ){
            entries.add(new PieEntry(stats.getOrangeMins(), mContext.getString(R.string.title_orange)));
            colors.add(ContextCompat.getColor(mContext, R.color.orange_stats));
        }
        if ( stats.getOtherMins() > 0 ){
            entries.add(new PieEntry(stats.getOtherMins(), mContext.getString(R.string.title_other)));
            colors.add(ContextCompat.getColor(mContext, R.color.blue_stats));
            //colors.add(Color.rgb(192, 255, 140));
        }

        PieDataSet set = new PieDataSet(entries, mContext.getString(R.string.title_mins_stats));

        set.setDrawIcons(false);
        set.setSliceSpace(3f);
        set.setIconsOffset(new MPPointF(0, 40));
        set.setSelectionShift(5f);

        set.setColors(colors);

        PieData data = new PieData(set);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        mTotalMins = stats.getTotalMins();
        mChart.setCenterText(mContext.getString(R.string.title_total_mins)
                + mTotalMins + " m\n"
                + mContext.getString(R.string.title_click_color_hint));

        // to refresh using animate OR invalidate
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
//        mChart.invalidate();
    }

    public void displayEmptyDataMessage(){
        mChart.clear();
        mChart.setNoDataText(mContext.getString(R.string.title_empty_data));
            /*// if I want add style for empty message
            Paint p = mChart.getPaint(Chart.PAINT_INFO);
            p.setTextSize(...);
            p.setColor(...);
            p.setTypeface(...);*/
    }

    // if I want add style for centre TextView
    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        PieEntry entry = (PieEntry) e;
        mChart.setCenterText(entry.getLabel() + "\n"
                + ((int)entry.getValue()) + " m");
    }

    @Override
    public void onNothingSelected() {
        mChart.setCenterText(mContext.getString(R.string.title_total_mins)
                + mTotalMins + " m\n"
                + mContext.getString(R.string.title_click_color_hint));
    }
}
