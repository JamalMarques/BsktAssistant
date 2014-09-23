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

    private onCompleteOfDefDialogListener dialogListener;

    public static interface onCompleteOfDefDialogListener{
        public void onCompleteOfDefDialog(String type,String whoCall);
    }

    public FragDialog_OfensiveDefensive(){/**Empty constructor*/}

    public static FragDialog_OfensiveDefensive getInstance(String fragDialogTitle,String whoCall){
        FragDialog_OfensiveDefensive fragDialog = new FragDialog_OfensiveDefensive();
        Bundle bun = new Bundle();
        bun.putString(Constants.FRAGDIALOG_TITLE,fragDialogTitle);
        bun.putString(Constants.WHO_CALL,whoCall);
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

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == bOfensive){
            this.dialogListener.onCompleteOfDefDialog(Constants.OFENSIVE,whoCall);
            this.dismiss();
        }else
            if(view == bDefensive){
                this.dialogListener.onCompleteOfDefDialog(Constants.DEFENSIVE,whoCall);
                this.dismiss();
            }
    }
}
