package com.skynet.basketassistant.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.basketassistant.Datos.DBCiudades;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.Activities.SelecTeamAct;

/**
 * Created by Jamal on 22/05/14.
 */
public class Frag_newciu extends Fragment implements View.OnClickListener{

    private TextView nomciu,nomprov;
    private Button btncrear,btncancelar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.frag_newciu,container,false);

        nomciu = (TextView)view.findViewById(R.id.etnomciu);
        nomprov = (TextView)view.findViewById(R.id.etnomprov);
        btncrear = (Button)view.findViewById(R.id.bagregar);
        btncancelar = (Button)view.findViewById(R.id.bcancelar);

        btncrear.setOnClickListener(this);
        btncancelar.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == btncrear.getId()){ //se realiza la insercion de la nueva ciudad
            if(!nomciu.getText().equals("") && !nomprov.getText().equals("")) {
                DBCiudades dbc = new DBCiudades(getActivity());
                dbc.Modolectura();
                if (dbc.Existe(nomciu.getText().toString()) == false) {

                    dbc.Modoescritura();
                    dbc.insertar(nomciu.getText().toString(), nomprov.getText().toString());

                    Toast.makeText(getActivity(), "Ciudad agregada correctamente", Toast.LENGTH_SHORT).show();
                    GoNew_team();
                } else
                    Toast.makeText(getActivity(), "Esa ciudad ya existe", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(getActivity(),getString(R.string.CompleteFields),Toast.LENGTH_SHORT).show();
        }
        else
            if(view.getId() == btncancelar.getId()){
                GoNew_team();
            }

    }

    public void GoNew_team(){ //llamo al metodo "Cambiarfragment" de SelectTeamAct enviandole un nuevo fragment "Frag_expequip"
        Frag_newteam frag = new Frag_newteam();
        ((SelecTeamAct)getActivity()).CambiarFrameLayout(frag);
    }

}
