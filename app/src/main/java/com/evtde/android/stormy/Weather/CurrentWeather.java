package com.evtde.android.stormy.Weather;

import com.evtde.android.stormy.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class CurrentWeather {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;



    public String getFormatedTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = simpleDateFormat.format(dateTime);

        return timeString;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId() {

        return Forecast.getIconId(mIcon);
    }

    public double getPrecipChance() {
        return Math.round(mPrecipChance * 100);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getTemperature() {
        double celsius = (mTemperature -32) * 5 / 9;
        return (int)Math.round(celsius);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }


}
