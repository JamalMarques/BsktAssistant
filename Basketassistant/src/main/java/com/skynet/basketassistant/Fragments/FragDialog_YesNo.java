package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

/**
 * Created by USUARIO on 27/10/2014.
 */
public class FragDialog_YesNo extends DialogFragment implements View.OnClickListener {

    private String title;
    private OnCompleteYesNoDialogListener dListener;
    private Button bYes,bNo;
    private int whoCall;

    public FragDialog_YesNo(){/*Empty Constructor*/}

    public static interface OnCompleteYesNoDialogListener{
        public void onCompleteYesNoDialog(int response, int whocall);
    }

    public static FragDialog_YesNo getInstance(String title,int whocall){
        FragDialog_YesNo fd = new FragDialog_YesNo();
        Bundle bun = new Bundle();
        bun.putString(Constants.FRAGDIALOG_TITLE, title);
        bun.putInt(Constants.WHO_CALL,whocall);
        fd.setArguments(bun);
        return fd;
    }

    @Override
    public void onAttach(Activity activity) {
        try{
            this.dListener = (OnCompleteYesNoDialogListener)activity;
            super.onAttach(activity);
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnCompleteYesNoDialogListener");
        }
    }

    /*public void setListenerOnFragment(Fragment frag){
        try{
            this.dListener = (OnCompleteYesNoDialogListener)frag;
        }catch(ClassCastException e){
            throw new ClassCastException("The fragment "+frag.toString() + " must implement OnCompleteYesNoDialogListener");
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragdialog_yes_or_no, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        title = getArguments().getString(Constants.FRAGDIALOG_TITLE);
        TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        whoCall = getArguments().getInt(Constants.WHO_CALL);

        bYes = (Button)view.findViewById(R.id.bYes);
        bYes.setOnClickListener(this);
        bNo = (Button)view.findViewById(R.id.bNo);
        bNo.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == bYes){
            dListener.onCompleteYesNoDialog(Constants.YES, whoCall);
            this.dismiss();
        }else{
            if(view == bNo){
                dListener.onCompleteYesNoDialog(Constants.NO, whoCall);
                this.dismiss();
            }
        }
    }
}
