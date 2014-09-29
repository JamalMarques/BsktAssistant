package com.skynet.basketassistant.Otros;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.skynet.basketassistant.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jamal on 02/07/14.
 */
public class Manejo_Imagenes {

    public static final String Url = Environment.getExternalStorageDirectory()+"/BasketAssistant/Imagenes/";  //Url donde almaceno imagenes del proyecto
    public static Bitmap ImageNoPlayer;

    public static Bitmap Dame_Bitmap(String nom_img_jugador){
        Bitmap bitmap = BitmapFactory.decodeFile((Url + nom_img_jugador));
        return bitmap;
    }

    public static Bitmap Rotar_Imagen(Bitmap bitmap,int rotacion){
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.postRotate(rotacion);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    public static int Dame_Rotacion(String file) throws IOException {  //Se debe recibir la url completa de la imagen con el nombe del jpg y todo!!

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, bounds);

        ExifInterface exif = new ExifInterface(file);
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotacion = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotacion = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotacion = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotacion = 270;

        return rotacion;
    }

    public static void Grabar_Imagen(Bitmap bitmap,String imagename){  //Lo va a guardar en el directorio de Imagenes!!

        FileOutputStream Foutp = null;

        File dir = new File(Url);
        dir.mkdirs();

        File f = new File(dir,imagename);
        try{
            Foutp = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,Foutp);
            Foutp.flush();
            Foutp.close();
        }catch(Exception e){
            Log.e("PUTA MADRE",e.getMessage()+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            e.printStackTrace();
        }
    }

    public static Bitmap Hacer_cubo_imagen(Bitmap bitmap){
        Bitmap postbitmap;
        if ( bitmap.getWidth() >= bitmap.getHeight() ){
            postbitmap = Bitmap.createBitmap(bitmap,bitmap.getWidth()/2 - bitmap.getHeight()/2,0,bitmap.getHeight(),bitmap.getHeight());
        }
        else
            postbitmap = Bitmap.createBitmap(bitmap,0,bitmap.getHeight()/2 - bitmap.getWidth()/2,bitmap.getWidth(),bitmap.getWidth());

        return postbitmap;
    }

    public static Bitmap Cubo_Rotar_Rotacion(String nom_img_jugador) throws Exception{  //Roto la imagen segun la foto que es y la recorto en cubo al mismo tiempo
        String file = Url+nom_img_jugador;
        Bitmap bitmap = Dame_Bitmap(nom_img_jugador);
        if(bitmap != null) {
            Bitmap bitmap2 = Hacer_cubo_imagen(Rotar_Imagen(bitmap, Dame_Rotacion(file)));
            return bitmap2;
        }else
            return null;
    }

}
