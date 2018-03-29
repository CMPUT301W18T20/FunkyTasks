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
 * Created by MonicaB on 2018-03-29.
 */

public class PicturePagerAdapter extends PagerAdapter{
    Context context;
    ArrayList<Bitmap> images;
    LayoutInflater layoutInflater;


    public PicturePagerAdapter(Context context, ArrayList<Bitmap> images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (images != null){
            return images.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View layout = layoutInflater.inflate(R.layout.imageitem, container, false);


        ImageView imageView = (ImageView) layout.findViewById(R.id.taskpicture);
        imageView.setImageBitmap(images.get(position));

        (container).addView(layout);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return layout;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
