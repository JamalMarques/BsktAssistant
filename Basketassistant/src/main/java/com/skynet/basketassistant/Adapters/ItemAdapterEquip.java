package com.skynet.basketassistant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skynet.basketassistant.Datos.DBCiudades;
import com.skynet.basketassistant.Modelo.Ciudad;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.R;

import java.util.List;

/**
 * Created by jamal on 27/04/14.
 */
public class ItemAdapterEquip extends BaseAdapter {

    private Context context;
    private List<String> equiposlista;

    public ItemAdapterEquip(Context context, List<String> equipos) {
        this.context = context;
        this.equiposlista = equipos;
    }

    @Override
    public int getCount() {
       return this.equiposlista.size(); //cantidad de elementos de la lista
    }

    @Override
    public Object getItem(int posicion) {
        return this.equiposlista.get(posicion); //devuelve elemento Equipo de la posicion
    }

    @Override
    public long getItemId(int posicion) {
        return posicion; // wtf!!?? Devuelve el identificador de fila de una determinada posición de la lista.
    }

    @Override
    public View getView(int posicion, View convertview, ViewGroup parent) {   //viewgroup ---> parent (padre)

        View rowview = convertview;

        if (convertview == null) {
            //crear un nuevo view en la lista
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //inflater_service atributo estatico de clase "Context"
            rowview = inflater.inflate(R.layout.list_item_equip, null);
        }

        //agregar datos dentro del view
        TextView tvnom = (TextView) rowview.findViewById(R.id.tvnom);

        String equip = this.equiposlista.get(posicion);
        tvnom.setText(equip);


        return rowview;
    }

    public void AgregarALista(String nom_equip){
        equiposlista.add(nom_equip);
        notifyDataSetChanged();
    }
}
