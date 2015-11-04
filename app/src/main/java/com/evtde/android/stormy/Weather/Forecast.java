package com.evtde.android.stormy.Weather;

import com.evtde.android.stormy.R;

/**
 * Created by einars on 01/11/15.
 */
public class Forecast {
    private CurrentWeather mCurrentWeather;
    private HourWeather[] mHourWeather;
    private DailyWeather[] mDailyWeather;

    public CurrentWeather getCurrentWeather() {
        return mCurrentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        mCurrentWeather = currentWeather;
    }

    public HourWeather[] getHourWeather() {
        return mHourWeather;
    }

    public void setHourWeather(HourWeather[] hourWeather) {
        mHourWeather = hourWeather;
    }

    public DailyWeather[] getDailyWeather() {
        return mDailyWeather;
    }

    public void setDailyWeather(DailyWeather[] dailyWeather) {
        mDailyWeather = dailyWeather;
    }

    public static int getIconId(String iconString) {
        int iconId = R.drawable.clear_day;

        if (iconString.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (iconString.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (iconString.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (iconString.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (iconString.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (iconString.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (iconString.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }
}
