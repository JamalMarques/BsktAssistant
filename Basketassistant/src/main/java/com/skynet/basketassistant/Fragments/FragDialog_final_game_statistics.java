package com.skynet.basketassistant.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.skynet.basketassistant.Modelo.Partido;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

/**
 * Created by Jamal on 13/11/2014.
 */
public class FragDialog_final_game_statistics extends DialogFragment implements View.OnClickListener {

    private String title,myTeamName,opponentTeamName;
    private Partido game;

    public static FragDialog_final_game_statistics getInstance(String title,Partido game,String myTeamName,String opponentTeamName){
        FragDialog_final_game_statistics fragdialog = new FragDialog_final_game_statistics();
        Bundle bun = new Bundle();
        bun.putString(Constants.GAME,new Gson().toJson(game));
        bun.putString(Constants.MYTEAM,myTeamName);
        bun.putString(Constants.OPPONENTTEAM,opponentTeamName);
        bun.putString(Constants.FRAGDIALOG_TITLE,title);
        fragdialog.setArguments(bun);
        return fragdialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragdialog_final_game_statistics, container, false);

        title = getArguments().getString(Constants.FRAGDIALOG_TITLE);
        myTeamName = getArguments().getString(Constants.MYTEAM);
        opponentTeamName = getArguments().getString(Constants.OPPONENTTEAM);
        /*
        game = make magic with the GSON
        */

        

        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
