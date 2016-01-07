package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.skynet.basketassistant.Adapters.ItemAdapterEquip;
import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBUsuarios;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.UserContainer;
import com.skynet.basketassistant.Modelo.Usuario;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamal on 22/04/14.
 */
public class Frag_listaequip extends Fragment implements AdapterView.OnItemClickListener{

    public static Integer idToDelete = null; //Take care with this attribute, always must be cleaned after use it.

    private int userID;
    private DBEquipos dbequip;
    private DBUsuarios dbuser;
    //private ListView listview;
    private SwipeListView listview;
    private ItemAdapterEquip adapterlist;
    private int lastItemSelected = 0;

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

        listview = (SwipeListView)view.findViewById(R.id.lvequip);
        listViewBehaviour(listview);


        dbequip = new DBEquipos(getActivity());
        dbuser = new DBUsuarios(getActivity());

        List<String> listaequipos = generateTeamsName(UserContainer.DameUser().getId());

        adapterlist = new ItemAdapterEquip(getActivity(),listaequipos);
        listview.setAdapter(adapterlist);

        return view;
    }

    private void listViewBehaviour(final SwipeListView listview){
        listview.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
            }

            @Override
            public void onStartClose(int position, boolean right) {
            }

            @Override
            public void onClickFrontView(int position) {
                if(position != lastItemSelected) {
                    String nomequip = adapterlist.getItem(position);//String.valueOf(((TextView)view.findViewById(R.id.tvnom)).getText().toString());  //Tomo el nombre del equipo desde el layout!!
                    Equipo equip = dbequip.DameEquipo(nomequip);
                    lastItemSelected = position;
                    mcallbacks.onSelecciondeItemEquipo(equip);  //ejecuta el codigo del ´SelectTeamAct´
                }
            }

            @Override
            public void onClickBackView(int position) {
                FragDialog_YesNo frag = FragDialog_YesNo.getInstance(getString(R.string.delete_team_message), Constants.YES_NO_DELETE_TEAM);
                DBEquipos dbe = new DBEquipos(getActivity());
                Frag_listaequip.idToDelete = dbe.DameEquipo(adapterlist.getItem(position)).getId();
                frag.show(getActivity().getSupportFragmentManager(),"FragDialog_YesNo");
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {

            }
        });

        //These are the swipe listview settings. you can change these
        //setting as your requrement
        listview.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT); // there are five swiping modes
        listview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); //there are four swipe actions
        //listview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_NONE);
        listview.setOffsetLeft(convertDpToPixel(-60f)); // left side offset
        listview.setOffsetRight(convertDpToPixel(-60f)); // right side offset
        listview.setAnimationTime(80); // animarion time
        listview.setSwipeOpenOnLongPress(false); // enable or disable SwipeOpenOnLongPress
    }

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    private List<String> generateTeamsName(int userID){
        List<Equipo> teams = dbequip.EquiposdeUsuario(userID);  //cargo el cursor con los equipos del usuario
        List<String> listaequipos = new ArrayList<String>();
        for(int i=0; i<teams.size(); i++){
            listaequipos.add(teams.get(i).getNombre());
        }
        return listaequipos;
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
    }

    public void Refrescar(){
        adapterlist.changeList(generateTeamsName(UserContainer.DameUser().getId()));
        adapterlist.notifyDataSetChanged();
        selectTheFirstTeamInList();
    }

    public void Refrescar(Equipo equip){
        adapterlist.AgregarALista(equip.getNombre());
    }

    private void selectTheFirstTeamInList(){
        DBEquipos dbe = new DBEquipos(getActivity());
        dbe.Modolectura();
        List<Equipo> teams = dbe.EquiposdeUsuario(UserContainer.DameUser().getId());
        if(teams.size() >= 1) {
            mcallbacks.onSelecciondeItemEquipo(teams.get(0));  //Select the first of the list
        }
    }



}