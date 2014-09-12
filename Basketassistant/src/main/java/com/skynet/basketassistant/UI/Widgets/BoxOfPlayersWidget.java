package com.skynet.basketassistant.UI.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.R;

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
        setRootView(LayoutInflater.from(context).inflate(R.layout.box_of_players_widget,this));
        linear1 = (LinearLayout)getRootView().findViewById(R.id.linear1);
        linear2 = (LinearLayout)getRootView().findViewById(R.id.linear2);
        linear3 = (LinearLayout)getRootView().findViewById(R.id.linear3);
        populateList();

    }

    private void populateList(){
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer1));
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer2));
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer3));
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer4));
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer5));
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer6));
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer7));
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer8));
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer9));
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer10));
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer11));
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer12));
        listPlayerWidget.add((PlayerBoxWidget)getRootView().findViewById(R.id.wplayer13));
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

    public void setListPlayerWidget(List<PlayerBoxWidget> listPlayerWidget) {
        this.listPlayerWidget = listPlayerWidget;
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
