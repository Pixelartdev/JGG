package de.pixelart.jgg.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import de.pixelart.jgg.android.R;
import de.pixelart.jgg.android.utils.ImageLoader;
 
public class FullScreenImageAdapter extends PagerAdapter {
 
    private Context context;
    private ArrayList<String> urls;
	private LayoutInflater inflater;
	public ImageLoader imageLoader;
 
    // constructor
    public FullScreenImageAdapter(Context context,
            ArrayList<String> urls) {
        this.context = context;
        this.urls = urls;
        
        imageLoader = new ImageLoader(context);
    }
 
    @Override
    public int getCount() {
        return this.urls.size();
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
     
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.row_fullscreen, container, false);
        
    //    ImageView imgDisplay = new ImageView(context);
        ImageView imgDisplay = (ImageView) viewLayout.findViewById(R.id.show_lage);
        imageLoader.DisplayImage(urls.get(position), imgDisplay);
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
    }
     
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}