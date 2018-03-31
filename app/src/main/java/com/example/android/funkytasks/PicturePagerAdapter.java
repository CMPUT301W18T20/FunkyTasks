/**
 * Picture Pager Adapter
 *
 * Version 1.0.0
 *
 * Created on March 8th by Funky Tasks
 *
 * Copyright information: https://github.com/CMPUT301W18T20/FunkyTasks/wiki/Reuse-Statement
 */
package com.example.android.funkytasks;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * An adapter that allows us to display photos, and scroll through photos on the same activity
 */

public class PicturePagerAdapter extends PagerAdapter{
    private Context context;
    private ArrayList<Bitmap> images;
    private LayoutInflater layoutInflater;


    /**
     * Constructer for the PagerAdapter
     * @param context for which activity the adapter will be set in
     * @param images arraylist of bitmaps to display
     */
    public PicturePagerAdapter(Context context, ArrayList<Bitmap> images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Counts the number of images
     * @return integer representing the number of images found in the list
     */
    @Override
    public int getCount() {
        if (images != null){
            return images.size();
        }
        return 0;
    }

    /**
     * Checking if the image found in on the same view
     * @param view where the image is displayed
     * @param object the image itself
     * @return a boolean if the image is from the view
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    /**
     * Displays the image on the view
     * @param container for which U.I element the image will be displayed on
     * @param position for what image to display from the arraylist
     * @return the view containing the image display
     */
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.imageitem, container, false);


        ImageView imageView = (ImageView) itemView.findViewById(R.id.taskpicture);
        Bitmap image = images.get(position);

        imageView.setImageBitmap(image);
        (container).addView(itemView);


        return itemView;
    }

    /**
     * Destroys the item shown on screen
     * @param container for which U.I element the image will be displayed on
     * @param position for what image to display from the arraylist
     * @param object for what view to delete the image from
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
