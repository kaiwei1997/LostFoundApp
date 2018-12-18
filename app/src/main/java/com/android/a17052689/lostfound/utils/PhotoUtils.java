package com.android.a17052689.lostfound.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class PhotoUtils {

    public static byte[] getBytes (Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
    public static Bitmap getImage(byte[] data){

        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
