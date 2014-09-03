package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.basketassistant.Adapters.ItemAdapterEquip;
import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBUsuarios;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.UserContainer;
import com.skynet.basketassistant.Modelo.Usuario;
import com.skynet.basketassistant.R;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

/**
 * Created by jamal on 22/04/14.
 */
public class Frag_listaequip extends Fragment implements AdapterView.OnItemClickListener{

    private Cursor cursor;
    private DBEquipos dbequip;
    private DBUsuarios dbuser;
    private ListView listview;
    private ItemAdapterEquip adapterlist;

    private Callbacks mcallbacks = callbacksvacios;  //Al inicializarlo no tengo enlace con nadie


    public interface Callbacks{   //Creo la interfaz que me va a servir para enlazarlo cn el metodo "onSelecciondeItemEquipo" de otro objeto
        public void onSelecciondeItemEquipo(Equipo equip);
    }

    public static Callbacks callbacksvacios = new Callbacks() {  //Creo el objeto de tipo "Callbacks" que va a estar vacio, se va a usar para
        @Override                                                // desenlazar del objeto al que estaba enlazado.
        public void onSelecciondeItemEquipo(Equipo equip) {

        }
    };

    public Frag_listaequip(){/*Empty constructor*/}

    public static Frag_listaequip getInstance(){
        Frag_listaequip fle = new Frag_listaequip();
        return fle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.frag_listaequip,container, false);

        listview = (ListView)view.findViewById(R.id.lvequip);
        String nombreuser = UserContainer.DameUser().getNombre(); //getActivity().getIntent().getExtras().getString("User");
        listview.setOnItemClickListener(this);


        dbequip = new DBEquipos(getActivity());
        dbuser = new DBUsuarios(getActivity());

        dbuser.Modolectura();
        Usuario usr = dbuser.BuscarUsuario(nombreuser);
        //dbuser.Cerrar();

        dbequip.Modolectura();
        cursor = dbequip.EquiposdeUsuarioNombres(usr.getId());  //cargo el cursor con los equipos del usuario
        //dbequip.Cerrar();

        List<String> listaequipos = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do {
                listaequipos.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        cursor.close();

        adapterlist = new ItemAdapterEquip(getActivity().getApplicationContext(),listaequipos);
        listview.setAdapter(adapterlist);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {  //Called once the fragment is associated with its activity.
        super.onAttach(activity);               //Ejecuto el metodo original normalmente

        if (!(activity instanceof Callbacks)) {  //Pregunto si esta actividad implementa la interfaz "Callbacks" aqui creada. ("Frag_listaequip.Callbacks")
            throw new IllegalStateException("Error: La actividad debe implementar el callback del fragmento");
        }

        mcallbacks = (Callbacks) activity;  //Realizo el enlace del metodo "onSelecciondeItemEquipo" de "mcallbacks" de esta clase con el de la "Activity" que contiene el fragment.
    }

    @Override
    public void onDetach() {  //Destruyo el enlace para que al ejecutar el metodo de de "mcallbacks" se ejecute el vacio del estatico de esta clase (callbacksvacios).
        super.onDetach();       //called immediately prior to the fragment no longer being associated with its activity.
        mcallbacks = callbacksvacios;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String nomequip = String.valueOf(((TextView)view.findViewById(R.id.tvnom)).getText().toString());  //Tomo el nombre del equipo desde el layout!!

        Equipo equip = dbequip.DameEquipo(nomequip);
        mcallbacks.onSelecciondeItemEquipo(equip);  //ejecuta el codigo del ´SelectTeamAct´
    }

    public void Refrescar(){  //solo queda arreglar esto para que refresque... entra bien al metodo.
        adapterlist.notifyDataSetChanged();
    }

    public void Refrescar(Equipo equip){
        adapterlist.AgregarALista(equip.getNombre());
    }
}
