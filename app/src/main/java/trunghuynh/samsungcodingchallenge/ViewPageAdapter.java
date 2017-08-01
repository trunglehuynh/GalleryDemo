package trunghuynh.samsungcodingchallenge;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by trunghuynh on 7/31/17.
 */

public class ViewPageAdapter extends PagerAdapter {


    Drawable[] images;
    Context context;
    int layoutID;
    int imageViewID;


    public ViewPageAdapter(Drawable[] images, Context context, int layoutID, int imageViewID) {

        this.images = images;
        this.context = context;
        this.layoutID = layoutID;
        this.imageViewID = imageViewID;

    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater itemInflater =  LayoutInflater.from(context);
        View itemView = itemInflater.inflate( layoutID,container,false);
        ImageView imageView = (ImageView) itemView.findViewById(imageViewID);
        imageView.setImageDrawable(images[position]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
