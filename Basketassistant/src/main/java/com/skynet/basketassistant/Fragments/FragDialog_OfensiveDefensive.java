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
 * Created by USUARIO on 22/09/2014.
 */
public class FragDialog_OfensiveDefensive extends DialogFragment implements View.OnClickListener{

    private String title;
    private String whoCall;
    private Button bOfensive,bDefensive;
    private TextView tvTitle;
    private int add_or_remove;

    private onCompleteOfDefDialogListener dialogListener;

    public static interface onCompleteOfDefDialogListener{
        public void onCompleteOfDefDialog_add(String type,String whoCall);
        public void onCompleteOfDefDialog_remove(String type,String whoCall);
    }

    public FragDialog_OfensiveDefensive(){/**Empty constructor*/}

    public static FragDialog_OfensiveDefensive getInstance(String fragDialogTitle,String whoCall,int add_or_remove){
        FragDialog_OfensiveDefensive fragDialog = new FragDialog_OfensiveDefensive();
        Bundle bun = new Bundle();
        bun.putString(Constants.FRAGDIALOG_TITLE,fragDialogTitle);
        bun.putString(Constants.WHO_CALL,whoCall);
        bun.putInt(Constants.ADD_OR_REMOVE,add_or_remove);
        fragDialog.setArguments(bun);
        return fragDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            this.dialogListener = (onCompleteOfDefDialogListener)activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement onCompleteOfDefDialogListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragdialog_ofensive_defensive, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        bOfensive = (Button)view.findViewById(R.id.bOfensive);
        bDefensive = (Button)view.findViewById(R.id.bDefensive);
        bOfensive.setOnClickListener(this);
        bDefensive.setOnClickListener(this);

        title = getArguments().getString(Constants.FRAGDIALOG_TITLE);
        tvTitle.setText(title);
        whoCall = getArguments().getString(Constants.WHO_CALL);
        add_or_remove = getArguments().getInt(Constants.ADD_OR_REMOVE);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(add_or_remove == Constants.MODE_ADD) {
            if (view == bOfensive) {
                this.dialogListener.onCompleteOfDefDialog_add(Constants.OFENSIVE, whoCall);
                this.dismiss();
            } else if (view == bDefensive) {
                this.dialogListener.onCompleteOfDefDialog_add(Constants.DEFENSIVE, whoCall);
                this.dismiss();
            }
        }else{
            if(add_or_remove == Constants.MODE_REMOVE){
                if(view == bOfensive) {
                    this.dialogListener.onCompleteOfDefDialog_remove(Constants.OFENSIVE, whoCall);
                    this.dismiss();
                }else if(view == bDefensive){
                    this.dialogListener.onCompleteOfDefDialog_remove(Constants.DEFENSIVE, whoCall);
                    this.dismiss();
                }
            }
        }
    }
}
