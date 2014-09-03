package com.skynet.basketassistant.Fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skynet.basketassistant.Adapters.ItemAdapterJugadores;
import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBJugadores;
import com.skynet.basketassistant.Datos.DBPartidos;
import com.skynet.basketassistant.ListView.HorizontalListView;
import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.Modelo.Partido;
import com.skynet.basketassistant.Activities.PartidosAct;
import com.skynet.basketassistant.R;

/**
 * Created by Mdpprogram0 on 03/06/14.
 */
public class Frag_exppart extends Fragment implements AdapterView.OnItemClickListener {


    private Partido partido = null;

    private TextView tv_p_global_e1,tv_p_global_e2,tv_fecha,tv_nom_equip1,tv_nom_equip2,
                        tv_nom_equip1b,tv_nom_equip2b,tv_q1_e1,tv_q1_e2,tv_q2_e1,tv_q2_e2,tv_q3_e1,tv_q3_e2,
                        tv_q4_e1,tv_q4_e2,tv_ext_e1,tv_ext_e2;

    private ImageView iw_result;
    private HorizontalListView h_list;
    private DBJugadores dbj;

    private ProgressBar load_circle;


    /*public Frag_exppart(Partido part){
        partido = part;
    }*/

    public Frag_exppart(){/*Empty constructor*/}

    public static Frag_exppart getInstance(int id_partido){
        Frag_exppart fexp = new Frag_exppart();
        Bundle bun = new Bundle();
        bun.putInt("id_partido",id_partido);
        fexp.setArguments(bun);
        return fexp;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.frag_exppartid,container,false);

        //Bundle
        DBPartidos dbp = new DBPartidos(getActivity());
        dbp.Modolectura();
        partido = dbp.DamePartido(getArguments().getInt("id_partido"));
        dbp.Cerrar();

        if( partido != null ){
            CargaBasica(view);
        }

        dbj = new DBJugadores(getActivity());
        dbj.Modolectura();

        load_circle = (ProgressBar)view.findViewById(R.id.load_circle);
        h_list = (HorizontalListView)view.findViewById(R.id.h_listview);
        h_list.setOnItemClickListener(this);

        //h_list.setAdapter(new ItemAdapterJugadores(getActivity().getApplicationContext(), dbj.DameListaJugadores()));
        new SetearAdaptador().execute();
        //dbj.Cerrar();

        return view;
    }



    public void CargaBasica(View view){
        tv_p_global_e1 = (TextView)view.findViewById(R.id.tv_p_global_e1);
        tv_nom_equip1 = (TextView)view.findViewById(R.id.tv_nom_equip1);
        tv_fecha = (TextView)view.findViewById(R.id.tv_fecha);
        tv_p_global_e2 = (TextView)view.findViewById(R.id.tv_p_global_e2);
        tv_nom_equip2 = (TextView)view.findViewById(R.id.tv_nom_equip2);
        tv_nom_equip1b = (TextView)view.findViewById(R.id.tv_nom_equip1b);
        tv_nom_equip2b = (TextView)view.findViewById(R.id.tv_nom_equip2b);
        tv_q1_e1 = (TextView)view.findViewById(R.id.tv_q1_e1);
        tv_q1_e2 = (TextView)view.findViewById(R.id.tv_q1_e2);
        tv_q2_e1 = (TextView)view.findViewById(R.id.tv_q2_e1);
        tv_q2_e2 = (TextView)view.findViewById(R.id.tv_q2_e2);
        tv_q3_e1 = (TextView)view.findViewById(R.id.tv_q3_e1);
        tv_q3_e2 = (TextView)view.findViewById(R.id.tv_q3_e2);
        tv_q4_e1 = (TextView)view.findViewById(R.id.tv_q4_e1);
        tv_q4_e2 = (TextView)view.findViewById(R.id.tv_q4_e2);
        tv_ext_e1 = (TextView)view.findViewById(R.id.tv_ext_e1);
        tv_ext_e2 = (TextView)view.findViewById(R.id.tv_ext_e2);

        //Adding DATA!
        tv_p_global_e1.setText(String.valueOf(partido.getPuntos_E1()));
        DBEquipos dbe = new DBEquipos(getActivity());
        dbe.Modolectura();
        tv_nom_equip1.setText((dbe.DameEquipo(partido.getEquipo1_id())).getNombre());
        //dbe.Cerrar();

        tv_fecha.setText(partido.getFecha());
        tv_p_global_e2.setText(String.valueOf(partido.getPuntos_E2()));
        tv_nom_equip2.setText(partido.getEquipo2_nom());
        tv_nom_equip1b.setText(tv_nom_equip1.getText().toString());
        tv_nom_equip2b.setText(tv_nom_equip2.getText().toString());
        tv_q1_e1.setText(String.valueOf(partido.getPunt_q1_e1()));
        tv_q1_e2.setText(String.valueOf(partido.getPunt_q1_e2()));
        tv_q2_e1.setText(String.valueOf(partido.getPunt_q2_e1()));
        tv_q2_e2.setText(String.valueOf(partido.getPunt_q2_e2()));
        tv_q3_e1.setText(String.valueOf(partido.getPunt_q3_e1()));
        tv_q3_e2.setText(String.valueOf(partido.getPunt_q3_e2()));
        tv_q4_e1.setText(String.valueOf(partido.getPunt_q4_e1()));
        tv_q4_e2.setText(String.valueOf(partido.getPunt_q4_e2()));
        tv_ext_e1.setText(String.valueOf(partido.getPunt_ext_e1()));
        tv_ext_e2.setText(String.valueOf(partido.getPunt_ext_e2()));


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        int id_jug = Integer.parseInt(((TextView)view.findViewById(R.id.tv_hidden_idplayer)).getText().toString());

        DBJugadores dbj = new DBJugadores(getActivity());
        dbj.Modolectura();
        Jugador jug = dbj.DameJugador(id_jug);
        dbj.Cerrar();

       // Frag_exp_jug_part frag = new Frag_exp_jug_part(jug,partido);
        Frag_exp_jug_part frag = Frag_exp_jug_part.getInstance(id_jug,partido.getId());
        ((PartidosAct)getActivity()).CambiarFragmentLayout2(frag);
    }

    private class SetearAdaptador extends AsyncTask<Void,Void,Void>{

        private ItemAdapterJugadores adapt;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            adapt = new ItemAdapterJugadores(getActivity().getApplicationContext(), dbj.DameListaJugadores());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            h_list.setAdapter(adapt);
            load_circle.setVisibility(View.GONE);
            h_list.setVisibility(View.VISIBLE);
        }
    }
}
