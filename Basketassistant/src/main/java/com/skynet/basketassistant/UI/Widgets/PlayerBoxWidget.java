package com.skynet.basketassistant.UI.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.Otros.Manejo_Imagenes;
import com.skynet.basketassistant.R;

/**
 * Created by Jamal on 11/09/2014.
 */
public class PlayerBoxWidget extends RelativeLayout {

    private View rootView;
    private LinearLayout totalLayoutContainer;
    private ImageView playerImage;
    private TextView tvPlayerNumber;
    private Jugador player;

    public PlayerBoxWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRootView(LayoutInflater.from(context).inflate(R.layout.widget_player_box,this));

        totalLayoutContainer = (LinearLayout)getRootView().findViewById(R.id.totalLayoutContainer);
        playerImage = (ImageView)getRootView().findViewById(R.id.imageViewPlayer);
        tvPlayerNumber = (TextView)getRootView().findViewById(R.id.textViewPlayerNumber);
    }

    public void setPlayer(Jugador player){
        this.player = player;
        try {
            playerImage.setImageBitmap(Manejo_Imagenes.Cubo_Rotar_Rotacion(player.getImagen_url()));
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        tvPlayerNumber.setText(String.valueOf(player.getNumero()));
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public ImageView getPlayerImage() {
        return playerImage;
    }

    public TextView getTvPlayerNumber() {
        return tvPlayerNumber;
    }

    public Jugador getPlayer() {
        return player;
    }

    public LinearLayout getViewListener(){ return totalLayoutContainer; }
}
