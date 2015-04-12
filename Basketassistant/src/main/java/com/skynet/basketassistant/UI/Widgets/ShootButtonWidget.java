package com.skynet.basketassistant.UI.Widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

/**
 * Created by USUARIO on 12/09/2014.
 */
public class ShootButtonWidget extends RelativeLayout {

    private View rootView;
    private Button iButton;
    private int shootvalue;

    public ShootButtonWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRootView(LayoutInflater.from(context).inflate(R.layout.widget_shoot_button,this));
        iButton = (Button)getRootView().findViewById(R.id.iButton);
    }

    public void setButtonProperties(int shootvalue){
        this.shootvalue = shootvalue;
        switch (shootvalue){
            case Constants.SIMPLE_SHOOT_VALUE:
                    iButton.setText(getContext().getString(R.string.simple_shoot_btn));
                break;
            case Constants.DOUBLE_SHOOT_VALUE:
                    iButton.setText(getContext().getString(R.string.double_shoot_btn));
                break;
            case Constants.TRIPLE_SHOOT_VALUE:
                    iButton.setText(getContext().getString(R.string.triple_shoot_btn));
                break;
            default:
                    iButton.setText("BAD CONFIGURATION");
                break;
        }
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public Button getViewListener() {
        return iButton;
    }

}
