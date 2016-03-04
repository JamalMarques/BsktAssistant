package com.skynet.basketassistant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
public class ItemAdapterEquip extends ArrayAdapter<String>{

    //private List<String> teamList;

    public ItemAdapterEquip(Context context,List<String> teamList) {
        super(context, 0,teamList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        ViewHolder holder = null;

        if( rootView == null ){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            rootView = inflater.inflate(R.layout.list_item_equip,parent,false);

            holder.teamName = (TextView)rootView.findViewById(R.id.tvnom);
            rootView.setTag(holder);
        }else{
            holder = (ViewHolder)rootView.getTag();
        }

        holder.teamName.setText(getItem(position));

        return rootView;
    }

    private class ViewHolder{
        public TextView teamName;
    }

    public void AgregarALista(String nom_equip){
        //equiposlista.add(nom_equip);
        add(nom_equip);
        notifyDataSetChanged();
    }

    public void changeList(List<String> teamsName){
        //equiposlista = teamsName;
        clear();
        addAll(teamsName);
        notifyDataSetChanged();
    }
}

