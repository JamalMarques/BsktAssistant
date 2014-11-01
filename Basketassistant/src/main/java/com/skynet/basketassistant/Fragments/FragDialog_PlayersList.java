package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.skynet.basketassistant.Adapters.ItemAdapterPlayersSimple;
import com.skynet.basketassistant.Datos.DBJugadores;
import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

import java.util.List;

/**
 * Created by USUARIO on 28/10/2014.
 */
public class FragDialog_PlayersList extends DialogFragment implements ListView.OnItemClickListener {

    //Actions
    public static final String ADD_ASSISTANCE = "ADD_ASSISTANCE";
    //posibility of add more

    private String title;
    private int teamId = 0;
    private ListView lvPlayers;
    private OnItemClicked dListener;
    private List<Jugador> playersList;
    private ItemAdapterPlayersSimple adapter;


    public static interface OnItemClicked{
        public void onItemClicked(int player_id,String action);
    }

    public static FragDialog_PlayersList getInstance(String title,int teamId,String action){
        FragDialog_PlayersList fd = new FragDialog_PlayersList();
        Bundle bun = new Bundle();
        bun.putInt(Constants.TEAM_ID, teamId);
        bun.putString(Constants.FRAGDIALOG_TITLE, title);
        bun.putString(Constants.FRAGMENT_DIALOG_ACTION, action);
        fd.setArguments(bun);
        return fd;
    }

    @Override
    public void onAttach(Activity activity) {
        try{
            this.dListener = (OnItemClicked)activity;
            super.onAttach(activity);
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnItemClicked");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragdialog_players_list, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        tvTitle.setText(getArguments().getString(Constants.FRAGDIALOG_TITLE));
        teamId = getArguments().getInt(Constants.TEAM_ID);
        lvPlayers = (ListView)view.findViewById(R.id.lvPlayers);
        lvPlayers.setOnItemClickListener(this);

        DBJugadores dbj = new DBJugadores(getActivity());
        dbj.Modolectura();
        playersList = dbj.DameListaJugadoresEquipo(teamId);
        dbj.Cerrar();

        adapter = new ItemAdapterPlayersSimple(getActivity(),playersList);
        lvPlayers.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        dListener.onItemClicked(playersList.get(position).getId(),getArguments().getString(Constants.FRAGMENT_DIALOG_ACTION));
        this.dismiss();
    }

}
