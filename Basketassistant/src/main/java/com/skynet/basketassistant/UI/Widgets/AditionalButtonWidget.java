package com.skynet.basketassistant.UI.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.skynet.basketassistant.R;

/**
 * Created by USUARIO on 15/09/2014.
 */
public class AditionalButtonWidget extends LinearLayout {

    private View rootView;
    private Button iButton;

    public AditionalButtonWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRootView(LayoutInflater.from(context).inflate(R.layout.widget_aditional_button,this));
        iButton = (Button)getRootView().findViewById(R.id.iButton);
    }


    @Override
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public Button getViewListener(){ return iButton;}

    public void setButtonText(String text){
        iButton.setText(text);
    }
}
