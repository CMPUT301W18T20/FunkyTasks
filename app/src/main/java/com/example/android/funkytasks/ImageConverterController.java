/**
 * ImageConverterController
 *
 * Version 1.0.0
 *
 * Created by FunkyTasks on 2018-03-29.
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */
package com.example.android.funkytasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.BitmapCompat;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Image controller allows system to convert a bitmap image to a string and vice versa.
 * Gives flexibility for image storage and its memory when we compress it.
 */

public class ImageConverterController {

    private static final int QUALITY= 100;
    private static final int MAX_SIZE = 100;

    /**
     * Converts a string to a bitmap
     * @param bitmapString String representation of Bitmap
     * @return Image representation of bitmap
     */
    public static Bitmap convertToImage (String bitmapString){
        byte[] decodedString = Base64.decode(bitmapString, Base64.DEFAULT);
        Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return image;
    }

    /**
     * Converts a bitmap to a string
     * @param image Bitmap representation of picture
     * @return String of Compressed bitmap
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

    /**
     * Validates the size of the images
     * @param bitmapStrings a list of strings represented as images
     * @return a validated list of bitmaps
     */

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

    /**
     * Converts a list of bitmap strings to a list of bitmaps
     * @param strings a list of string containing their bitmap values
     * @return a converted array list of bitmaps
     */

    public static ArrayList<Bitmap> stringToImageList(ArrayList<String> strings){
        ArrayList<Bitmap> images = new ArrayList<Bitmap>();
        for (String string: strings){
            images.add(convertToImage(string));
        }
        return images;
    }

    /**
     * Scales down the size of an image
     * @param image a bitmap for the image we want to scale
     * @param maxSize a integer representing how large we want the image to be
     * @return
     */
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
