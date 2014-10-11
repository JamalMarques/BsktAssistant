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
public class QuarterControlWidget extends LinearLayout {

    private View rootView;
    private ImageButton ibRightButton,ibLeftButton;
    private TextView tvQuarter;
    private int quarter = 1;

    public QuarterControlWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRootView(LayoutInflater.from(context).inflate(R.layout.widget_quarter_control,this));
        ibRightButton = (ImageButton)getRootView().findViewById(R.id.ibRightButton);
        ibLeftButton = (ImageButton)getRootView().findViewById(R.id.ibLeftButton);
        tvQuarter = (TextView)getRootView().findViewById(R.id.tvQuarter);
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    private void refresh(){
        if(quarter == Constants.MAX_NUMER_OF_QUARTERS)
            tvQuarter.setText(getContext().getString(R.string.QuarterExtension));
        else
            tvQuarter.setText(String.valueOf(quarter));
    }

    public void addQuarter(){
        if(quarter++ > 4){
            quarter = Constants.MAX_NUMER_OF_QUARTERS;
        }else {
            quarter++;
        }
        refresh();
    }

    public void subtractQuarter(){
        if (quarter-- < 1)
            quarter = 1;
        else
            quarter--;
        refresh();
    }

    public int getActualQuarter(){
        return quarter;
    }
}
