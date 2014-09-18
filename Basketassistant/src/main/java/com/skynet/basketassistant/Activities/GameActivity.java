package com.skynet.basketassistant.Activities;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Falta;
import com.skynet.basketassistant.Modelo.Lanzamiento;
import com.skynet.basketassistant.Modelo.Rebote;
import com.skynet.basketassistant.Modelo.Robo;
import com.skynet.basketassistant.Modelo.Tapon;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.UI.Widgets.AditionalButtonWidget;
import com.skynet.basketassistant.UI.Widgets.BoxOfPlayersWidget;
import com.skynet.basketassistant.UI.Widgets.PlayerBoxWidget;
import com.skynet.basketassistant.UI.Widgets.ShootButtonWidget;
import com.skynet.basketassistant.UI.Widgets.StatisticsBoxWidget;

import java.util.List;

public class GameActivity extends BaseActivity implements View.OnClickListener,View.OnLongClickListener {

    private Equipo myTeam;
    private String enemyTeam;

    private BoxOfPlayersWidget boxOfPlayersW;
    private PlayerBoxWidget playerTouched = null;
    private StatisticsBoxWidget statisticsWidget;
    private AditionalButtonWidget reboundButton,stealButton,blockButton,foulButton;
    private ShootButtonWidget simplePointWidget,doublePointWidget,triplePointWidget;

    private List<Lanzamiento> shootList;
    private List<Rebote> reboundList;
    private List<Robo> stealList;
    private List<Tapon> blockList;
    private List<Falta> foulList;


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
           reboundBehavior();
       }else
           if( view == stealButton.getViewListener()){ //Press Steal button
              stealBehavior();
           }else
               if( view == blockButton.getViewListener()){ //Press Block button
                   blockBehavior();
               }else
                   if( view == foulButton.getViewListener()){ //Press Foul button
                       foulBehavior();
                   }else
                       if( view == simplePointWidget.getViewListener()){ //Press simpleShoot button
                           simpleshootBehavior();
                       }else
                           if( view == doublePointWidget.getViewListener()){ //Press doubleShoot button
                               doubleshootBehavior();
                           }else
                               if( view == triplePointWidget.getViewListener()){ //Press tripleShoot button
                                   tripleshootBehavior();
                               }else { //Search for player touched!
                                   for (int i=0;i < boxOfPlayersW.getListPlayerWidget().size(); i++){
                                       if( view == boxOfPlayersW.getListPlayerWidget().get(i).getViewListener()){ //Player has been touched!
                                            //change background color
                                            //set playertouched atribute
                                           Toast.makeText(this,i+" touched",Toast.LENGTH_SHORT).show();
                                           i = boxOfPlayersW.getListPlayerWidget().size()-1; //Break for!
                                       }
                                   }
                               }

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    private void reboundBehavior(){
        Toast.makeText(this,"rebound button toucked",Toast.LENGTH_SHORT).show();
    }
    private void stealBehavior(){
        Toast.makeText(this,"steal button toucked",Toast.LENGTH_SHORT).show();
    }
    private void blockBehavior(){
        Toast.makeText(this,"block button toucked",Toast.LENGTH_SHORT).show();
    }
    private void foulBehavior(){
        Toast.makeText(this,"foul button toucked",Toast.LENGTH_SHORT).show();
    }
    private void simpleshootBehavior(){
        Toast.makeText(this,"simplePoint button toucked",Toast.LENGTH_SHORT).show();
    }
    private void doubleshootBehavior(){
        Toast.makeText(this,"doublePoint button toucked",Toast.LENGTH_SHORT).show();
    }
    private void tripleshootBehavior(){
        Toast.makeText(this,"triplePoint button toucked",Toast.LENGTH_SHORT).show();
    }




}
