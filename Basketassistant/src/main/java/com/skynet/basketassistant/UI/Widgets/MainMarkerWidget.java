package com.skynet.basketassistant.UI.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skynet.basketassistant.R;

/**
 * Created by Jamal on 09/10/2014.
 */
public class MainMarkerWidget extends LinearLayout {

    private View rootView;
    private TextView tvGlobalPoints, tvGlobalFoulsInQuarter;
    private int points = 0,fouls = 0;

    public MainMarkerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRootView(LayoutInflater.from(context).inflate(R.layout.widget_main_marker,this));
        tvGlobalPoints = (TextView) getRootView().findViewById(R.id.tvGlobalPoints);
        tvGlobalFoulsInQuarter = (TextView) getRootView().findViewById(R.id.tvGlobalFoulsInQuarter);
        Refresh();
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    private void Refresh(){
        tvGlobalPoints.setText(String.valueOf(this.points));
        tvGlobalFoulsInQuarter.setText(String.valueOf(this.fouls));
    }

    public void addPoints(int points){
        this.points += points;
        Refresh();
    }


    public void removePoints(int points){
        if(this.points-points < 0)
            this.points = 0;
        else
            this.points -= points;
        Refresh();
    }

    public void addFouls(int fouls){
        if(this.fouls + fouls >= 5){
            this.fouls = 5;
            //change color
            tvGlobalFoulsInQuarter.setTextColor(getResources().getColor(R.color.Rojo_1));
        }
        else
            this.fouls += fouls;
        Refresh();
    }

    public void removeFouls(int fouls){
        if(this.fouls-fouls < 0)
            this.fouls = 0;
        else {
            this.fouls -= fouls;
            tvGlobalFoulsInQuarter.setTextColor(null);
        }
        Refresh();
    }

    public int getGlobalPoints(){
        return this.points;
    }
    public int getFoulsOfQuarter(){
        return this.fouls;
    }

    public void changeQuarter(int foulsOfQuarter){
        if(foulsOfQuarter > 5)
            this.fouls = 5;
        else
            this.fouls = foulsOfQuarter;
        Refresh();
    }

}
