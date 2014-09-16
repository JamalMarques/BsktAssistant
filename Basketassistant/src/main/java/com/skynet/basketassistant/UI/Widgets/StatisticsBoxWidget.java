package com.skynet.basketassistant.UI.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skynet.basketassistant.R;

/**
 * Created by yamil.marques on 12/09/2014.
 */
public class StatisticsBoxWidget extends RelativeLayout {

    private View rootView;
    private TextView tvPoints;
    private TextView tvRebounds;
    private TextView tvSteals;
    private TextView tvBlocks;
    private TextView tvFouls;

    public StatisticsBoxWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRootView(LayoutInflater.from(context).inflate(R.layout.widget_statics_box,this));
        tvPoints = (TextView)getRootView().findViewById(R.id.tvPoints);
        tvRebounds = (TextView)getRootView().findViewById(R.id.tvRebounds);
        tvSteals = (TextView)getRootView().findViewById(R.id.tvSteals);
        tvBlocks = (TextView)getRootView().findViewById(R.id.tvBlocks);
        tvFouls = (TextView)getRootView().findViewById(R.id.tvFouls);

        initiate();
    }

    private void initiate(){
        getTvPoints().setText("0");
        getTvRebounds().setText("0");
        getTvSteals().setText("0");
        getTvBlocks().setText("0");
        getTvFouls().setText("0");
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

}
