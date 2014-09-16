package com.skynet.basketassistant.UI.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USUARIO on 11/09/2014.
 */
public class BoxOfPlayersWidget extends RelativeLayout {

    private View rootView;
    private List<PlayerBoxWidget> listPlayerWidget;
    private LinearLayout linear1,linear2,linear3;

    public BoxOfPlayersWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRootView(LayoutInflater.from(context).inflate(R.layout.widget_box_of_players,this));
        linear1 = (LinearLayout)getRootView().findViewById(R.id.linear1);
        linear2 = (LinearLayout)getRootView().findViewById(R.id.linear2);
        linear3 = (LinearLayout)getRootView().findViewById(R.id.linear3);

        populateList();

    }

    public void populateList(){
        PlayerBoxWidget w1 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer1);
        PlayerBoxWidget w2 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer2);
        PlayerBoxWidget w3 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer3);
        PlayerBoxWidget w4 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer4);
        PlayerBoxWidget w5 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer5);
        PlayerBoxWidget w6 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer6);
        PlayerBoxWidget w7 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer7);
        PlayerBoxWidget w8 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer8);
        PlayerBoxWidget w9 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer9);
        PlayerBoxWidget w10 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer10);
        PlayerBoxWidget w11 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer11);
        PlayerBoxWidget w12 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer12);
        PlayerBoxWidget w13 = (PlayerBoxWidget)getRootView().findViewById(R.id.wplayer13);

        listPlayerWidget = new ArrayList<PlayerBoxWidget>();
        listPlayerWidget.add(w1);
        listPlayerWidget.add(w2);
        listPlayerWidget.add(w3);
        listPlayerWidget.add(w4);
        listPlayerWidget.add(w5);
        listPlayerWidget.add(w6);
        listPlayerWidget.add(w7);
        listPlayerWidget.add(w8);
        listPlayerWidget.add(w9);
        listPlayerWidget.add(w10);
        listPlayerWidget.add(w11);
        listPlayerWidget.add(w12);
        listPlayerWidget.add(w13);
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public List<PlayerBoxWidget> getListPlayerWidget() {
        return listPlayerWidget;
    }

    public void setPlayers(List<Jugador> listplayers,int numberOfPlayers){
        if(numberOfPlayers > 5) {
            if (numberOfPlayers < 11)
                linear3.setVisibility(View.GONE);
        }else {
            linear2.setVisibility(View.GONE);
        }

        for (int i=0; i< listplayers.size(); i++){
            listPlayerWidget.get(i).setPlayer(listplayers.get(i));  //Setting player to the diferents widgets
        }
    }

    public void setOnPlayersWidgetsClickListener(OnClickListener event){
        for (int i=0;i < listPlayerWidget.size() ; i++){
            listPlayerWidget.get(i).setOnClickListenerWidget(event);  //Set the OnClick event from activity container
        }
    }
}
