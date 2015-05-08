package de.pixelart.jgg.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import de.pixelart.jgg.android.settings.BonusFragment;
import de.pixelart.jgg.android.settings.GeneralFragment;
import de.pixelart.jgg.android.settings.HelpFragment;
import de.pixelart.jgg.android.settings.InfoFragment;
import de.pixelart.jgg.android.settings.LinksFragment;
import de.pixelart.jgg.android.settings.NotificationFragment;
import de.pixelart.jgg.android.settings.StartFragment;
import de.pixelart.jgg.android.settings.VpFragment;

/**
 * The Host Activity für dei Settings
 * @author Deniz
 * @since 1.5
 * @updated 1.5.9
 */
public class SettingsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		getSupportActionBar().setTitle(getString(R.string.title_activity_settings));
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		String action = getIntent().getAction();
		
        if(action != null && action.equals("pref_jgg_general")) {
        	getFragmentManager()
        	.beginTransaction()
        	.replace(R.id.content_frame, new GeneralFragment())
        	.commit();
        }else if(action != null && action.equals("pref_jgg_notification")) {
        	getFragmentManager()
        	.beginTransaction()
        	.replace(R.id.content_frame, new NotificationFragment())
        	.commit();
        }else if(action != null && action.equals("pref_jgg_vp")) {
        	getFragmentManager()
        	.beginTransaction()
        	.replace(R.id.content_frame, new VpFragment())
        	.commit();
        }else if(action != null && action.equals("pref_jgg_info")) {
        	getFragmentManager()
        	.beginTransaction()
        	.replace(R.id.content_frame, new InfoFragment())
        	.commit();
        }else if(action != null && action.equals("pref_jgg_help")) {
        	getFragmentManager()
        	.beginTransaction()
        	.replace(R.id.content_frame, new HelpFragment())
        	.commit();
        }else if(action != null && action.equals("pref_jgg_links")) {
        	getFragmentManager()
        	.beginTransaction()
        	.replace(R.id.content_frame, new LinksFragment())
        	.commit();
        }else if(action != null && action.equals("pref_jgg_bonus")) {
        	getFragmentManager()
        	.beginTransaction()
        	.replace(R.id.content_frame, new BonusFragment())
        	.commit();
        }else {
        	getFragmentManager()
        	.beginTransaction()
        	.replace(R.id.content_frame, new StartFragment())
        	.commit();
        }
	}
}