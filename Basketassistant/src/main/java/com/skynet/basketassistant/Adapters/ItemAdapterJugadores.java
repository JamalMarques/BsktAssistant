package com.skynet.basketassistant.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jamal on 09/06/14.
 */
public class ItemAdapterJugadores extends BaseAdapter {

    private Context context;
    private List<Jugador> lista_jugadores = null;
    //private List<Bitmap> lista_bitmap = null;
    private boolean isOldVersion;  //Exist 2 xml to inflate (new version and old version)

    public ItemAdapterJugadores(Context cont, List<Jugador> jugadores,boolean isOldVersion){
        context = cont;
        lista_jugadores = jugadores;
        //lista_bitmap = Generarbitmaps();
        this.isOldVersion = isOldVersion;
    }

    public void setList(List<Jugador> playersList){
        lista_jugadores = playersList;
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
            if(isOldVersion)
                rowview = ltInflate.inflate(R.layout.grid_item_jugador,parent,false);
            else
                rowview = ltInflate.inflate(R.layout.grid_item_jugador_2,parent,false);

            holder.nombre = (TextView) rowview.findViewById(R.id.tv_apellido);
            holder.image = (ImageView) rowview.findViewById(R.id.iv_imagenplayer);
            holder.id_player = (TextView) rowview.findViewById(R.id.tv_hidden_idplayer);

            rowview.setTag(holder);
        }
        else
        {
            holder = (Holder) rowview.getTag();
        }

        Jugador jug = lista_jugadores.get(position);

        holder.nombre.setText(jug.getApellido());
        holder.id_player.setText(String.valueOf(jug.getId()));

        String url = "file://"+Manejo_Imagenes.Url+((Jugador)getItem(position)).getImagen_url();
        Picasso.with(context).load(url)
                .error(R.drawable.no_player_image)
                .transform(new CircleTransformPicasso())
                .fit()
                .centerCrop()
                .into(holder.image);


        /*if(lista_bitmap.get(position) != null)
            holder.getImage().setImageBitmap(lista_bitmap.get(position));*/

        return rowview;
    }


    class Holder
    {
        ImageView image;
        TextView nombre;
        TextView id_player;
    }

    public List<Bitmap> Generarbitmaps(){   //lo que pasas es que al enviarle "" con el hidden automaticamente lee la url entera y lo toma como file.exist true y esto genera luego la excepcion
        List<Bitmap> list = new ArrayList<Bitmap>();
        //Bitmap bit = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
        for(int i=0;i<lista_jugadores.size();i++){

            if(lista_jugadores.get(i).getImagen_url().equals("empty")){
                list.add(null/*Manejo_Imagenes.ImageNoPlayer*/); //inserto la imagen por default
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
                    list.add(null/*Manejo_Imagenes.ImageNoPlayer*/); //debo insertar la imagen por default
                }
            }
    }
    return list;
    }


    public class CircleTransformPicasso implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            //250

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

}
