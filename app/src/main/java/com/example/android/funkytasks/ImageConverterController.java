package com.example.android.funkytasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.BitmapCompat;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by MonicaB on 2018-03-30.
 */

public class ImageConverterController {

    static final int QUALITY= 50;
    static final int MAX_SIZE = 140;

    /**
     * @param bitmapString String representation of Bitmap
     * @return Image representation of bitmap
     */
    public static Bitmap convertToImage (String bitmapString){
        byte[] decodedString = Base64.decode(bitmapString, Base64.DEFAULT);
        Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return image;
    }

    /**
     * @param image Bitmap representation of picture
     * @return String of Bitmap
     */
    public static String convertToString (Bitmap image) {
        String encodedImage;

        image = getResizedBitmap(image,MAX_SIZE);

        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, QUALITY, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }


    public static boolean checkImages (ArrayList<String> bitmapStrings){
        ArrayList<Bitmap> images = stringToImageList(bitmapStrings);

        for (Bitmap bitmap: images){
            int bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap);
            Log.e("byte size",String.valueOf(bitmapByteCount));
            if (bitmapByteCount >= 65536) { // checking if image is over our wanted size constaint
                return false;
            }
        }

        return true;
    }

    public static ArrayList<Bitmap> stringToImageList(ArrayList<String> strings){
        ArrayList<Bitmap> images = new ArrayList<Bitmap>();
        for (String string: strings){
            images.add(convertToImage(string));
        }
        return images;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
