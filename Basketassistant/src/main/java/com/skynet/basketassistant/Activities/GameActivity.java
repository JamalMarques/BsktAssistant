package com.skynet.basketassistant.Activities;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBJugadores;
import com.skynet.basketassistant.Fragments.FragDialog_OfensiveDefensive;
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
import com.skynet.basketassistant.UI.Widgets.PlayerStatisticsBoxWidget;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends BaseActivity implements View.OnClickListener,View.OnLongClickListener,FragDialog_ScoreOrNot.OnCompleteShootDialogListener,
                                                            FragDialog_OfensiveDefensive.onCompleteOfDefDialogListener
{

    //Necesary data
    private Equipo myTeam;
    private String enemyTeam;
    private List<Jugador> teamPlayers = new ArrayList<Jugador>();

    private BoxOfPlayersWidget boxOfPlayersW;
    private PlayerBoxWidget playerTouched = null;
    private PlayerStatisticsBoxWidget playerStatisticsWidget;
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
        playerStatisticsWidget = (PlayerStatisticsBoxWidget)findViewById(R.id.statisticsBox);
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
        simplePointWidget.getViewListener().setOnLongClickListener(this);
        doublePointWidget.getViewListener().setOnLongClickListener(this);
        triplePointWidget.getViewListener().setOnLongClickListener(this);
        simplePointWidget.setButtonProperties(1, BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.ic_launcher));
        doublePointWidget.setButtonProperties(2, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher));
        triplePointWidget.setButtonProperties(3, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher));

        //Aditional Buttons
        reboundButton.getViewListener().setOnClickListener(this);
        stealButton.getViewListener().setOnClickListener(this);
        blockButton.getViewListener().setOnClickListener(this);
        foulButton.getViewListener().setOnClickListener(this);
        //---
        reboundButton.getViewListener().setOnLongClickListener(this);
        stealButton.getViewListener().setOnLongClickListener(this);
        blockButton.getViewListener().setOnLongClickListener(this);
        foulButton.getViewListener().setOnLongClickListener(this);

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
                                               playerStatisticsWidget.changePlayer(playerTouched.getPlayer(), shootList, reboundList, stealList, blockList, foulList);
                                           }else {
                                               playerTouched.statePressed(false);
                                               playerTouched = null;
                                               playerStatisticsWidget.reset();
                                           }
                                           i = boxOfPlayersW.getListPlayerWidget().size()-1; //Break for!
                                       }
                                   }
                               }

    }

    @Override
    public boolean onLongClick(View view) {

        if(isPlayerSelected()) {
            if (view == reboundButton.getViewListener()) { //Press Rebound button
                reboundBehaviorLong();
            } else if (view == stealButton.getViewListener()) { //Press Steal button
                stealBehaviorLong();
            } else if (view == blockButton.getViewListener()) { //Press Block button
                blockBehaviorLong();
            } else if (view == foulButton.getViewListener()) { //Press Foul button
                foulBehaviorLong();
            } else if (view == simplePointWidget.getViewListener()) { //Press simpleShoot button
                simpleshootBehaviorLong();
            } else if (view == doublePointWidget.getViewListener()) { //Press doubleShoot button
                doubleshootBehaviorLong();
            } else if (view == triplePointWidget.getViewListener()) { //Press tripleShoot button
                tripleshootBehaviorLong();
            }
        }else
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();

        return true;
    }

    private boolean isPlayerSelected(){
        boolean is = (playerTouched != null)? true : false;
        return is;
    }

    private void showScoreOrNotDialog(String title,int constant_shoot,int add_or_remove_shoot){
        FragDialog_ScoreOrNot fragdialog = FragDialog_ScoreOrNot.getInstance(title,constant_shoot,add_or_remove_shoot);
        fragdialog.show(getSupportFragmentManager(), Constants.FRAGMENT_DIALOG_SCORE_OR_NOT);
    }

    private void showOfensiveDefensiveDialog(String title,String whoCall,int add_or_remove){
        FragDialog_OfensiveDefensive fragd = FragDialog_OfensiveDefensive.getInstance(title,whoCall,add_or_remove);
        fragd.show(getSupportFragmentManager(), Constants.FRAGMENT_DIALOG_OFENSIVE_DEFENSIVE);
    }


    private void reboundBehavior(){
        if(isPlayerSelected()){
            showOfensiveDefensiveDialog(getString(R.string.Rebound),Constants.REBOUND_CALL,Constants.MODE_ADD);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }
    private void reboundBehaviorLong(){
        //removeFromRebounds(playerTouched.getPlayer().getId());
        showOfensiveDefensiveDialog(getString(R.string.DeleteRebound),Constants.REBOUND_CALL,Constants.MODE_REMOVE);
    }

    private void stealBehavior(){
        if(isPlayerSelected()){
            Robo newSteal = new Robo(0,playerTouched.getPlayer().getId(),0);
            stealList.add(newSteal);
            playerStatisticsWidget.addSteals(1);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }
    private void stealBehaviorLong(){
        removeFromSteals(playerTouched.getPlayer().getId());
    }

    private void blockBehavior(){
        if(isPlayerSelected()){
            Tapon newBlock = new Tapon(0,playerTouched.getPlayer().getId(),0);
            blockList.add(newBlock);
            playerStatisticsWidget.addBlocks(1);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }
    private void blockBehaviorLong(){
        removeFromBlocks(playerTouched.getPlayer().getId());
    }

    private void foulBehavior(){
        if(isPlayerSelected()){
            showOfensiveDefensiveDialog(getString(R.string.Foul),Constants.FOUL_CALL,Constants.MODE_ADD);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }
    private void foulBehaviorLong(){
        //removeFromFouls(playerTouched.getPlayer().getId());
        showOfensiveDefensiveDialog(getString(R.string.DeleteFoul),Constants.FOUL_CALL,Constants.MODE_REMOVE);
    }

    // -------------  SHOOTS --------------

    private void simpleshootBehavior(){
        if(isPlayerSelected()){
            showScoreOrNotDialog(getString(R.string.Shoot),Constants.SIMPLE_SHOOT,Constants.MODE_ADD);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }
    private void simpleshootBehaviorLong(){
        if(isPlayerSelected()){
            showScoreOrNotDialog(getString(R.string.RemoveShoot),Constants.SIMPLE_SHOOT,Constants.MODE_REMOVE);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }

    private void doubleshootBehavior(){
        if(isPlayerSelected()){
            showScoreOrNotDialog(getString(R.string.Shoot),Constants.DOUBLE_SHOOT,Constants.MODE_ADD);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }
    private void doubleshootBehaviorLong(){
        if(isPlayerSelected()){
            showScoreOrNotDialog(getString(R.string.RemoveShoot),Constants.DOUBLE_SHOOT,Constants.MODE_REMOVE);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }

    private void tripleshootBehavior(){
        if(isPlayerSelected()){
            showScoreOrNotDialog(getString(R.string.Shoot),Constants.TRIPLE_SHOOT,Constants.MODE_ADD);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }
    private void tripleshootBehaviorLong(){
        if(isPlayerSelected()){
            showScoreOrNotDialog(getString(R.string.RemoveShoot),Constants.TRIPLE_SHOOT,Constants.MODE_REMOVE);
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCompleteShootDialog_Add(int status, int constant_shoot) {
        Lanzamiento shoot;
        switch (constant_shoot){
            case Constants.SIMPLE_SHOOT:
                    shoot = new Lanzamiento(status,Constants.SHOOT_TYPE_SIMPLE,Constants.SIMPLE_SHOOT_VALUE,0,playerTouched.getPlayer().getId());
                    shootList.add(shoot);
                    if(status == Constants.SHOOT_SCORED)
                        playerStatisticsWidget.addPoints(Constants.SIMPLE_SHOOT_VALUE);
                    playerStatisticsWidget.addTotalPoints(Constants.SIMPLE_SHOOT_VALUE);
                break;
            case Constants.DOUBLE_SHOOT:
                    shoot = new Lanzamiento(status,Constants.SHOOT_TYPE_DOUBLE,Constants.DOUBLE_SHOOT_VALUE,0,playerTouched.getPlayer().getId());
                    shootList.add(shoot);
                    if(status == Constants.SHOOT_SCORED)
                        playerStatisticsWidget.addPoints(Constants.DOUBLE_SHOOT_VALUE);
                    playerStatisticsWidget.addTotalPoints(Constants.DOUBLE_SHOOT_VALUE);
                break;
            case Constants.TRIPLE_SHOOT:
                    shoot = new Lanzamiento(status,Constants.SHOOT_TYPE_TRIPLE,Constants.TRIPLE_SHOOT_VALUE,0,playerTouched.getPlayer().getId());
                    shootList.add(shoot);
                    if(status == Constants.SHOOT_SCORED)
                        playerStatisticsWidget.addPoints(Constants.TRIPLE_SHOOT_VALUE);
                    playerStatisticsWidget.addTotalPoints(Constants.TRIPLE_SHOOT_VALUE);
                break;
        }
    }


    @Override
    public void onCompleteShootDialogRemove(int status, int constant_shoot) {
        int deleteflag=0;
        switch (constant_shoot){
            case Constants.SIMPLE_SHOOT:
                for (int i=0; i < shootList.size() ; i++){
                    if(shootList.get(i).getEfectivo() == status && shootList.get(i).getTipoLanzamiento().equals(Constants.SHOOT_TYPE_SIMPLE) && shootList.get(i).getJugador_id() == playerTouched.getPlayer().getId()){
                        if(shootList.get(i).getEfectivo() == Constants.SHOOT_SCORED)
                            playerStatisticsWidget.removePoints(Constants.SIMPLE_SHOOT_VALUE);
                        shootList.remove(i);
                        playerStatisticsWidget.removeTotalPoints(Constants.SIMPLE_SHOOT_VALUE);
                        deleteflag=1;
                        break;
                    }
                }
                break;
            case Constants.DOUBLE_SHOOT:
                for (int i=0; i < shootList.size() ; i++){
                    if(shootList.get(i).getEfectivo() == status && shootList.get(i).getTipoLanzamiento().equals(Constants.SHOOT_TYPE_DOUBLE) && shootList.get(i).getJugador_id() == playerTouched.getPlayer().getId()){
                        if(shootList.get(i).getEfectivo() == Constants.SHOOT_SCORED)
                            playerStatisticsWidget.removePoints(Constants.DOUBLE_SHOOT_VALUE);
                        shootList.remove(i);
                        playerStatisticsWidget.removeTotalPoints(Constants.DOUBLE_SHOOT_VALUE);
                        deleteflag = 1;
                        break;
                    }
                }
                break;
            case Constants.TRIPLE_SHOOT:
                for (int i=0; i < shootList.size() ; i++){
                    if(shootList.get(i).getEfectivo() == status && shootList.get(i).getTipoLanzamiento().equals(Constants.SHOOT_TYPE_TRIPLE) && shootList.get(i).getJugador_id() == playerTouched.getPlayer().getId()){
                        if(shootList.get(i).getEfectivo() == Constants.SHOOT_SCORED)
                            playerStatisticsWidget.removePoints(Constants.TRIPLE_SHOOT_VALUE);
                        shootList.remove(i);
                        playerStatisticsWidget.removeTotalPoints(Constants.TRIPLE_SHOOT_VALUE);
                        deleteflag = 1;
                        break;
                    }
                }
                break;
        }
        if(deleteflag == 0)
            Toast.makeText(this,getString(R.string.NoShootToDelete),Toast.LENGTH_SHORT).show();
    }

    // ------------------------------------

    @Override
    public void onCompleteOfDefDialog_add(String type,String whoCall) {

        if(whoCall.equals(Constants.REBOUND_CALL)) { //Come from REBOUND_CALL
            Rebote rebound = new Rebote(0,playerTouched.getPlayer().getId(),0,type); //TYPE HAVE : OFENSIVE OR DEFENSIVE CONSTANTS COMMING FROM DIALOG
            reboundList.add(rebound);
            playerStatisticsWidget.addRebounds(1);
        }else {
            if(whoCall.equals(Constants.FOUL_CALL)){ //Come from FOUL_CALL
                Falta foul = new Falta(0,0,playerTouched.getPlayer().getId(),type); //TYPE HAVE : OFENSIVE OR DEFENSIVE CONSTANTS COMMING FROM DIALOG
                foulList.add(foul);
                playerStatisticsWidget.addFouls(1);
            }
        }
    }

    @Override
    public void onCompleteOfDefDialog_remove(String type, String whoCall) {
        //filter "who call" to know from who list remove it
        if(whoCall.equals(Constants.REBOUND_CALL)){
            for(int i=reboundList.size()-1; i >= 0; i--){
                if(reboundList.get(i).getTiporeb().equals(type) && reboundList.get(i).getJugador_id() == playerTouched.getPlayer().getId()) {
                    reboundList.remove(i);
                    playerStatisticsWidget.removeRebounds(1);
                    i = -1;
                }
            }
        }else{
            if(whoCall.equals(Constants.FOUL_CALL)){
                for(int i=foulList.size()-1; i >= 0; i--){
                    if(foulList.get(i).getTipo().equals(type) && foulList.get(i).getJugador_id() == playerTouched.getPlayer().getId()){
                        foulList.remove(i);
                        playerStatisticsWidget.removeFouls(1);
                        i = -1;
                    }
                }
            }
        }
    }


    private void removeFromRebounds(int player_id){
        int flag = 0;
        if(reboundList.size() > 0) {
            for (int j = reboundList.size()-1; j >= 0; j--) {
                if (reboundList.get(j).getJugador_id() == player_id) {
                    flag = 1;
                    reboundList.remove(j);
                    playerStatisticsWidget.removeRebounds(1);
                    j = -1; //Break for
                }
            }
        }
        if(flag == 0)
            Toast.makeText(this,getString(R.string.NoHaveRebounds),Toast.LENGTH_SHORT).show();
    }

    private void removeFromSteals(int player_id){
        int flag=0;
        if(stealList.size() > 0) {
            for (int i = stealList.size()-1; i >= 0; i--) {
                if (stealList.get(i).getJugador_id() == player_id) {
                    flag = 1;
                    stealList.remove(i);
                    playerStatisticsWidget.removeSteals(1);
                    i = -1; //Break for
                }
            }
        }
        if(flag == 0)
            Toast.makeText(this,getString(R.string.NoHaveRebounds),Toast.LENGTH_SHORT).show();
    }

    private void removeFromBlocks(int player_id){
        int flag=0;
        if(blockList.size() > 0) {
            for (int i = blockList.size()-1; i >= 0; i--) {
                if (blockList.get(i).getJugador_id() == player_id) {
                    flag = 1;
                    blockList.remove(i);
                    playerStatisticsWidget.removeBlocks(1);
                    i = -1; //Break for
                }
            }
        }
        if(flag == 0)
            Toast.makeText(this,getString(R.string.NoHaveRebounds),Toast.LENGTH_SHORT).show();
    }

    private void removeFromFouls(int player_id){
        int flag=0;
        if(foulList.size() > 0) {
            for (int i = foulList.size()-1; i >= 0; i--) {
                if (foulList.get(i).getJugador_id() == player_id) {
                    flag = 1;
                    foulList.remove(i);
                    playerStatisticsWidget.removeFouls(1);
                    i = -1; //Break for
                }
            }
        }
        if(flag == 0)
            Toast.makeText(this,getString(R.string.NoHaveRebounds),Toast.LENGTH_SHORT).show();
    }

    private void removeFromShoots(int player_id,String type_shoot,int scoredOrNot){  //typle_shoot = constant shoot simple double o triple
        int flag=0;
        for(int i=shootList.size(); i>=0 ; i--){
            if(shootList.get(i).getJugador_id() == player_id && shootList.get(i).getTipoLanzamiento().equals(type_shoot) && shootList.get(i).getEfectivo() == scoredOrNot){
                flag = 1;
                shootList.remove(i);
                if(type_shoot.equals(Constants.SHOOT_TYPE_SIMPLE))
                    playerStatisticsWidget.removePoints(Constants.SIMPLE_SHOOT_VALUE);
                else
                    if(type_shoot.equals(Constants.SHOOT_TYPE_DOUBLE))
                        playerStatisticsWidget.removePoints(Constants.DOUBLE_SHOOT_VALUE);
                    else
                        playerStatisticsWidget.removePoints(Constants.TRIPLE_SHOOT_VALUE);
                i = -1;  //For Breaker
            }
        }
        if(flag == 0)
            Toast.makeText(this,getString(R.string.NoHaveThisShot),Toast.LENGTH_SHORT).show();
    }

}
