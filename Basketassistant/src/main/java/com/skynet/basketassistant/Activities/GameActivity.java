package com.skynet.basketassistant.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.UI.Widgets.BoxOfPlayersWidget;
import com.skynet.basketassistant.UI.Widgets.PlayerBoxWidget;

public class GameActivity extends BaseActivity {

    private BoxOfPlayersWidget boxOfPlayersW;
    private PlayerBoxWidget playerTouched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        boxOfPlayersW = (BoxOfPlayersWidget)findViewById(R.id.view);
        //boxOfPlayersW.populateList();
    }
    
}
