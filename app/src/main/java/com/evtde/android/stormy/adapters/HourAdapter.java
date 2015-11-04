package com.evtde.android.stormy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.evtde.android.stormy.R;
import com.evtde.android.stormy.Weather.HourWeather;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by einars on 03/11/15.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private HourWeather[] mHours;
    private Context mContext;

    public HourAdapter(Context contexts, HourWeather[] hours) {
        mContext = contexts;
        mHours = hours;
    }


    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item, parent, false);
        HourViewHolder viewHolder = new HourViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{

        @Bind(R.id.time_label) TextView mTimeLabel;
        @Bind(R.id.summary_label) TextView mSummaryLabel;
        @Bind(R.id.temperature_label2) TextView mTemperatureLabel;
        @Bind(R.id.weather_icon1) ImageView mWeatherIcon;

        public HourViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bindHour(HourWeather hourWeather) {
            mTimeLabel.setText(hourWeather.gerHour());
            mSummaryLabel.setText(hourWeather.getSummary());
            mTemperatureLabel.setText(hourWeather.getTemperature() + "");
            mWeatherIcon.setImageResource(hourWeather.getIconId());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String time = mTimeLabel.getText().toString();
            String temperature = mTemperatureLabel.getText().toString();
            String summary = mSummaryLabel.getText().toString();
            String message = String.format("At %s it will be %s and %s",
                    time,
                    temperature,
                    summary);
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }
}
