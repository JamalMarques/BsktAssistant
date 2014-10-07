package com.skynet.basketassistant.Fragments;

import android.app.Activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

/**
 * Created by Jamal on 20/09/2014.
 */
public class FragDialog_ScoreOrNot extends DialogFragment implements View.OnClickListener{

    private OnCompleteShootDialogListener dListener;
    private Button bScored,bFailded;
    private int constant_shoot,add_or_remove_shoot;
    private String title;

    public FragDialog_ScoreOrNot(){/*Empty constructor*/}

    public static interface OnCompleteShootDialogListener {
        public abstract void onCompleteShootDialog_Add(int status,int constant_shoot);
        public abstract void onCompleteShootDialogRemove(int status,int constant_shoot);
    }

    @Override
    public void onAttach(Activity activity) {
        try{
            this.dListener = (OnCompleteShootDialogListener)activity;
            super.onAttach(activity);
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnCompleteShootDialogListener");
        }
    }

    public static FragDialog_ScoreOrNot getInstance(String title,int constant_shoot,int add_or_remove_shoot){
        FragDialog_ScoreOrNot fsn = new FragDialog_ScoreOrNot();
        Bundle bun = new Bundle();
        bun.putInt(Constants.CONSTANT_SHOOT,constant_shoot);
        bun.putInt(Constants.ADD_OR_REMOVE, add_or_remove_shoot);
        bun.putString(Constants.FRAGDIALOG_TITLE,title);
        fsn.setArguments(bun);
        return fsn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragdialog_score_or_not, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        bScored = (Button)view.findViewById(R.id.bScored);
        bFailded = (Button)view.findViewById(R.id.bFailed);
        bScored.setOnClickListener(this);
        bFailded.setOnClickListener(this);

        this.constant_shoot = getArguments().getInt(Constants.CONSTANT_SHOOT);
        this.add_or_remove_shoot = getArguments().getInt(Constants.ADD_OR_REMOVE);
        this.title = getArguments().getString(Constants.FRAGDIALOG_TITLE);
        TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        return view;
    }


    @Override
    public void onClick(View view) {

        if(add_or_remove_shoot == Constants.MODE_ADD) {

            if (view == bScored) {
                this.dListener.onCompleteShootDialog_Add(Constants.SHOOT_SCORED, constant_shoot);
                this.dismiss();
            } else {
                if (view == bFailded) {
                    this.dListener.onCompleteShootDialog_Add(Constants.SHOOT_FAILED, constant_shoot);
                    this.dismiss();
                }
            }
        }else{
            if(add_or_remove_shoot == Constants.MODE_REMOVE){
                if(view == bScored){
                    this.dListener.onCompleteShootDialogRemove(Constants.SHOOT_SCORED, constant_shoot);
                    this.dismiss();
                }else{
                    if( view == bFailded){
                        this.dListener.onCompleteShootDialogRemove(Constants.SHOOT_FAILED,constant_shoot);
                        this.dismiss();
                    }
                }

            }
        }
    }
}
