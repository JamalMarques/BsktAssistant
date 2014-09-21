package com.skynet.basketassistant.Fragments;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.skynet.basketassistant.R;

/**
 * Created by Jamal on 20/09/2014.
 */
public class FragDialog_ScoreOrNot extends DialogFragment {

    public FragDialog_ScoreOrNot(){/*Empty constructor*/}

    public static FragDialog_ScoreOrNot getInstance(){
        FragDialog_ScoreOrNot fsn = new FragDialog_ScoreOrNot();
        Bundle bun = new Bundle();
        fsn.setArguments(bun);
        return fsn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragdialog_score_or_not, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        return view;
    }
}
