package com.skynet.basketassistant.UI.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

/**
 * Created by USUARIO on 09/10/2014.
 */
public class QuarterControlWidget extends LinearLayout implements View.OnClickListener {

    private OnChangeQuarterListener quarterListener;

    private View rootView;
    private ImageButton ibBottomButton, ibTopButton;
    private TextView tvQuarter;
    private int quarter = 1;

    public static interface OnChangeQuarterListener{
        public abstract void onChangeQuarter();
    }

    public QuarterControlWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRootView(LayoutInflater.from(context).inflate(R.layout.widget_quarter_control, this));
        ibBottomButton = (ImageButton)getRootView().findViewById(R.id.ibBottomButton);
        ibTopButton = (ImageButton)getRootView().findViewById(R.id.ibTopButton);
        tvQuarter = (TextView)getRootView().findViewById(R.id.tvQuarter);
        ibBottomButton.setOnClickListener(this);
        ibTopButton.setOnClickListener(this);

        try{
            this.quarterListener = (OnChangeQuarterListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException("the activity that contains this widget must implement OnChangeQuarterListener interface");
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

    private void refresh(){
        if(quarter == Constants.MAX_NUMBER_OF_QUARTERS) {
            tvQuarter.setText(getContext().getString(R.string.QuarterExtension));
            tvQuarter.setTextColor(getResources().getColor(R.color.Verde_oscuro_1));
        }
        else{
            tvQuarter.setText(String.valueOf(quarter));
            tvQuarter.setTextColor(getResources().getColor(R.color.Gris_Negro));
        }
    }

    public void addQuarter(){
        if((quarter+1) > (Constants.MAX_NUMBER_OF_QUARTERS-1)){
            quarter = Constants.MAX_NUMBER_OF_QUARTERS;
        }else {
            quarter++;
        }
        refresh();
    }

    public void subtractQuarter(){
        if ((quarter-1) < 1)
            quarter = 1;
        else
            quarter--;
        refresh();
    }

    public int getActualQuarter(){
        return quarter;
    }

    @Override
    public void onClick(View view) {
        if(view == ibBottomButton) {
            subtractQuarter();
            quarterListener.onChangeQuarter();
        }
        else {
            if (view == ibTopButton) {
                addQuarter();
                quarterListener.onChangeQuarter();
            }
        }
    }
}