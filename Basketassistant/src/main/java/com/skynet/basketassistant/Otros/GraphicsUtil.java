package com.skynet.basketassistant.Otros;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

import java.util.logging.Logger;


public class GraphicsUtil
{

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

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(scaleBitmapImage, new Rect(0, 0, scaleBitmapImage.getWidth(), scaleBitmapImage.getHeight()), new Rect(0, 0, targetSide, targetSide), paint);

        } catch (Exception e)
        {
            Log.e("GraphicsUtil - Method rounded bitmap", e.getMessage());
        }
        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap scaleBitmapImage, float angle)
    {
        Bitmap bitmap = null;
        try
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            bitmap = Bitmap.createBitmap(scaleBitmapImage, 0, 0, scaleBitmapImage.getWidth(), scaleBitmapImage.getHeight(), matrix, true);
        } catch (Exception e)
        {
            Log.e("GraphicsUtil - Method rotate bitmap", e.getMessage());
        }
        return bitmap;
    }

    /*public static Bitmap getUriFromIntent(Uri selectedImageUri)
    {
        Bitmap bitMap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        try
        {
            String[] filePathColumn = { MediaColumns.DATA };
            Cursor cursor = MfsApp.getInstance().getApplicationContext().getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int column_index = cursor.getColumnIndexOrThrow(filePathColumn[0]);
            String tempPath = cursor.getString(column_index);
            cursor.close();

            opts.inDither = false;
            opts.inPurgeable = true;
            opts.inInputShareable = true;
            opts.inJustDecodeBounds = true;
            // opts.inTempStorage = new byte[32 * 1024];
            bitMap = BitmapFactory.decodeFile(tempPath, opts);
            opts.inSampleSize = calculateInSampleSize(opts, MfsApp.getInstance().getScreenSize().x, MfsApp.getInstance().getScreenSize().y);
            opts.inJustDecodeBounds = false;
            bitMap = BitmapFactory.decodeFile(tempPath, opts);
            bitMap = resizeBitmap(bitMap);
        } catch (OutOfMemoryError e)
        {
            // Logger.error("GraphicsUtil - Method get uri OutOfMemoryError: ", e);
            // bitMap = BitmapFactory.decodeResource(MfsApp.getInstance().getApplicationContext().getResources(), R.drawable.silueta, opts);
        } catch (Exception e)
        {
            // Logger.error("GraphicsUtil - Method get uri Exception: ", e);
            // bitMap = BitmapFactory.decodeResource(MfsApp.getInstance().getApplicationContext().getResources(), R.drawable.silueta, opts);
        }
        return bitMap;
    }*/

    public static Bitmap resizeBitmap(Bitmap scaleBitmapImage,int dpX,int dpY)
    {
        final int imageHeight = scaleBitmapImage.getHeight();
        final int imageWidth = scaleBitmapImage.getWidth();
        final int screenHeight = dpY;//MfsApp.getInstance().getScreenSize().y;
        final int screenWidth = dpX;//MfsApp.getInstance().getScreenSize().x;

        if (imageHeight > screenHeight && imageWidth > screenWidth)
        {
            return Bitmap.createScaledBitmap(scaleBitmapImage, screenWidth, screenHeight, true);
        }
        if (imageHeight > screenHeight)
        {
            return Bitmap.createScaledBitmap(scaleBitmapImage, imageWidth, screenHeight, true);
        }
        if (imageWidth > screenWidth)
        {
            return Bitmap.createScaledBitmap(scaleBitmapImage, screenWidth, imageHeight, true);
        }
        return scaleBitmapImage;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth)
            {
                inSampleSize *= 2;
            }
            long totalPixels = width * height / inSampleSize;
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap)
            {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }

        System.out.println("Calculate Sample Size: " + String.valueOf(inSampleSize));
        return inSampleSize;
    }

}
