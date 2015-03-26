package com.skynet.basketassistant.Weather;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.PendingRequestListener;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.WearableClasses.SendToDataLayerThread;

/**
 * Created by yamil.marques on 3/26/15.
 */
public class OpenWeatherListener implements PendingRequestListener<OpenWeather> {

    private GoogleApiClient googleApiClient;

    public OpenWeatherListener(GoogleApiClient googleApiClient){
        this.googleApiClient = googleApiClient;
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

            Double temperatura = openWeather.getMain().getTemp();
            String ciudad = openWeather.getName();

            DataMap dataMap = new DataMap();
            /*dataMap.putDouble(Constants.MAP_TEMPERATURE, temperatura);
            dataMap.putString(Constants.MAP_CITY,ciudad);*/
            if(googleApiClient != null) {
                new SendToDataLayerThread(Constants.WEARABLE_DATA_PATH_1, dataMap, googleApiClient).start();
            }
        }

    }

}