package com.skynet.basketassistant.UI.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skynet.basketassistant.Modelo.Asistencia;
import com.skynet.basketassistant.Modelo.Falta;
import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.Modelo.Lanzamiento;
import com.skynet.basketassistant.Modelo.Rebote;
import com.skynet.basketassistant.Modelo.Robo;
import com.skynet.basketassistant.Modelo.Tapon;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

import java.util.List;

/**
 * Created by yamil.marques on 12/09/2014.
 */
public class PlayerStatisticsBoxWidget extends RelativeLayout {

    private View rootView;
    private TextView tvPoints;
    private TextView tvRebounds;
    private TextView tvSteals;
    private TextView tvBlocks;
    private TextView tvFouls;
    private TextView tvTotalPoints;
    private TextView tvAssistances;

    private int points=0;
    private int totalPoints=0;
    private int rebounds=0;
    private int steals=0;
    private int blocks=0;
    private int fouls=0;
    private int assistances=0;

    public PlayerStatisticsBoxWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRootView(LayoutInflater.from(context).inflate(R.layout.widget_statics_box,this));
        tvPoints = (TextView)getRootView().findViewById(R.id.tvPoints);
        tvRebounds = (TextView)getRootView().findViewById(R.id.tvRebounds);
        tvSteals = (TextView)getRootView().findViewById(R.id.tvSteals);
        tvBlocks = (TextView)getRootView().findViewById(R.id.tvBlocks);
        tvFouls = (TextView)getRootView().findViewById(R.id.tvFouls);
        tvTotalPoints = (TextView)getRootView().findViewById(R.id.tvTotalPoints);
        tvAssistances = (TextView)getRootView().findViewById(R.id.tvAssistances);


        initiate();
    }

    private void initiate(){
        refresh();
    }

    private void refresh(){
        getTvPoints().setText(String.valueOf(getPoints()));
        getTvRebounds().setText(String.valueOf(getRebounds()));
        getTvSteals().setText(String.valueOf(getSteals()));
        getTvBlocks().setText(String.valueOf(getBlocks()));
        getTvFouls().setText(String.valueOf(getFouls()));
        getTvTotalPoints().setText(String.valueOf(getTotalPoints()));
        getTvAssistances().setText(String.valueOf(getAssistances()));
    }

    public void reset(){
        points=0;
        rebounds=0;
        steals=0;
        blocks=0;
        fouls=0;
        totalPoints=0;
        assistances=0;
        refresh();
    }

    public void changePlayer(Jugador player,List<Lanzamiento> shootsList,List<Rebote> reboundsList,List<Robo> stealsList,
                             List<Tapon> blocksList,List<Falta> foulsList, List<Asistencia> assitList) {

        reset();

        for (int i = 0; i < shootsList.size(); i++) {
            if (shootsList.get(i).getJugador_id() == player.getId()) {
                totalPoints = getTotalPoints() + shootsList.get(i).getValor();
                if (shootsList.get(i).getEfectivo() == Constants.SHOOT_SCORED) {
                    points += shootsList.get(i).getValor();
                }
            }
        }

        for (int i = 0; i < reboundsList.size(); i++) {
            if (reboundsList.get(i).getJugador_id() == player.getId())
                rebounds++;
        }
        for (int i = 0; i < stealsList.size(); i++) {
            if (stealsList.get(i).getJugador_id() == player.getId())
                steals++;
        }
        for (int i = 0; i < blocksList.size(); i++) {
            if (blocksList.get(i).getJugador_id() == player.getId())
                blocks++;
        }
        for (int i = 0; i < foulsList.size(); i++) {
            if (foulsList.get(i).getJugador_id() == player.getId())
                fouls++;
        }
        for (int i = 0; i < assitList.size(); i++) {
            if (assitList.get(i).getJugador_id() == player.getId())
                assistances++;
        }

            refresh();

    }

    public void addPoints(int points){
        this.points += points;
        refresh();
    }
    public void addTotalPoints(int points){
        this.totalPoints += points;
        refresh();
    }
    public void addRebounds(int rebounds){
        this.rebounds += rebounds;
        refresh();
    }
    public void addSteals(int steals){
        this.steals += steals;
        refresh();
    }
    public void addBlocks(int blocks){
        this.blocks += blocks;
        refresh();
    }
    public void addFouls(int fouls){
        this.fouls += fouls;
        refresh();
    }
    public void addAssistances(int assist){
        this.assistances += assist;
        refresh();
    }


    public void removePoints(int points){
        if( (this.points - points) <= 0)
            this.points = 0;
        else
            this.points -= points;
        refresh();
    }
    public void removeTotalPoints(int points){
        int asd = totalPoints;
        if( (this.totalPoints - points) <= 0)
            this.totalPoints = 0;
        else
            this.totalPoints -= points;
        refresh();
    }
    public void removeRebounds(int rebounds){
        if( (this.rebounds - rebounds) <= 0)
            this.rebounds = 0;
        else
            this.rebounds -= rebounds;
        refresh();
    }
    public void removeSteals(int steals){
        if( (this.steals - steals) <= 0)
            this.steals = 0;
        else
            this.steals -= steals;
        refresh();
    }
    public void removeBlocks(int blocks){
        if( (this.blocks - blocks) <= 0)
            this.blocks = 0;
        else
            this.blocks -= blocks;
        refresh();
    }
    public void removeFouls(int fouls){
        if( (this.fouls - fouls) <= 0)
            this.fouls = 0;
        else
            this.fouls -= fouls;
        refresh();
    }
    public void removeAssistances(int assist){
        if( (this.assistances - assist) <= 0){
            this.assistances = 0;
        }else{
            this.assistances -= assist;
        }
        refresh();
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public TextView getTvPoints() {
        return tvPoints;
    }


    public TextView getTvRebounds() {
        return tvRebounds;
    }


    public TextView getTvSteals() {
        return tvSteals;
    }


    public TextView getTvBlocks() {
        return tvBlocks;
    }

    public TextView getTvFouls() {
        return tvFouls;
    }

    public int getPoints() {
        return points;
    }

    public int getRebounds() {
        return rebounds;
    }

    public int getSteals() {
        return steals;
    }

    public int getBlocks() {
        return blocks;
    }

    public int getFouls() {
        return fouls;
    }

    public int getAssistances() { return assistances; }

    public TextView getTvTotalPoints() {
        return tvTotalPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public TextView getTvAssistances() {
        return tvAssistances;
    }


}
