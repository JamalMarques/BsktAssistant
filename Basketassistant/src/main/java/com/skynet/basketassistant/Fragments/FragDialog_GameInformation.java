package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.skynet.basketassistant.R;

/**
 * Created by jamal on 31/10/2014.
 */
public class FragDialog_GameInformation extends DialogFragment implements View.OnClickListener {

    private OnCompleteInformation dListener;

    private RadioButton rbLocal,rbVisitor;
    private Button bOk;
    private EditText etOpponentName;

    public FragDialog_GameInformation(){/*Empty constructor*/}

    public static FragDialog_GameInformation getInstance(){
        FragDialog_GameInformation fd = new FragDialog_GameInformation();
        return fd;
    }

    public interface OnCompleteInformation{
        public void onCompleteInfo(String opponentName,boolean isLocal);
    }

    @Override
    public void onAttach(Activity activity) {
        try{
            this.dListener = (OnCompleteInformation)activity;
            super.onAttach(activity);
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnCompleteInformation");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragdialog_game_information, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        etOpponentName = (EditText)view.findViewById(R.id.etOpponentName);
        rbLocal = (RadioButton)view.findViewById(R.id.rbLocal);
        rbVisitor = (RadioButton)view.findViewById(R.id.rbVisitor);
        bOk = (Button)view.findViewById(R.id.bOk);
        bOk.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == bOk){
            if( (!(etOpponentName.getText().toString().equals(""))) && (etOpponentName.getText().length() >= 3) && (etOpponentName.getText().length() <= 10) ){
                boolean isLocal = (rbLocal.isChecked())? true : false;
                dListener.onCompleteInfo(etOpponentName.getText().toString(),isLocal);
                this.dismiss();
            }else
                Toast.makeText(getActivity(),getString(R.string.Name_must_more_or_equal_3_to_10),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        /*if(etOpponentName.getText().length() < 3) {
            Toast.makeText(getActivity(), getString(R.string.CompleteFields), Toast.LENGTH_SHORT).show();
            //show it again
        }*/
    }
}
