package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

/**
 * Created by Jamal on 20/09/2014.
 */
public class FragDialog_ScoreOrNot extends DialogFragment implements View.OnClickListener{

    private OnCompleteDialogListener dListener;
    private Button bScored,bFailded;
    private int constant_shoot;

    public FragDialog_ScoreOrNot(){/*Empty constructor*/}

    public static interface OnCompleteDialogListener{
        public abstract void onComplete(int status,int constant_shoot);
    }

    @Override
    public void onAttach(Activity activity) {
        try{
            this.dListener = (OnCompleteDialogListener)activity;
            super.onAttach(activity);
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    public static FragDialog_ScoreOrNot getInstance(int constant_shoot){
        FragDialog_ScoreOrNot fsn = new FragDialog_ScoreOrNot();
        Bundle bun = new Bundle();
        bun.putInt(Constants.CONSTANT_SHOOT,constant_shoot);
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


        return view;
    }


    @Override
    public void onClick(View view) {
        if(view == bScored){
            this.dListener.onComplete(Constants.SHOOT_SCORED,constant_shoot);
            this.dismiss();
        }else{
            if(bFailded == bFailded){
                this.dListener.onComplete(Constants.SHOOT_FAILED,constant_shoot);
                this.dismiss();
            }
        }
    }
}
