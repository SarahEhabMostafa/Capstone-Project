package com.sarahehabm.carbcalculator.main.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.sarahehabm.carbcalculator.R;
import com.sarahehabm.carbcalculator.common.database.CarbCounterContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sarah E. Mostafa on 09-May-16.
 */
public abstract class BasePagerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = BasePagerFragment.class.getSimpleName();

    protected LineChartView chartView;
    protected TextView textViewEmpty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        initLoader();

        textViewEmpty = (TextView) rootView.findViewById(R.id.textView_empty);
        chartView = (LineChartView) rootView.findViewById(R.id.barchart);

        chartView.setXAxis(false)
                .setYAxis(false)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYLabels(YController.LabelPosition.NONE);

        return rootView;
    }

    protected abstract void initLoader();

    @Override
    public abstract Loader<Cursor> onCreateLoader(int id, Bundle args);

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data == null || data.getCount()==0 || data.isClosed() || data.isLast()){
            showEmptyChart();
            return;
        }

        if(data.isAfterLast()) {
            if(!data.moveToFirst())
                return;
        }

        Calendar calendarStart = getStartRange(),
                calendarEnd = getEndRange();

        Date dateStart = new Date(calendarStart.getTimeInMillis()),
                dateEnd = new Date(calendarEnd.getTimeInMillis());

        if(chartView.isShown())
            chartView.dismiss();

        LineSet lineSet = new LineSet();
        while (data.moveToNext()) {
            int carbs = data.getInt(data.getColumnIndex(CarbCounterContract.MealEntry.COLUMN_TOTAL_CARBS));
            String timestamp = data.getString(data.getColumnIndex(CarbCounterContract.MealEntry.COLUMN_TIMESTAMP));
            long timeStamp = Long.parseLong(timestamp);
            Date date = new Date(timeStamp);
            boolean isToday = false;

            if(date.after(dateStart) && date.before(dateEnd))
                isToday = true;

            Log.v(TAG, new SimpleDateFormat().format(new Date(timeStamp)));
            if(isToday) {
                Point point = new Point("", carbs);
                lineSet.addPoint(point);
            }
        }

        if(lineSet.size() < 1) {
            showEmptyChart();
            return;
        } else {
            chartView.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
        }
        lineSet.setThickness(4);
        lineSet.setColor(chartView.getContext().getResources().getColor(R.color.colorAccent));

        try {
            chartView.addData(lineSet);
        } catch (IllegalArgumentException e) {
            chartView.notifyDataUpdate();
        }
        chartView.show();
    }

    public void showEmptyChart() {
        chartView.setVisibility(View.GONE);
        textViewEmpty.setVisibility(View.VISIBLE);
    }

    public abstract Calendar getStartRange();

    public abstract Calendar getEndRange();

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
