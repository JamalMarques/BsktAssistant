package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skynet.basketassistant.Modelo.Partido;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

import java.lang.reflect.Type;

/**
 * Created by Jamal on 13/11/2014.
 */
public class FragDialog_final_game_statistics extends DialogFragment implements View.OnClickListener {

    private String title,myTeamName,opponentTeamName;
    private Partido game;
    private TextView tvQ1,tvQ2,tvQ3,tvQ4,tvQExt,tvTotalP,tvTotalPOp,tvStatus,tvTitle;
    private Button bContinue;

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
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        title = getArguments().getString(Constants.FRAGDIALOG_TITLE);
        myTeamName = getArguments().getString(Constants.MYTEAM);
        opponentTeamName = getArguments().getString(Constants.OPPONENTTEAM);

        String gameJson = getArguments().getString(Constants.GAME);
        Gson converter = new Gson();
        Type listType = new TypeToken<Partido>(){}.getType();
        game = (Partido) converter.fromJson(gameJson,listType);

        //Setting atributes
        tvQ1 = (TextView)view.findViewById(R.id.tvQ1);
        tvQ2 = (TextView)view.findViewById(R.id.tvQ2);
        tvQ3 = (TextView)view.findViewById(R.id.tvQ3);
        tvQ4 = (TextView)view.findViewById(R.id.tvQ4);
        tvQExt = (TextView)view.findViewById(R.id.tvExt);
        tvStatus = (TextView)view.findViewById(R.id.tvStatus);
        tvTotalP = (TextView)view.findViewById(R.id.tvE1Points);
        tvTotalPOp = (TextView)view.findViewById(R.id.tvE2Points);
        tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        bContinue = (Button)view.findViewById(R.id.bContinue);
        bContinue.setOnClickListener(this);

        tvQ1.setText(game.getPunt_q1_e1()+"");
        tvQ1.setText(game.getPunt_q2_e1()+"");
        tvQ1.setText(game.getPunt_q3_e1()+"");
        tvQ1.setText(game.getPunt_q4_e1()+"");
        if(game.getPunt_ext_e1() > 0){
            tvQExt.setVisibility(View.VISIBLE);
            tvQExt.setText(game.getPunt_ext_e1()+"");
        }
        tvTotalP.setText(game.getPuntos_E1()+"");
        tvTotalPOp.setText(game.getPuntos_E2()+"");
        if(game.Victoria()){
            tvStatus.setText(getString(R.string.Victory));
            //change color too
        }else{
            tvStatus.setText(getString(R.string.Defeat));
            //change color too
        }


        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == bContinue){
               getActivity().finish();
        }
    }
}
