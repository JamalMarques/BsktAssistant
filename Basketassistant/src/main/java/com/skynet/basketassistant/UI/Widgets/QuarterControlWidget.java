package com.skynet.basketassistant.UI.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.skynet.basketassistant.R;

/**
 * Created by USUARIO on 09/10/2014.
 */
public class QuarterControlWidget extends LinearLayout {

    private View rootView;

    public QuarterControlWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRootView(LayoutInflater.from(context).inflate(R.layout.widget_quarter_control,this));
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }
}
