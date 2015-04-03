package com.skynet.basketassistant.Fragments;

import android.graphics.drawable.ColorDrawable;
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
    private TextView tvQ1E1,tvQ2E1,tvQ3E1,tvQ4E1,tvQExtE1,tvTotalP,tvTotalPOp,tvStatus,tvTitle,tvQ1E2,tvQ2E2,tvQ3E2,tvQ4E2,tvQExtE2;
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
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(false);

        title = getArguments().getString(Constants.FRAGDIALOG_TITLE);
        myTeamName = getArguments().getString(Constants.MYTEAM);
        opponentTeamName = getArguments().getString(Constants.OPPONENTTEAM);

        String gameJson = getArguments().getString(Constants.GAME);
        Gson converter = new Gson();
        Type listType = new TypeToken<Partido>(){}.getType();
        game = (Partido) converter.fromJson(gameJson,listType);

        //Setting atributes
        ((TextView)view.findViewById(R.id.tvTeam1Name)).setText(myTeamName);
        ((TextView)view.findViewById(R.id.tvTeam2Name)).setText(opponentTeamName);

        tvQ1E1 = (TextView)view.findViewById(R.id.tvQ1E1);
        tvQ2E1 = (TextView)view.findViewById(R.id.tvQ2E1);
        tvQ3E1 = (TextView)view.findViewById(R.id.tvQ3E1);
        tvQ4E1 = (TextView)view.findViewById(R.id.tvQ4E1);
        tvQExtE1 = (TextView)view.findViewById(R.id.tvExtE1);
        tvQ1E2 = (TextView)view.findViewById(R.id.tvQ1E2);
        tvQ2E2 = (TextView)view.findViewById(R.id.tvQ2E2);
        tvQ3E2 = (TextView)view.findViewById(R.id.tvQ3E2);
        tvQ4E2 = (TextView)view.findViewById(R.id.tvQ4E2);
        tvQExtE2 = (TextView)view.findViewById(R.id.tvExtE2);
        tvStatus = (TextView)view.findViewById(R.id.tvStatus);
        tvTotalP = (TextView)view.findViewById(R.id.tvE1Points);
        tvTotalPOp = (TextView)view.findViewById(R.id.tvE2Points);
        tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        bContinue = (Button)view.findViewById(R.id.bContinue);
        bContinue.setOnClickListener(this);

        //Team 1
        tvQ1E1.setText(game.getPunt_q1_e1()+"");
        tvQ2E1.setText(game.getPunt_q2_e1()+"");
        tvQ3E1.setText(game.getPunt_q3_e1()+"");
        tvQ4E1.setText(game.getPunt_q4_e1()+"");
        if(game.getPunt_ext_e1() > 0){
            tvQExtE1.setVisibility(View.VISIBLE);
            tvQExtE1.setText(game.getPunt_ext_e1()+"");
        }
       //Team 2
        tvQ1E2.setText(game.getPunt_q1_e2()+"");
        tvQ2E2.setText(game.getPunt_q2_e2()+"");
        tvQ3E2.setText(game.getPunt_q3_e2()+"");
        tvQ4E2.setText(game.getPunt_q4_e2()+"");
        if(game.getPunt_ext_e2() > 0){
            tvQExtE2.setVisibility(View.VISIBLE);
            tvQExtE2.setText(game.getPunt_ext_e2()+"");
        }
        tvTotalP.setText(game.getPuntos_E1()+"");
        tvTotalPOp.setText(game.getPuntos_E2()+"");


        if( game.GameResult() == Partido.VICTORY ){
            tvStatus.setTextColor(getActivity().getResources().getColor(R.color.VerdeClaro_1));
            tvStatus.setText(getActivity().getString(R.string.Victory));
        }else{
            if( game.GameResult() == Partido.DEFEAT) {
                tvStatus.setTextColor(getActivity().getResources().getColor(R.color.Rojo_1));
                tvStatus.setText(getActivity().getString(R.string.Defeat));
            }else{
                tvStatus.setTextColor(getActivity().getResources().getColor(R.color.OrangeButton1));
                tvStatus.setText(getActivity().getString(R.string.Draw));
            }
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
