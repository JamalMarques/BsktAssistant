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
    private ImageButton pointButton;
    private ImageButton lessButton;
    private int shootvalue;

    public ShootButtonWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRootView(LayoutInflater.from(context).inflate(R.layout.widget_shoot_button,this));
        pointButton = (ImageButton)getRootView().findViewById(R.id.ibPoint);
        lessButton = (ImageButton)getRootView().findViewById(R.id.ibLess);
    }

    public void setButtonProperties(int shootvalue,Bitmap imagepoint,Bitmap imageless){
        this.shootvalue = shootvalue;
        pointButton.setImageBitmap(imagepoint);
        lessButton.setImageBitmap(imageless);
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public ImageButton getPointButton() {
        return pointButton;
    }
    public ImageButton getLessButton() {
        return lessButton;
    }

}
