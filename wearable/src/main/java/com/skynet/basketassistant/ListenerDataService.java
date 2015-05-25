package com.skynet.basketassistant;

import com.skynet.basketassistant.DigitalWatchFaceService;
import com.skynet.basketassistant.Others.Constants;
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
                    dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                    String mapCity = dataMap.getString(Constants.MAP_CITY);
                    Double temperature = dataMap.getDouble(Constants.MAP_TEMPERATURE);

                    DigitalWatchFaceService.city = mapCity;
                    DigitalWatchFaceService.temperature = temperature;

                }
            }
        }
    }

}
