package de.pixelart.jgg.android.settings;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;

import de.pixelart.jgg.android.R;

public class VpFragment extends PreferenceFragment {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		addPreferencesFromResource(R.xml.pref_vplan);

		bindPreferenceSummaryToValue(findPreference("stufe_list"));
		bindPreferenceSummaryToValue(findPreference("klasse_list"));
	}

	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if(preference instanceof ListPreference) {
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);
				preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

			}else if (preference instanceof RingtonePreference) {
				if (TextUtils.isEmpty(stringValue)) {
					preference.setSummary(R.string.pref_ringtone_silent);

				}else {
					Ringtone ringtone = RingtoneManager.getRingtone(
							preference.getContext(), Uri.parse(stringValue));

					if(ringtone == null) {
						preference.setSummary(null);
					}else {
						String name = ringtone.getTitle(preference.getContext());
						preference.setSummary(name);
					}
				}

			}else {
				preference.setSummary(stringValue);
			}
			return true;
		}
	};
	
	private static void bindPreferenceSummaryToValue(Preference preference) {
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext())
											 .getString(preference.getKey(), ""));
	}
}
