package com.skynet.basketassistant.UI.Widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.skynet.basketassistant.R;

/**
 * Created by USUARIO on 12/09/2014.
 */
public class ShootButtonWidget extends RelativeLayout {

    private View rootView;
    private ImageButton iButton;
    private int shootvalue;

    public ShootButtonWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRootView(LayoutInflater.from(context).inflate(R.layout.widget_shoot_button,this));
        iButton = (ImageButton)getRootView().findViewById(R.id.iButton);
    }

    public void setButtonProperties(int shootvalue,Bitmap imagepoint){
        this.shootvalue = shootvalue;
        iButton.setImageBitmap(imagepoint);
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public ImageButton getViewListener() {
        return iButton;
    }

}
