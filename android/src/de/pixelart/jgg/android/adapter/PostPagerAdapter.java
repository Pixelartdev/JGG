package de.pixelart.jgg.android.adapter;

import de.pixelart.jgg.android.R;
import de.pixelart.jgg.android.fragment.DetailsFragment;
import de.pixelart.jgg.android.fragment.FotoFragment;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
/**
 * Tabs für die Beiträge
 * 
 * @author Deniz Celebi
 * @since 1.5.9
 */
public class PostPagerAdapter extends FragmentPagerAdapter {
	
	String[] tabs = null;
	Activity activity;
 
    public PostPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.activity=activity;
        tabs = activity.getResources().getStringArray(R.array.tabs);
    }
    
    public Fragment getItem(int index) {
        switch (index) {
        case 0:
            return new DetailsFragment();
        case 1:
            return new FotoFragment();
        }
        
        return null;
    }
 
    @Override
    public int getCount() {
    	return tabs.length;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    } 
}