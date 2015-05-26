package com.skynet.basketassistant.Weather;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.PendingRequestListener;
import com.skynet.basketassistant.Activities.WearableConfigurationActivity;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.WearableClasses.SendToDataLayerThread;

import java.text.DecimalFormat;

/**
 * Created by yamil.marques on 3/26/15.
 */
public class OpenWeatherListener implements PendingRequestListener<OpenWeather> {

    private GoogleApiClient googleApiClient;
    private TextView tvCity;
    private TextView tvTemperature;

    public OpenWeatherListener(GoogleApiClient googleApiClient){
        this.googleApiClient = googleApiClient;
    }

    public OpenWeatherListener(GoogleApiClient googleApiClient,TextView tvCity, TextView tvTemperature){
        this.googleApiClient = googleApiClient;
        this.tvCity = tvCity;
        this.tvTemperature = tvTemperature;
    }

    @Override
    public void onRequestNotFound() {

    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Log.d(spiceException.getMessage(), spiceException.getLocalizedMessage());
    }

    @Override
    public void onRequestSuccess(OpenWeather openWeather) {
        if (openWeather != null)
        {

            Double temperature = openWeather.getMain().getTemp();
            String city = openWeather.getName();
            if(tvCity != null){
                tvCity.setText(city);
            }
            if (tvTemperature != null){
                DecimalFormat df = new DecimalFormat("#");
                tvTemperature.setText(df.format(temperature)+"°");
            }
            DataMap dataMap = new DataMap();
            dataMap.putDouble(Constants.MAP_TEMPERATURE, temperature);
            dataMap.putString(Constants.MAP_CITY,city);
            if(googleApiClient != null) {
                new SendToDataLayerThread(Constants.WEARABLE_DATA_PATH_1, dataMap, googleApiClient).start();
            }
        }

    }

}