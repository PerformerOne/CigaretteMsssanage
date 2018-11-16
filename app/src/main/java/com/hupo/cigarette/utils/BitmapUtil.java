package com.hupo.cigarette.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapUtil {

    /**
     * res转Bitmap
     *
     * @param context
     * @param res
     * @return
     */
    public static Bitmap resourceToBitmap(Context context, int res) {
        Resources r = context.getResources();
        return BitmapFactory.decodeResource(r, res);
    }

    /**
     * Bitmap转byte
     *
     * @param bm
     * @return
     */
    public byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte转Bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static Drawable byteToDrawable(Context context,byte[] bytes){
        Bitmap b = bytesToBimap(bytes);
        if (b!=null){
            return bitmapToDrawable(context,b);
        }
        return null;
    }

    /**
     * Drawable转化为Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }


    public static Drawable bitmapToDrawable(Context context,Bitmap bm){
        return new BitmapDrawable(context.getResources(), bm);
    }

    public static InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    public static byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[200];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 200)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    public static File inputToFile(InputStream stream, File file){
        OutputStream os ;
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            os = new FileOutputStream(file);
            int bytesRead ;
            byte[] buffer = new byte[8192];
            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.write(buffer);
            os.flush();
            os.close();
            stream.close();
            return file;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return null;
    }
}
