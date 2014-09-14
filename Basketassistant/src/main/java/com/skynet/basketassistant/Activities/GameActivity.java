package com.skynet.basketassistant.Activities;

import android.os.Bundle;

import com.skynet.basketassistant.R;
import com.skynet.basketassistant.UI.Widgets.BoxOfPlayersWidget;
import com.skynet.basketassistant.UI.Widgets.PlayerBoxWidget;
import com.skynet.basketassistant.UI.Widgets.StatisticsBoxWidget;

public class GameActivity extends BaseActivity {

    private BoxOfPlayersWidget boxOfPlayersW;
    private PlayerBoxWidget playerTouched;
    private StatisticsBoxWidget statisticsWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        boxOfPlayersW = (BoxOfPlayersWidget)findViewById(R.id.view);
        //boxOfPlayersW.populateList();
    }
    
}
