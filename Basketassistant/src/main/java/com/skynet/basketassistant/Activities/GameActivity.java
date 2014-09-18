package com.skynet.basketassistant.Activities;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.UI.Widgets.AditionalButtonWidget;
import com.skynet.basketassistant.UI.Widgets.BoxOfPlayersWidget;
import com.skynet.basketassistant.UI.Widgets.PlayerBoxWidget;
import com.skynet.basketassistant.UI.Widgets.ShootButtonWidget;
import com.skynet.basketassistant.UI.Widgets.StatisticsBoxWidget;

public class GameActivity extends BaseActivity implements View.OnClickListener,View.OnLongClickListener {

    private Equipo myTeam;
    private String enemyTeam;

    private BoxOfPlayersWidget boxOfPlayersW;
    private PlayerBoxWidget playerTouched = null;
    private StatisticsBoxWidget statisticsWidget;
    private AditionalButtonWidget reboundButton,stealButton,blockButton,foulButton;
    private ShootButtonWidget simplePointWidget,doublePointWidget,triplePointWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadAtributtes();




    }

    private void loadAtributtes(){
        boxOfPlayersW = (BoxOfPlayersWidget)findViewById(R.id.playersBox);
        statisticsWidget = (StatisticsBoxWidget)findViewById(R.id.statisticsBox);
        reboundButton = (AditionalButtonWidget)findViewById(R.id.reboundButton);
        stealButton = (AditionalButtonWidget)findViewById(R.id.stealButton);
        blockButton = (AditionalButtonWidget)findViewById(R.id.blockButton);
        foulButton = (AditionalButtonWidget)findViewById(R.id.foulButton);
        simplePointWidget = (ShootButtonWidget)findViewById(R.id.simplePointWidget);
        doublePointWidget = (ShootButtonWidget)findViewById(R.id.doublePointWidget);
        triplePointWidget = (ShootButtonWidget)findViewById(R.id.triplePointWidget);

        boxOfPlayersW.setOnPlayersWidgetsClickListener(this);

        //Points Button
        simplePointWidget.getViewListener().setOnClickListener(this);
        doublePointWidget.getViewListener().setOnClickListener(this);
        triplePointWidget.getViewListener().setOnClickListener(this);
        simplePointWidget.setButtonProperties(1, BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.ic_launcher));
        doublePointWidget.setButtonProperties(2, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher));
        triplePointWidget.setButtonProperties(3, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher));

        //Aditional Buttons
        reboundButton.getViewListener().setOnClickListener(this);
        stealButton.getViewListener().setOnClickListener(this);
        blockButton.getViewListener().setOnClickListener(this);
        foulButton.getViewListener().setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
       if( view == reboundButton.getViewListener()){ //Press Rebound button

       }else
           if( view == stealButton.getViewListener()){ //Press Steal button

           }else
               if( view == blockButton.getViewListener()){ //Press Block button

               }else
                   if( view == foulButton.getViewListener()){ //Press Foul button

                   }else
                       if( view == simplePointWidget.getViewListener()){ //Press simpleShoot button

                       }else
                           if( view == doublePointWidget.getViewListener()){ //Press doubleShoot button

                           }else
                               if( view == triplePointWidget.getViewListener()){ //Press tripleShoot button

                               }else { //Search for player touched!
                                   for (int i=0;i < boxOfPlayersW.getListPlayerWidget().size(); i++){
                                       if( view == boxOfPlayersW.getListPlayerWidget().get(i).getViewListener()){ //Player has been touched!



                                       }

                                   }

                               }

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}
