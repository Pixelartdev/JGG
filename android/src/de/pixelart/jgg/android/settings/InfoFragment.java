package de.pixelart.jgg.android.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import de.pixelart.jgg.android.R;

public class InfoFragment extends PreferenceFragment {
	
	Boolean isBonus = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_info);
	
		SharedPreferences prefs = getActivity().getSharedPreferences("PREFERENCE_APP", 0);
		isBonus = prefs.getBoolean("BonusAktiv", false);
		
		Preference pref = (Preference) findPreference("pref_bonus");
		PreferenceCategory cat = (PreferenceCategory) findPreference("infoCat");
		
		if(isBonus != true || isBonus == false) {
	    	try{
	    		cat.removePreference(pref);
	    	}catch (NullPointerException ex){}
		}		
	}
}
