package com.skynet.basketassistant.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.Receivers.RefreshReceiver;
import com.skynet.basketassistant.SpiceManager.SpiceService;
import com.skynet.basketassistant.Weather.OpenWeather;
import com.skynet.basketassistant.Weather.OpenWeatherListener;
import com.skynet.basketassistant.Weather.OpenWeatherRequest;

import java.util.Calendar;

public class WearableConfigurationActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    public static final String SHARED_PREFERENCES_NAME = "Preferences";

    private ImageButton buttonBack;
    private TextView tvCity,tvTemperature;
    private Button buttonConfirm;
    private Spinner refreshSpinner;
    private Location lastLocation;
    private String lastLong="-58.368282";
    private String lastLat="-34.602961";
    private String lastRequestCacheKey;
    private GoogleApiClient googleApiClient, positionGoogleApiClient;
    private AlarmManager alarmManager;
    private SharedPreferences sharedPreferences;
    private SpiceManager spiceManager = new SpiceManager(SpiceService.class);

    protected SpiceManager getSpiceManager()
    {
        return spiceManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearable_configuration);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,0);

        //Building a Google Api Client
        googleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        positionGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        alarmManager = (AlarmManager)getSystemService(this.ALARM_SERVICE);

        buttonBack = (ImageButton)findViewById(R.id.bBack);
        buttonBack.setOnClickListener(this);
        buttonConfirm = (Button)findViewById(R.id.bConfirm);
        buttonConfirm.setOnClickListener(this);
        refreshSpinner = (Spinner)findViewById(R.id.refreshSpinner);
        refreshSpinner.setAdapter(ArrayAdapter.createFromResource(
                this, R.array.time_to_refresh, android.R.layout.simple_spinner_item));
        refreshSpinner.setSelection(sharedPreferences.getInt(Constants.SHARED_PREFERENCES_TIME_TO_REFRESH,1)-1);

        //Refresh or not AlarmManager
        if( (refreshSpinner.getSelectedItemPosition()+1) != sharedPreferences.getInt(Constants.SHARED_PREFERENCES_TIME_TO_REFRESH,0)){
            GenerateAlarm(refreshSpinner.getSelectedItemPosition());
        }

        tvCity = (TextView)findViewById(R.id.tvCity);
        tvTemperature = (TextView)findViewById(R.id.tvTemperature);
    }

    @Override
    protected void onStart() {
        super.onStart();
        positionGoogleApiClient.connect();
        googleApiClient.connect();
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        if( googleApiClient != null && googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
        if( positionGoogleApiClient != null && positionGoogleApiClient.isConnected()){
            positionGoogleApiClient.disconnect();
        }
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(positionGoogleApiClient);
        if (lastLocation != null) {
            lastLat = String.valueOf(lastLocation.getLatitude());
            lastLong = String.valueOf(lastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        if(view == buttonBack){
            onBackPressed();
        }else{
            if(view == buttonConfirm){
                //Refresh the alarm
                GenerateAlarm(refreshSpinner.getSelectedItemPosition());
                OpenWeatherRequest weatherRequest = new OpenWeatherRequest(lastLat,lastLong,"metric");
                lastRequestCacheKey = weatherRequest.createCacheKey();
                spiceManager.execute(weatherRequest,lastRequestCacheKey,DurationInMillis.ONE_HOUR,new OpenWeatherListener(googleApiClient,tvCity,tvTemperature));
            }
        }
    }

    private void GenerateAlarm(int alarmPositionTime){
        Calendar cal = Calendar.getInstance();
        long frequencyTime = 0;
        switch (alarmPositionTime){
            case Constants.HOURS_1:
                frequencyTime = Constants.HALF_HOUR * 2;
                break;
            case Constants.HOURS_2:
                frequencyTime = Constants.HALF_HOUR * 4;
                break;
            case Constants.HOURS_3:
                frequencyTime = Constants.HALF_HOUR * 6;
                break;
            default:
                frequencyTime = Constants.HALF_HOUR;
                break;
        }
        Intent intent = new Intent(this,RefreshReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,Constants.ID_PENDING_INTENT, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),frequencyTime,pendingIntent);
        //Saving the changes
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.SHARED_PREFERENCES_TIME_TO_REFRESH,alarmPositionTime);
    }


}
