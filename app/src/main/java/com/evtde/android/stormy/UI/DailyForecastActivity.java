package com.evtde.android.stormy.UI;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.evtde.android.stormy.R;
import com.evtde.android.stormy.StormyActivity;
import com.evtde.android.stormy.Weather.DailyWeather;
import com.evtde.android.stormy.adapters.DayAdapter;

import java.util.Arrays;

public class DailyForecastActivity extends ListActivity {

    private DailyWeather[] mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(StormyActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelables, parcelables.length, DailyWeather[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);
        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String dayOnTheWeek = mDays[position].getDayOfTheWeek();
        String conditions = mDays[position].getSummary();
        String highTemp = mDays[position].getTemperatureMax() + "";

        String message = String.format("On %s the high will be %s and it will be %s",
                dayOnTheWeek,
                 highTemp,
                conditions);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
