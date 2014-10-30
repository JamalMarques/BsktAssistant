package com.skynet.basketassistant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.R;

import java.util.List;

/**
 * Created by USUARIO on 28/10/2014.
 */
public class ItemAdapterPlayersSimple extends ArrayAdapter<Jugador> {

    private final int resource;
    private Context context;
    private List<Jugador> playersList;

    public ItemAdapterPlayersSimple(Context context, List<Jugador> playersList) {
        super(context, R.layout.list_item_player, playersList); //resourse will be hardcoded
        this.resource = R.layout.list_item_player;
        this.context = context;
        this.playersList = playersList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if(rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(resource, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.playerName = (TextView)rowView.findViewById(R.id.tvNamePlayer);
            holder.playerNumber = (TextView)rowView.findViewById(R.id.tvPlayerNumer);
            rowView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder)rowView.getTag();

        Jugador player = playersList.get(position);
        holder.playerNumber.setText(player.getNumero()+"");
        holder.playerName.setText(player.getApellido()+", "+player.getNombre());

        return rowView;
    }

    static class ViewHolder{
        public TextView playerNumber;
        public TextView playerName;
    }
}
