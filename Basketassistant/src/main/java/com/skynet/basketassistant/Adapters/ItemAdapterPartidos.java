package com.skynet.basketassistant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skynet.basketassistant.Modelo.Partido;
import com.skynet.basketassistant.R;

import java.util.List;

/**
 * Created by PROGRAMACION on 31/05/14.
 */
public class ItemAdapterPartidos extends BaseAdapter {

    private Context context;
    private List<Partido> partidoslista;

    public ItemAdapterPartidos(Context cont,List<Partido> partidos){
        context = cont;
        partidoslista = partidos;
    }

    @Override
    public int getCount() {
        return this.partidoslista.size();
    }

    @Override
    public Object getItem(int position) {
        return this.partidoslista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View rowview = view;

        if(view == null){
            //crear un nuevo view en la lista
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //inflater_service atributo estatico de clase "Context"
            rowview = inflater.inflate(R.layout.list_item_partido, viewGroup, false);
        }

        //agregar datos dentro del view
        TextView tvnom_rival = (TextView)rowview.findViewById(R.id.tvnom_rival);
        TextView tv_fecha = (TextView)rowview.findViewById(R.id.tv_fecha);
        TextView tv_result = (TextView)rowview.findViewById(R.id.tv_result);
        TextView tv_idpart = (TextView)rowview.findViewById(R.id.tv_idpartido);

        Partido part = this.partidoslista.get(position);

        tvnom_rival.setText(part.getEquipo2_nom().toString());
        tv_fecha.setText(part.getFecha().toString());
        tv_idpart.setText(String.valueOf(part.getId()));

        if( part.GameResult() == Partido.VICTORY ){
            tv_result.setTextColor(context.getResources().getColor(R.color.VerdeClaro_1));
            tv_result.setText(context.getString(R.string.Victory));
        }else{
            if( part.GameResult() == Partido.DEFEAT) {
                tv_result.setTextColor(context.getResources().getColor(R.color.Rojo_1));
                tv_result.setText(context.getString(R.string.Defeat));
            }else{
                tv_result.setTextColor(context.getResources().getColor(R.color.OrangeButton1));
                tv_result.setText(context.getString(R.string.Draw));
            }
        }


        return rowview;
    }
}
