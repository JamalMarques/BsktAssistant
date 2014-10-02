package com.skynet.basketassistant.Otros;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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

    public static Bitmap Cubo_Rotar_Rotacion2(String nom_img_jugador,int dpW,int dpH) throws Exception{  //Roto la imagen segun la foto que es y la recorto en cubo al mismo tiempo
        String file = Url+nom_img_jugador;
        //Bitmap bitmap = Dame_Bitmap(nom_img_jugador);
        Bitmap bitmap = decodeScaledBitmapFromSdCard(Url + nom_img_jugador,dpW,dpH);
        if(bitmap != null) {
            Bitmap bitmap2 = Hacer_cubo_imagen(Rotar_Imagen(bitmap, Dame_Rotacion(file)));
            return bitmap2;
        }else
            return null;
    }

    //------------------------------  NEW! -------------------------
    public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
                                                      int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage,int targetRes)
    {
        Bitmap bitmap = null;
        try
        {
            //int targetSide = (MfsApp.getInstance().getScreenSize().x * 3) / 4;
            int targetSide = targetRes;

            bitmap = Bitmap.createBitmap(targetSide,targetSide, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);

            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(0xff424242);

            canvas.drawCircle((float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2, (float) bitmap.getWidth() / 2, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(scaleBitmapImage, new Rect(0, 0, scaleBitmapImage.getWidth(), scaleBitmapImage.getHeight()), new Rect(0, 0, targetSide, targetSide), paint);

        } catch (Exception e)
        {
            Log.e("GraphicsUtil - Method rounded bitmap", e.getMessage());
        }
        return bitmap;
    }

}
