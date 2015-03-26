package com.example.yamilmarques.wearable;

import com.example.yamilmarques.wearable.Others.Constants;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by yamil.marques on 3/16/15.
 */
public class ListenerDataService extends WearableListenerService {

    //private static final String WEARABLE_DATA_PATH_1 = "/wearable_data";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        DataMap dataMap;
        for (DataEvent event : dataEvents) {
            // Check the data type
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // Check the data path
                String path = event.getDataItem().getUri().getPath();
                if (path.equals(Constants.WEARABLE_DATA_PATH_1)) {

                    /*dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                    String actionNumber = dataMap.getString(Constants.MAP_ACTION_NUMBER);
                    boolean isActionUp = dataMap.getBoolean(Constants.MAP_IS_ACTION_UP);
                    int widgetMode = dataMap.getInt(Constants.MAP_WIDGET_MODE);
                    int colorMode = dataMap.getInt(Constants.MAP_COLOR_MODE);
                    String percentajeChange = dataMap.getString(Constants.MAP_PERCENTAJE_CHANGE);

                    DigitalWatchFaceService.globActions = actionNumber;
                    DigitalWatchFaceService.isActionUp = isActionUp;
                    DigitalWatchFaceService.widgetMode = widgetMode;
                    DigitalWatchFaceService.colorMode = colorMode;
                    DigitalWatchFaceService.percentajeActionChange = Double.valueOf(percentajeChange);*/

                }
            }
        }
    }

}
