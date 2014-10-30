package com.skynet.basketassistant.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.Otros.GraphicsUtil;
import com.skynet.basketassistant.Otros.Manejo_Imagenes;
import com.skynet.basketassistant.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jamal on 09/06/14.
 */
public class ItemAdapterJugadores extends BaseAdapter {

    private Context context;
    private List<Jugador> lista_jugadores = null;
    private List<Bitmap> lista_bitmap = null;

    public ItemAdapterJugadores(Context cont, List<Jugador> jugadores){
        context = cont;
        lista_jugadores = jugadores;
        lista_bitmap = Generarbitmaps();
    }

    @Override
    public int getCount() {   //aca debe controlar la cantidad de items a drawvear
        return this.lista_jugadores.size();
    }

    @Override
    public Object getItem(int position) {
        return this.lista_jugadores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        View rowview = convertview;

        Holder holder = null;

        if ( rowview == null)
        {
            holder = new Holder();
            LayoutInflater ltInflate = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            rowview = ltInflate.inflate(R.layout.grid_item_jugador,parent,false);

            holder.setnombre((TextView) rowview.findViewById(R.id.tv_apellido));
            holder.setImage((ImageView) rowview.findViewById(R.id.iv_imagenplayer));
            holder.setId_player((TextView) rowview.findViewById(R.id.tv_hidden_idplayer));

            rowview.setTag(holder);  //se ejecutara la primera vez, una vez que se tenga cargado el tag ya va al else de una (para ahorrar memoria)
        }
        else
        {
            holder = (Holder) rowview.getTag();
        }

        Jugador jug = lista_jugadores.get(position);

        holder.getnombre().setText(jug.getApellido());
        holder.getId_player().setText(String.valueOf(jug.getId()));


        if(lista_bitmap.get(position) != null)
            holder.getImage().setImageBitmap(lista_bitmap.get(position));

        return rowview;
    }


    class Holder
    {
        ImageView image;
        TextView nombre;
        TextView id_player;

        public ImageView getImage(){ return image; }

        public void setImage(ImageView image){ this.image = image; }

        public TextView getnombre(){ return nombre; }

        public void setnombre(TextView textView){ this.nombre = textView; }

        public TextView getId_player() {
            return id_player;
        }

        public void setId_player(TextView id_player) {
            this.id_player = id_player;
        }
    }

    public List<Bitmap> Generarbitmaps(){   //lo que pasas es que al enviarle "" con el hidden automaticamente lee la url entera y lo toma como file.exist true y esto genera luego la excepcion
        List<Bitmap> list = new ArrayList<Bitmap>();
        //Bitmap bit = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
        for(int i=0;i<lista_jugadores.size();i++){

            if(lista_jugadores.get(i).getImagen_url().equals("empty")){
                list.add(Manejo_Imagenes.ImageNoPlayer); //inserto la imagen por default
            }else{
                File file = new File(Manejo_Imagenes.Url + lista_jugadores.get(i).getImagen_url());
                if( file.exists()){
                    try {
                        //Bitmap bitmap = Bitmap.createScaledBitmap(Manejo_Imagenes.Cubo_Rotar_Rotacion(lista_jugadores.get(i).getImagen_url()),250,250,true);
                        Bitmap bitmap = Manejo_Imagenes.getRoundedShape((Manejo_Imagenes.Cubo_Rotar_Rotacion2(lista_jugadores.get(i).getImagen_url(),250,250)),250);
                        //list.add(GraphicsUtil.getRoundedShape(bitmap,150));
                        list.add(bitmap);
                    }catch (Exception e){ /*Excepcion no controlada*/ }
                }
                else{
                    list.add(Manejo_Imagenes.ImageNoPlayer); //debo insertar la imagen por default
                }
            }
    }
    return list;
    }

}
