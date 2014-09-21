package com.skynet.basketassistant.Activities;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBJugadores;
import com.skynet.basketassistant.Datos.DBLanzamientos;
import com.skynet.basketassistant.Fragments.FragDialog_ScoreOrNot;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Falta;
import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.Modelo.Lanzamiento;
import com.skynet.basketassistant.Modelo.Rebote;
import com.skynet.basketassistant.Modelo.Robo;
import com.skynet.basketassistant.Modelo.Tapon;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.UI.Widgets.AditionalButtonWidget;
import com.skynet.basketassistant.UI.Widgets.BoxOfPlayersWidget;
import com.skynet.basketassistant.UI.Widgets.PlayerBoxWidget;
import com.skynet.basketassistant.UI.Widgets.ShootButtonWidget;
import com.skynet.basketassistant.UI.Widgets.StatisticsBoxWidget;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends BaseActivity implements View.OnClickListener,View.OnLongClickListener,FragDialog_ScoreOrNot.OnCompleteDialogListener {

    //Necesary data
    private Equipo myTeam;
    private String enemyTeam;
    private List<Jugador> teamPlayers = new ArrayList<Jugador>();

    private BoxOfPlayersWidget boxOfPlayersW;
    private PlayerBoxWidget playerTouched = null;
    private StatisticsBoxWidget statisticsWidget;
    private AditionalButtonWidget reboundButton,stealButton,blockButton,foulButton;
    private ShootButtonWidget simplePointWidget,doublePointWidget,triplePointWidget;

    //Statistics
    private List<Lanzamiento> shootList = new ArrayList<Lanzamiento>();
    private List<Rebote> reboundList = new ArrayList<Rebote>();
    private List<Robo> stealList = new ArrayList<Robo>();
    private List<Tapon> blockList = new ArrayList<Tapon>();
    private List<Falta> foulList = new ArrayList<Falta>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadAtributtes();
        loadPlayers();
    }

    private void loadAtributtes(){

        //Loading Team
        String team_name = getIntent().getExtras().getString(Constants.TEAM_NAME);
        DBEquipos dbe = new DBEquipos(this);
        dbe.Modolectura();
        myTeam = dbe.DameEquipo(team_name);
        dbe.Cerrar();

        //Loading Team Players
        DBJugadores dbj = new DBJugadores(this);
        dbj.Modolectura();
        teamPlayers = dbj.DameListaJugadoresEquipo(myTeam.getId());
        dbj.Cerrar();

        Toast.makeText(this,teamPlayers.size()+"",Toast.LENGTH_LONG).show();

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


    private void loadPlayers(){
        if( teamPlayers != null && boxOfPlayersW != null){
            boxOfPlayersW.setPlayers(teamPlayers,teamPlayers.size());
        }
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
                                           if(boxOfPlayersW.getListPlayerWidget().get(i) != playerTouched ) {
                                               if (isPlayerSelected()) {
                                                   playerTouched.statePressed(false);
                                                   playerTouched = null;
                                               }
                                               playerTouched = boxOfPlayersW.getListPlayerWidget().get(i);
                                               playerTouched.statePressed(true);
                                           }else {
                                               playerTouched.statePressed(false);
                                               playerTouched = null;
                                           }
                                           i = boxOfPlayersW.getListPlayerWidget().size()-1; //Break for!
                                       }
                                   }
                               }

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    private boolean isPlayerSelected(){
        boolean is = (playerTouched != null)? true : false;
        return is;
    }

    private void showScoreOrNotDialog(int constant_shoot){
        FragDialog_ScoreOrNot fragdialog = FragDialog_ScoreOrNot.getInstance(constant_shoot);
        fragdialog.show(getFragmentManager(),Constants.FRAGMENT_DIALOG_SCORE_OR_NOT);
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

    // -------------  SHOOTS --------------

    private void simpleshootBehavior(){
        if(isPlayerSelected()){
            showScoreOrNotDialog(Constants.SIMPLE_SHOOT);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }
    private void doubleshootBehavior(){
        if(isPlayerSelected()){
            showScoreOrNotDialog(Constants.DOUBLE_SHOOT);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }
    private void tripleshootBehavior(){
        if(isPlayerSelected()){
            showScoreOrNotDialog(Constants.TRIPLE_SHOOT);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onComplete(int status, int constant_shoot) {

    }


    // ------------------------------------
}
