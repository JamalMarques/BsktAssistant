package com.skynet.basketassistant.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.basketassistant.Datos.DBCiudades;
import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBUsuarios;
import com.skynet.basketassistant.Modelo.Ciudad;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.UserContainer;
import com.skynet.basketassistant.Modelo.Usuario;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.SelecTeamAct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USUARIO on 08/05/14.
 */
public class Frag_newteam extends Fragment implements View.OnClickListener{

   // Bundle bun_usr;  //.getString("User") ---> Usuario
    private List<String> lista_ciudades;
    private Button btncrear;
    private ImageButton ibaddciu;
    private TextView tvnom;
    private Spinner spin_ciu;
    private Spinner spin_cat;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.frag_newteam,container,false);

       // bun_usr = getArguments();

        // Toast.makeText(getActivity(), bun_usr.getString("User"), Toast.LENGTH_SHORT).show();  FUNCIONA, MUESTRA EL USUARIO ACTUAL

        tvnom = (TextView)view.findViewById(R.id.etnombre_team);
        spin_ciu = (Spinner)view.findViewById(R.id.spinner_ciu);
        spin_cat = (Spinner)view.findViewById(R.id.spinner_cate);
        btncrear = (Button)view.findViewById(R.id.bcrear);
        ibaddciu = (ImageButton)view.findViewById(R.id.ibaddciu);
        btncrear.setOnClickListener(this);
        ibaddciu.setOnClickListener(this);

        lista_ciudades = new ArrayList<String>();

        DBCiudades dbc = new DBCiudades(getActivity());
        dbc.Modolectura();
        Cursor c = dbc.Cargarcursorciudades();

        if(c.moveToFirst()){                               //HERE IS THE PROBLEM
            int ciu = c.getColumnIndex(dbc.CN_CIUDAD);
            lista_ciudades.add(c.getString(ciu));
            while(c.moveToNext()){
                int ciu2 = c.getColumnIndex(dbc.CN_CIUDAD);
                lista_ciudades.add(c.getString(ciu));
            }
        }
        c.close();

        ArrayAdapter<String> adap_ciu = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lista_ciudades);
        ArrayAdapter<CharSequence> adap_cat = ArrayAdapter.createFromResource(getActivity(),R.array.categorias, R.layout.spinner_item);
        spin_ciu.setAdapter(adap_ciu);
        spin_cat.setAdapter(adap_cat);


        return view;
    }


    @Override
    public void onClick(View view) {

        //si se apreta el boton CREAR
        if(view.getId() == btncrear.getId()){
            if(tvnom.getText().toString().matches("") == false && spin_ciu.getSelectedItem().toString().matches("") == false && spin_cat.getSelectedItem().toString().matches("") == false ){
                DBEquipos dbe = new DBEquipos(getActivity());
                DBCiudades dbc = new DBCiudades(getActivity());
                DBUsuarios dbu = new DBUsuarios(getActivity());
                dbe.Modolectura();
                dbc.Modolectura();
                dbu.Modolectura();
                if(dbe.Existe(tvnom.getText().toString()) == false){

                    dbe.Modoescritura();
                    Ciudad ciu = dbc.Dameciudad(spin_ciu.getSelectedItem().toString());
                    Usuario us = UserContainer.DameUser();                //dbu.BuscarUsuario(bun_usr.getString("User"));

                    dbe.insertar(tvnom.getText().toString(),ciu.getId(),us.getId(),spin_cat.getSelectedItem().toString());
                    dbe.Modolectura();
                    Equipo newequip = dbe.DameEquipo(tvnom.getText().toString());

                    //antes de llamarlo tengo q llamar a un metodo de SelecTeamAct para que refresque la lista antes.
                    ((SelecTeamAct)getActivity()).RefrescarListaFrag1();
                    ((SelecTeamAct)getActivity()).onSelecciondeItemEquipo(newequip);

                }else
                    Toast.makeText(getActivity(),"Ese equipo ya existe", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getActivity(),"Complete ambos campos!", Toast.LENGTH_SHORT).show();
        }
        else
            if(view.getId() == ibaddciu.getId()){ //Agregar nueva ciudad!

                //llamo al metodo de "selectTeamact" para que cambie el fragment por el de agregar ciudad nueva.
                Frag_newciu frag = new Frag_newciu();
                ((SelecTeamAct)getActivity()).CambiarFrameLayout(frag);
            }
    }
}
