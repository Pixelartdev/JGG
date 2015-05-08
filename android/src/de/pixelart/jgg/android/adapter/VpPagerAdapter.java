package de.pixelart.jgg.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import de.pixelart.jgg.android.fragment.HeuteFragment;
import de.pixelart.jgg.android.fragment.MorgenFragment;
 
/**
 * Tabs den Vertretungsplan
 * 
 * @author Deniz Celebi
 * @since 1.5.9
 */
public class VpPagerAdapter extends FragmentPagerAdapter {
	
	String[] tabs = null;
	String[] content;
 
    public VpPagerAdapter(FragmentManager fm, String[] tabs, String[] content) {
        super(fm);
        this.content = content;
        this.tabs = tabs;
    }
    
    public Fragment getItem(int index) {
        switch (index) {
        case 0:
            return new HeuteFragment(content[0]);
        case 1:
            return new MorgenFragment(content[1]);
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