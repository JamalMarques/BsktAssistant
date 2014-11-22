package com.skynet.basketassistant.Activities;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.skynet.basketassistant.Datos.DBAsistencias;
import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBFaltas;
import com.skynet.basketassistant.Datos.DBJugadores;
import com.skynet.basketassistant.Datos.DBLanzamientos;
import com.skynet.basketassistant.Datos.DBPartidos;
import com.skynet.basketassistant.Datos.DBRebotes;
import com.skynet.basketassistant.Datos.DBRobos;
import com.skynet.basketassistant.Datos.DBTapones;
import com.skynet.basketassistant.Fragments.FragDialog_GameInformation;
import com.skynet.basketassistant.Fragments.FragDialog_OfensiveDefensive;
import com.skynet.basketassistant.Fragments.FragDialog_PlayersList;
import com.skynet.basketassistant.Fragments.FragDialog_ScoreOrNot;
import com.skynet.basketassistant.Fragments.FragDialog_YesNo;
import com.skynet.basketassistant.Fragments.FragDialog_final_game_statistics;
import com.skynet.basketassistant.Modelo.Asistencia;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Falta;
import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.Modelo.Lanzamiento;
import com.skynet.basketassistant.Modelo.Partido;
import com.skynet.basketassistant.Modelo.Rebote;
import com.skynet.basketassistant.Modelo.Robo;
import com.skynet.basketassistant.Modelo.Tapon;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.UI.Widgets.AditionalButtonWidget;
import com.skynet.basketassistant.UI.Widgets.BoxOfPlayersWidget;
import com.skynet.basketassistant.UI.Widgets.MainMarkerWidget;
import com.skynet.basketassistant.UI.Widgets.PlayerBoxWidget;
import com.skynet.basketassistant.UI.Widgets.QuarterControlWidget;
import com.skynet.basketassistant.UI.Widgets.ShootButtonWidget;
import com.skynet.basketassistant.UI.Widgets.PlayerStatisticsBoxWidget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GameActivity extends BaseActivity implements View.OnClickListener,View.OnLongClickListener,FragDialog_ScoreOrNot.OnCompleteShootDialogListener,
                                                            FragDialog_OfensiveDefensive.onCompleteOfDefDialogListener,QuarterControlWidget.OnChangeQuarterListener,
                                                            FragDialog_YesNo.OnCompleteYesNoDialogListener, FragDialog_PlayersList.OnItemClicked, FragDialog_GameInformation.OnCompleteInformation
{

    //Necesary data
    private Equipo myTeam;
    private Partido game;
    private String opponentTeamName;
    private List<Jugador> teamPlayers = new ArrayList<Jugador>();

    private BoxOfPlayersWidget boxOfPlayersW;
    private PlayerBoxWidget playerTouched = null;
    private PlayerStatisticsBoxWidget playerStatisticsWidget;
    private AditionalButtonWidget reboundButton,stealButton,blockButton,foulButton, assistanceButton;
    private ShootButtonWidget simplePointWidget,doublePointWidget,triplePointWidget;
    private MainMarkerWidget mainMarkerWidget;
    private QuarterControlWidget quarterControlWidget;
    private Button finishButton;

    //Statistics
    private List<Lanzamiento> shootList = new ArrayList<Lanzamiento>();
    private List<Rebote> reboundList = new ArrayList<Rebote>();
    private List<Robo> stealList = new ArrayList<Robo>();
    private List<Tapon> blockList = new ArrayList<Tapon>();
    private List<Falta> foulList = new ArrayList<Falta>();
    private List<Asistencia> assistancesList = new ArrayList<Asistencia>();
    private int[] opponentPointsInQuarter = new int[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadAtributtes();
        loadPlayers();
        showInformationDialog();
    }

    private void showInformationDialog(){
        FragDialog_GameInformation df = FragDialog_GameInformation.getInstance();
        df.show(getSupportFragmentManager(),Constants.FRAGMENT_DIALOG_GAME_INFORMATION);
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

        //Starting Opponent Quarters
        for(int i=0;i <= (Constants.MAX_NUMBER_OF_QUARTERS-1); i++){
            opponentPointsInQuarter[i] = 0;
        }

        boxOfPlayersW = (BoxOfPlayersWidget)findViewById(R.id.playersBox);
        playerStatisticsWidget = (PlayerStatisticsBoxWidget)findViewById(R.id.statisticsBox);
        mainMarkerWidget = (MainMarkerWidget)findViewById(R.id.mainMarkerWidget);
        quarterControlWidget = (QuarterControlWidget)findViewById(R.id.QuarterControlWidget);
        reboundButton = (AditionalButtonWidget)findViewById(R.id.reboundButton);
        stealButton = (AditionalButtonWidget)findViewById(R.id.stealButton);
        blockButton = (AditionalButtonWidget)findViewById(R.id.blockButton);
        foulButton = (AditionalButtonWidget)findViewById(R.id.foulButton);
        assistanceButton = (AditionalButtonWidget)findViewById(R.id.assistanceButton);
        simplePointWidget = (ShootButtonWidget)findViewById(R.id.simplePointWidget);
        doublePointWidget = (ShootButtonWidget)findViewById(R.id.doublePointWidget);
        triplePointWidget = (ShootButtonWidget)findViewById(R.id.triplePointWidget);
        finishButton = (Button)findViewById(R.id.finishButton);
        finishButton.setOnClickListener(this);

        boxOfPlayersW.setOnPlayersWidgetsClickListener(this);

        //Points Button
        simplePointWidget.getViewListener().setOnClickListener(this);
        doublePointWidget.getViewListener().setOnClickListener(this);
        triplePointWidget.getViewListener().setOnClickListener(this);
        simplePointWidget.getViewListener().setOnLongClickListener(this);
        doublePointWidget.getViewListener().setOnLongClickListener(this);
        triplePointWidget.getViewListener().setOnLongClickListener(this);
        simplePointWidget.setButtonProperties(1, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher));
        doublePointWidget.setButtonProperties(2, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher));
        triplePointWidget.setButtonProperties(3, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher));

        //Aditional Buttons
        reboundButton.getViewListener().setOnClickListener(this);
        stealButton.getViewListener().setOnClickListener(this);
        blockButton.getViewListener().setOnClickListener(this);
        foulButton.getViewListener().setOnClickListener(this);
        assistanceButton.getViewListener().setOnClickListener(this);
        //---
        reboundButton.getViewListener().setOnLongClickListener(this);
        stealButton.getViewListener().setOnLongClickListener(this);
        blockButton.getViewListener().setOnLongClickListener(this);
        foulButton.getViewListener().setOnLongClickListener(this);
        assistanceButton.getViewListener().setOnLongClickListener(this);


    }

    @Override
    public void onCompleteInfo(String opponentName, boolean isLocal) {
        loadGame(opponentName,isLocal);
    }

    private void loadGame(String oponentName,boolean isLocal){
        String stadium = (isLocal)? getString(R.string.Local) : getString(R.string.Visitor);
        opponentTeamName = oponentName;
        //Create the new Game in DataBase
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        DBPartidos dbg = new DBPartidos(this);
        dbg.Modoescritura();
        game = dbg.insertar(sdf.format(Calendar.getInstance().getTime()),stadium,myTeam.getId(),oponentName,0,0,0,0,0,0,0,0,0,0,0,0);  //Create it and return the id from database
        dbg.Cerrar();

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
                       if( view == assistanceButton.getViewListener()){  //Press Assistance button
                            assistanceBehaviour();
                       }else
                           if( view == simplePointWidget.getViewListener()){ //Press simpleShoot button
                               simpleshootBehavior();
                           }else
                               if( view == doublePointWidget.getViewListener()){ //Press doubleShoot button
                                   doubleshootBehavior();
                               }else
                                   if( view == triplePointWidget.getViewListener()){ //Press tripleShoot button
                                       tripleshootBehavior();
                                   }else
                                        if( view == finishButton ){
                                            finishGame();
                                        }else
                                           { //Search for player touched!
                                               for (int i=0;i < boxOfPlayersW.getListPlayerWidget().size(); i++){
                                                   if( view == boxOfPlayersW.getListPlayerWidget().get(i).getViewListener()){ //Player has been touched!
                                                       if(boxOfPlayersW.getListPlayerWidget().get(i) != playerTouched ) {
                                                           if (isPlayerSelected()) {
                                                               playerTouched.statePressed(false);
                                                               playerTouched = null;
                                                           }
                                                           playerTouched = boxOfPlayersW.getListPlayerWidget().get(i);
                                                           playerTouched.statePressed(true);
                                                           playerStatisticsWidget.changePlayer(playerTouched.getPlayer(), shootList, reboundList, stealList, blockList, foulList, assistancesList);
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
            } else if (view == assistanceButton.getViewListener()) { //Press Assistance button
                assistanceBehaviourLong();
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
        showOfensiveDefensiveDialog(getString(R.string.DeleteRebound),Constants.REBOUND_CALL,Constants.MODE_REMOVE);
    }

    private void stealBehavior(){
        if(isPlayerSelected()){
            Robo newSteal = new Robo(0,playerTouched.getPlayer().getId(),game.getId());
            stealList.add(newSteal);
            playerStatisticsWidget.addSteals(1);
            Toast.makeText(this,getString(R.string.StealAddedMessage),Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }
    private void stealBehaviorLong(){
        removeFromSteals(playerTouched.getPlayer().getId());
    }

    private void blockBehavior(){
        if(isPlayerSelected()){
            Tapon newBlock = new Tapon(0,playerTouched.getPlayer().getId(),game.getId());
            blockList.add(newBlock);
            playerStatisticsWidget.addBlocks(1);
            Toast.makeText(this,getString(R.string.BlockAddedMessage),Toast.LENGTH_SHORT).show();
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

    private void assistanceBehaviour(){
        if(isPlayerSelected()){
            Asistencia newAssist = new Asistencia(0,playerTouched.getPlayer().getId(),game.getId());
            assistancesList.add(newAssist);
            playerStatisticsWidget.addAssistances(1);
            Toast.makeText(this,getString(R.string.AssistanceAddedMessage),Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,getString(R.string.SelectPlayerError),Toast.LENGTH_SHORT).show();
        }
    }
    private void assistanceBehaviourLong(){
        removeFromAssistances(playerTouched.getPlayer().getId());
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
        int value;
        switch (constant_shoot){
            case Constants.SIMPLE_SHOOT:
                    value = Constants.SIMPLE_SHOOT_VALUE;
                    shoot = new Lanzamiento(status,Constants.SHOOT_TYPE_SIMPLE,value,game.getId(),playerTouched.getPlayer().getId(), quarterControlWidget.getActualQuarter());
                    shootList.add(shoot);
                    if(status == Constants.SHOOT_SCORED) {
                        playerStatisticsWidget.addPoints(value);
                        mainMarkerWidget.addPoints(value);
                    }
                    playerStatisticsWidget.addTotalPoints(value);
                break;
            case Constants.DOUBLE_SHOOT:
                    value = Constants.DOUBLE_SHOOT_VALUE;
                    shoot = new Lanzamiento(status,Constants.SHOOT_TYPE_DOUBLE,value,game.getId(),playerTouched.getPlayer().getId(), quarterControlWidget.getActualQuarter());
                    shootList.add(shoot);
                    if(status == Constants.SHOOT_SCORED) {
                        playerStatisticsWidget.addPoints(value);
                        mainMarkerWidget.addPoints(value);
                        FragDialog_YesNo fdYN = FragDialog_YesNo.getInstance(getString(R.string.Was_assisted),Constants.YES_NO_ASSISTANCE);
                        fdYN.show(getSupportFragmentManager(), Constants.FRAGMENT_DIALOG_YES_NO);
                    }
                    playerStatisticsWidget.addTotalPoints(value);
                break;
            case Constants.TRIPLE_SHOOT:
                    value = Constants.TRIPLE_SHOOT_VALUE;
                    shoot = new Lanzamiento(status,Constants.SHOOT_TYPE_TRIPLE,value,game.getId(),playerTouched.getPlayer().getId(), quarterControlWidget.getActualQuarter());
                    shootList.add(shoot);
                    if(status == Constants.SHOOT_SCORED) {
                        playerStatisticsWidget.addPoints(Constants.TRIPLE_SHOOT_VALUE);
                        mainMarkerWidget.addPoints(value);
                    }
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
                    if(shootList.get(i).getEfectivo() == status && shootList.get(i).getTipoLanzamiento().equals(Constants.SHOOT_TYPE_SIMPLE) && shootList.get(i).getJugador_id() == playerTouched.getPlayer().getId() && shootList.get(i).getQuarter_number() == quarterControlWidget.getActualQuarter()){
                        if(shootList.get(i).getEfectivo() == Constants.SHOOT_SCORED)
                            playerStatisticsWidget.removePoints(Constants.SIMPLE_SHOOT_VALUE);
                        shootList.remove(i);
                        playerStatisticsWidget.removeTotalPoints(Constants.SIMPLE_SHOOT_VALUE);
                        deleteflag=1;
                        Toast.makeText(this,getString(R.string.SimpleDeleteMessage),Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                break;
            case Constants.DOUBLE_SHOOT:
                for (int i=0; i < shootList.size() ; i++){
                    if(shootList.get(i).getEfectivo() == status && shootList.get(i).getTipoLanzamiento().equals(Constants.SHOOT_TYPE_DOUBLE) && shootList.get(i).getJugador_id() == playerTouched.getPlayer().getId() && shootList.get(i).getQuarter_number() == quarterControlWidget.getActualQuarter()){
                        if(shootList.get(i).getEfectivo() == Constants.SHOOT_SCORED)
                            playerStatisticsWidget.removePoints(Constants.DOUBLE_SHOOT_VALUE);
                        shootList.remove(i);
                        playerStatisticsWidget.removeTotalPoints(Constants.DOUBLE_SHOOT_VALUE);
                        deleteflag = 1;
                        Toast.makeText(this,getString(R.string.DoubleDeleteMessage),Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                break;
            case Constants.TRIPLE_SHOOT:
                for (int i=0; i < shootList.size() ; i++){
                    if(shootList.get(i).getEfectivo() == status && shootList.get(i).getTipoLanzamiento().equals(Constants.SHOOT_TYPE_TRIPLE) && shootList.get(i).getJugador_id() == playerTouched.getPlayer().getId() && shootList.get(i).getQuarter_number() == quarterControlWidget.getActualQuarter()){
                        if(shootList.get(i).getEfectivo() == Constants.SHOOT_SCORED)
                            playerStatisticsWidget.removePoints(Constants.TRIPLE_SHOOT_VALUE);
                        shootList.remove(i);
                        playerStatisticsWidget.removeTotalPoints(Constants.TRIPLE_SHOOT_VALUE);
                        deleteflag = 1;
                        Toast.makeText(this,getString(R.string.TripleDeleteMessage),Toast.LENGTH_SHORT).show();
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
            Rebote rebound = new Rebote(0,playerTouched.getPlayer().getId(),game.getId(),type); //TYPE HAVE : OFENSIVE OR DEFENSIVE CONSTANTS COMMING FROM DIALOG
            reboundList.add(rebound);
            playerStatisticsWidget.addRebounds(1);
            Toast.makeText(this,getString(R.string.ReboundAddedMessage),Toast.LENGTH_SHORT).show();
        }else {
            if(whoCall.equals(Constants.FOUL_CALL)){ //Come from FOUL_CALL
                Falta foul = new Falta(0,game.getId(),playerTouched.getPlayer().getId(),type,quarterControlWidget.getActualQuarter()); //TYPE HAVE : OFENSIVE OR DEFENSIVE CONSTANTS COMMING FROM DIALOG
                foulList.add(foul);
                playerStatisticsWidget.addFouls(1);
                mainMarkerWidget.addFouls(1);
                Toast.makeText(this,getString(R.string.FoulAddedMessage),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCompleteOfDefDialog_remove(String type, String whoCall) {
        //filter "who call" to know from who list remove it
        int flag=0;
        if(whoCall.equals(Constants.REBOUND_CALL)){
            for(int i=reboundList.size()-1; i >= 0; i--){
                if(reboundList.get(i).getTiporeb().equals(type) && reboundList.get(i).getJugador_id() == playerTouched.getPlayer().getId()) {
                    reboundList.remove(i);
                    playerStatisticsWidget.removeRebounds(1);
                    i = -1;
                    Toast.makeText(this,getString(R.string.AllDeleteMessage),Toast.LENGTH_SHORT).show();
                    flag=1;
                }
            }
            if(flag==0) Toast.makeText(this,getString(R.string.NoHaveRebounds),Toast.LENGTH_SHORT).show();
        }else{
            if(whoCall.equals(Constants.FOUL_CALL)){
                for(int i=foulList.size()-1; i >= 0; i--){
                    if(foulList.get(i).getTipo().equals(type) && foulList.get(i).getJugador_id() == playerTouched.getPlayer().getId() && foulList.get(i).getQuarter_number() == quarterControlWidget.getActualQuarter()){
                        foulList.remove(i);
                        playerStatisticsWidget.removeFouls(1);
                        i = -1;
                        Toast.makeText(this,getString(R.string.AllDeleteMessage),Toast.LENGTH_SHORT).show();
                        flag=1;
                    }
                }
                if(flag==0) Toast.makeText(this,getString(R.string.NoHaveFouls),Toast.LENGTH_SHORT).show();
            }
        }
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
            Toast.makeText(this,getString(R.string.NoHaveSteals),Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,getString(R.string.AllDeleteMessage),Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this,getString(R.string.NoHaveBlocks),Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,getString(R.string.AllDeleteMessage),Toast.LENGTH_SHORT).show();
    }

    private void removeFromAssistances(int player_id){
        int flag=0;
        if(assistancesList.size() > 0) {
            for (int i = assistancesList.size()-1; i >= 0; i--) {
                if (assistancesList.get(i).getJugador_id() == player_id) {
                    flag = 1;
                    assistancesList.remove(i);
                    playerStatisticsWidget.removeAssistances(1);
                    i = -1; //Break for
                }
            }
        }
        if(flag == 0)
            Toast.makeText(this,getString(R.string.NoHaveAssistances),Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,getString(R.string.AllDeleteMessage),Toast.LENGTH_SHORT).show();
    }


    private int foulCount(int quarter_number){
        int count_of_fouls = 0;
        for (int i=0; i < foulList.size(); i++){
            if(foulList.get(i).getQuarter_number() == quarter_number){
                if((count_of_fouls + 1) >= Constants.MAX_NUMBER_OF_FOULS_PER_QUARTER)
                    return Constants.MAX_NUMBER_OF_FOULS_PER_QUARTER;
                else
                    count_of_fouls++;
            }
        }
        return count_of_fouls;
    }

    @Override
    public void onChangeQuarter() {
        int q = quarterControlWidget.getActualQuarter();
        int fouln = foulCount(q);
        mainMarkerWidget.changeQuarter(foulCount(quarterControlWidget.getActualQuarter()));
    }

    @Override
    public void onCompleteYesNoDialog(int response, int whocall) {
        switch (whocall){
            case Constants.YES_NO_ASSISTANCE:
                if(response == Constants.YES){
                    FragDialog_PlayersList fd = FragDialog_PlayersList.getInstance(getString(R.string.Select_player),myTeam.getId(),FragDialog_PlayersList.ADD_ASSISTANCE);
                    fd.show(getSupportFragmentManager(), Constants.FRAGMENT_DIALOG_PLAYERS_LIST);
                }//else do nothing...
                break;
        }
    }


    @Override
    public void onItemClicked(int player_id, String action) { //That comes from the Players List Fragment Dialog
        if(action.equals(FragDialog_PlayersList.ADD_ASSISTANCE)){
            Asistencia asist = new Asistencia(0,player_id,game.getId());
            assistancesList.add(asist);
            Toast.makeText(this,getString(R.string.Assistance_added),Toast.LENGTH_SHORT).show();
        }
    }


    private void finishGame(){
        //Shoots
        DBLanzamientos dbl = new DBLanzamientos(this);
        dbl.SaveOnDatabase(shootList);
        //Rebounds
        DBRebotes dbr = new DBRebotes(this);
        dbr.SaveOnDatabase(reboundList);
        //Steals
        DBRobos dbro = new DBRobos(this);
        dbro.SaveOnDatabase(stealList);
        //Blocks
        DBTapones dbb = new DBTapones(this);
        dbb.SaveOnDatabase(blockList);
        //Fouls
        DBFaltas dbf = new DBFaltas(this);
        dbf.SaveOnDatabase(foulList);
        //Assistances
        DBAsistencias dba = new DBAsistencias(this);
        dba.SaveOnDatabase(assistancesList);

        updateGame();
        DBPartidos dbp = new DBPartidos(this);
        dbp.updateGame(game);

        //Show the total game statistics
        FragDialog_final_game_statistics fragDialog = FragDialog_final_game_statistics.getInstance(getString(R.string.EndOfGame),game,myTeam.getNombre(),opponentTeamName);
        fragDialog.show(getSupportFragmentManager(),Constants.FRAGMENT_DIALOG_FINAL_STATISTICS);

    }

    private void updateGame(){
        int[] pointsOfQuarterE1 = new int[5];
        int totalPointsE1=0;

        for (int i=0; i < shootList.size(); i++){  //Updating points of the quarters
            Lanzamiento shoot = shootList.get(i);
            if(shoot.getEfectivo() == Constants.SHOOT_SCORED){
                pointsOfQuarterE1[shoot.getQuarter_number()] += shoot.getValor();
                totalPointsE1 += shoot.getValor();
            }
        }

        //Setting points E1
        game.setPunt_q1_e1(pointsOfQuarterE1[1]);
        game.setPunt_q2_e1(pointsOfQuarterE1[2]);
        game.setPunt_q2_e1(pointsOfQuarterE1[3]);
        game.setPunt_q2_e1(pointsOfQuarterE1[4]);
        game.setPunt_ext_e1(pointsOfQuarterE1[5]);
        //Setting points E2 TODO (Opponent's points of quarter )
        //...
        game.setPunt_q1_e2(0);
        game.setPunt_q2_e2(0);
        game.setPunt_q2_e2(0);
        game.setPunt_q2_e2(0);
        game.setPunt_ext_e2(0);
        //Setting global points
        game.setPuntos_E1(totalPointsE1);
        game.setPuntos_E2(0); //TODO solve this! (Opponent's points of quarter)

    }


}
