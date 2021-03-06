package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.basketassistant.Datos.DBJugadores;
import com.skynet.basketassistant.Activities.JugadoresAct;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

/**
 * Created by Jamal on 05/07/14.
 */
public class Frag_new_player extends Fragment implements View.OnClickListener{

    private int equipo_id;
    private Button b_add;
    private EditText et_apellido,et_nombre,et_altura,et_peso,et_numero;
    private TextView tv_hidden_url;
    private Spinner spinner_roles;
    //private ImageView iv_photoplayer;
    private onAddPlayerListener addListener;

    public static onAddPlayerListener emptyAddListener = new onAddPlayerListener() {
        @Override
        public void onAddPlayer() {
            //empty
        }
    };

    /*private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;
    private Intent intent;
    private int codigo;*/

    public static interface onAddPlayerListener{
        public void onAddPlayer();
    }


    public Frag_new_player(){  /*Empty constructor*/ }

    public static Frag_new_player getInstance(int team_id){
        Frag_new_player fnp = new Frag_new_player();
        Bundle bun = new Bundle();
        bun.putInt(Constants.TEAM_ID,team_id);
        fnp.setArguments(bun);
        return fnp;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof onAddPlayerListener)){
            throw new IllegalStateException("Error: La actividad debe implementar el callback del fragmento!!");
        }

        addListener = (onAddPlayerListener)activity;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        addListener = emptyAddListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.frag_new_player,container,false);

        equipo_id = getArguments().getInt(Constants.TEAM_ID);

        b_add = (Button)view.findViewById(R.id.b_add);
        b_add.setOnClickListener(this);

        et_apellido = (EditText)view.findViewById(R.id.et_apellido);
        et_nombre = (EditText)view.findViewById(R.id.et_nombre);
        et_altura = (EditText)view.findViewById(R.id.et_altura);
        et_peso = (EditText)view.findViewById(R.id.et_peso);
        et_numero = (EditText)view.findViewById(R.id.et_numero);
        spinner_roles = (Spinner)view.findViewById(R.id.spinner_roles);
        tv_hidden_url = (TextView)view.findViewById(R.id.tv_hidden_url);

        ArrayAdapter<CharSequence> adap_cat = ArrayAdapter.createFromResource(getActivity(),R.array.roles, R.layout.spinner_item);
        spinner_roles.setAdapter(adap_cat);

        return view;
    }

    @Override
    public void onClick(View view) {
            if( view.getId() == b_add.getId() ){
                if( et_apellido.getText().toString().equals("") || et_numero.getText().toString().equals("") || et_nombre.getText().toString().equals("") || et_altura.getText().toString().equals("") || et_peso.getText().toString().equals("") || et_numero.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Complete todos los campos",Toast.LENGTH_SHORT).show();
                }else
                    if ( et_apellido.getText().toString().length() < 4 || et_nombre.getText().toString().length() < 4){
                        Toast.makeText(getActivity(),"El apellido y el nombre deben tener minimo 4 caracteres",Toast.LENGTH_SHORT).show();
                    }else{
                        //Create the player
                        DBJugadores dbj = new DBJugadores(getActivity());
                        dbj.Modoescritura();
                        dbj.insertar(et_apellido.getText().toString(),et_nombre.getText().toString(),Integer.valueOf(et_altura.getText().toString()),Integer.valueOf(et_peso.getText().toString()),Integer.valueOf(et_numero.getText().toString()),
                                        spinner_roles.getSelectedItem().toString(),equipo_id,tv_hidden_url.getText().toString());
                        dbj.Cerrar();

                        addListener.onAddPlayer(); //calling activity's method
                    }
            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
