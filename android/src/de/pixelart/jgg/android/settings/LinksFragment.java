package de.pixelart.jgg.android.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import de.pixelart.jgg.android.R;

public class LinksFragment extends PreferenceFragment {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_links);
    }

}
