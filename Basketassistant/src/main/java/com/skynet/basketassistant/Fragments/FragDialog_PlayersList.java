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
import android.widget.ListView;
import android.widget.TextView;

import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

import java.util.List;

/**
 * Created by USUARIO on 28/10/2014.
 */
public class FragDialog_PlayersList extends DialogFragment implements ListView.OnItemClickListener {

    private String title;
    private int teamId = 0;
    private ListView lvPlayers;
    private OnItemClicked dListener;


    public static interface OnItemClicked{
        public void onItemClicked(int player_id);
    }

    public static FragDialog_PlayersList getInstance(String title,int teamId){
        FragDialog_PlayersList fd = new FragDialog_PlayersList();
        Bundle bun = new Bundle();
        bun.putInt(Constants.TEAM_ID, teamId);
        bun.putString(Constants.FRAGDIALOG_TITLE, title);
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
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        tvTitle.setText(getArguments().getString(Constants.FRAGDIALOG_TITLE));
        teamId = getArguments().getInt(Constants.TEAM_ID);
        lvPlayers = (ListView)view.findViewById(R.id.lvPlayers);
        lvPlayers.setOnItemClickListener(this);



        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

}
