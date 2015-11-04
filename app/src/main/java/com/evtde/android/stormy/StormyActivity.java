package com.evtde.android.stormy;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.evtde.android.stormy.UI.AlertDialogFragment;
import com.evtde.android.stormy.UI.DailyForecastActivity;
import com.evtde.android.stormy.UI.HourlyForecastActivity;
import com.evtde.android.stormy.Weather.CurrentWeather;
import com.evtde.android.stormy.Weather.DailyWeather;
import com.evtde.android.stormy.Weather.Forecast;
import com.evtde.android.stormy.Weather.HourWeather;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StormyActivity extends AppCompatActivity {

    public static final String TAG = StormyActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY FORECAST";

    private Forecast mForecast;
    private TextView mTemeperatureLabel;

    @Bind(R.id.time) TextView mTimeLabel;
    @Bind(R.id.temperature_label) TextView mTemperatureValue;
    @Bind(R.id.humidity_value) TextView mHumidityValue;
    @Bind(R.id.perspiration_value) TextView mPrecipValue;
    @Bind(R.id.summary_view) TextView mSummaryLabel;
    @Bind(R.id.weather_icon) ImageView mIconImageView;
    @Bind(R.id.update_view) ImageView mRefreshView;
    @Bind(R.id.update_bar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stormy);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        final double latitude = 51.5081289;
        final double longitude = -.128005;

        mRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForcast(latitude, longitude);
            }
        });

        getForcast(latitude, longitude);
    }

    private void getForcast( double latitude, double longitude) {
        String apiKey = "ee72357a776214458993b2a77d22402e";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude +
                "," + longitude;

        if (isNetworlAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                    } catch (JSONException je) {
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.unavailable_network_text,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE){
        mProgressBar.setVisibility(View.VISIBLE);
        mRefreshView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        CurrentWeather currentWeather = mForecast.getCurrentWeather();
        mTemperatureValue.setText(currentWeather.getTemperature() + "");
        mTimeLabel.setText("At " + currentWeather.getFormatedTime() + " it will be");
        mHumidityValue.setText(currentWeather.getHumidity() +"");
        mPrecipValue.setText(currentWeather.getPrecipChance() + "%");
        mSummaryLabel.setText(currentWeather.getSummary());


        Drawable drawable = ContextCompat.getDrawable(this, currentWeather.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException{
        Forecast forecast = new Forecast();

        forecast.setCurrentWeather(getCurrentDetails(jsonData));

        forecast.setHourWeather(getHourlyForecast(jsonData));
        forecast.setDailyWeather(getDailyForecast(jsonData));
        return forecast;
    }

    private DailyWeather[] getDailyForecast(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        DailyWeather[] dailyWeathers = new DailyWeather[data.length()];

        for (int i  = 0; i < data.length(); i++) {
            JSONObject jsonDay =  data.getJSONObject(i);
            DailyWeather dailyWeather = new DailyWeather();

            dailyWeather.setSummary(jsonDay.getString("summary"));
            dailyWeather.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            dailyWeather.setIcon(jsonDay.getString("icon"));
            dailyWeather.setTime(jsonDay.getLong("time"));
            dailyWeather.setTimezone(timeZone);

            dailyWeathers[i] = dailyWeather;
        }

        return dailyWeathers;
    }

    private HourWeather[] getHourlyForecast(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        HourWeather[] hourWeathers = new HourWeather[data.length()];

        for (int i  = 0; i < data.length(); i++) {
            JSONObject jsonHour = data.getJSONObject(i);
            HourWeather hourWeather = new HourWeather();

            hourWeather.setSummary(jsonHour.getString("summary"));
            hourWeather.setTemperature(jsonHour.getDouble("temperature"));
            hourWeather.setIcon(jsonHour.getString("icon"));
            hourWeather.setTime(jsonHour.getLong("time"));
            hourWeather.setTimezone(timeZone);

            hourWeathers[i] = hourWeather;
        }
        return hourWeathers;
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");

        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timeZone);

        return currentWeather;
    }

    private boolean isNetworlAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable =  true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "error dialog");
    }

    @OnClick (R.id.daily_button)
    public void startDailyForecast(View view) {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDailyWeather());
        startActivity(intent);
    }

    @OnClick (R.id.hourly_button)
    public void startHourlyForecast(View view) {
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourWeather());
        startActivity(intent);
    }
}
