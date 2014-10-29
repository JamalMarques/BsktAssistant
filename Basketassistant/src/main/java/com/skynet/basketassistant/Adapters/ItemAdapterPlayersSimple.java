package com.skynet.basketassistant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.skynet.basketassistant.Modelo.Jugador;

import java.util.List;

/**
 * Created by USUARIO on 28/10/2014.
 */
public class ItemAdapterPlayersSimple extends ArrayAdapter<Jugador> {

    private final int resource = 0;
    private Context context;
    private List<Jugador> playersList;

    public ItemAdapterPlayersSimple(Context context, List<Jugador> playersList) {
        super(context, resource, playersList); //resourse will be hardcoded
        this.resource = ****;
        this.context = context;
        this.playersList = playersList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);


        return rowView;
    }
}
