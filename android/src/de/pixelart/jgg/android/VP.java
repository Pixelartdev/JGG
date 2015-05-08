package de.pixelart.jgg.android;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import de.pixelart.jgg.android.adapter.VpPagerAdapter;
import de.pixelart.jgg.android.utils.PagerSlidingTabStrip;
import de.pixelart.jgg.android.utils.VpDownload;

/**
 * Zeigt Vertretungsplan.
 * @author Deniz
 * @since 1.5
 * @version 1.6.0
 */
public class VP extends ActionBarActivity {
	
	EasyTracker easyTracker = null;
	InterstitialAd interstitial;
	ProgressDialog pd;
	VpDownload vpd;
	FrameLayout contentView;
	String urlH, urlM, nameH, nameM, conH, conM;
	ViewPager viewPager;
	PagerSlidingTabStrip tabs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vp);
		
		easyTracker = EasyTracker.getInstance(VP.this);
		viewPager = (ViewPager) findViewById(R.id.vp_pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.vp_tabs);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		setActionBar();
		vpd = new VpDownload(this);
		vpd.delete(); // immer erst löschen (vorerst)
		new DownloadVPTask().execute();
		
		// ADS
		AdView adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(getString(R.string.ad_unit_1));
		LinearLayout layout = (LinearLayout) findViewById(R.id.adLayout);
		layout.setVisibility(View.VISIBLE);
		layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        
        long current = System.currentTimeMillis();
        long last = prefs.getLong("last_ad", 0);
        boolean show = true;
        if((current - last) >= 10800000) {
        	show = true;
        	SharedPreferences.Editor editor = prefs.edit();
			editor.putLong("last_ad", current);
			editor.commit();
        }else {
        	show = false;
        }
        
        if(prefs.getBoolean("popup_ad", true) && show) {
	        interstitial = new InterstitialAd(VP.this);
			interstitial.setAdUnitId(getString(R.string.ad_unit_2));
	        interstitial.loadAd(adRequest);
	        interstitial.setAdListener(new AdListener() {
				public void onAdLoaded() {
					if (interstitial.isLoaded()) {
						interstitial.show();
					}
				}
			});
        }
        
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.vp, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_refresh:
				vpd.delete();
				new DownloadVPTask().execute();
				return true;
			case android.R.id.home:
				onBackPressed();
			default:
				return true;
	    }
	}
	
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	
	// ActionBar setzen
	private void setActionBar() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int back = prefs.getInt("vp_back", 0xFF2ECC71);
		int text = prefs.getInt("vp_text", 0xFFFFFFFF);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Vertretungsplan");
		toolbar.setTitleTextColor(text);
		setSupportActionBar(toolbar);
		
		tabs.setBackgroundColor(back);
		tabs.setTextColor(text);
		tabs.setIndicatorColor(text);
		
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(back));
		getSupportActionBar().setDisplayShowTitleEnabled(true);
	}
	
	
	// Update vom Inhalt
	private void updateContent() {
		// Heute
		Document h = Jsoup.parse(conH);
		Element elH = h.select("table.mon_list").first();
		Element enH = h.select("div.mon_title").first();
		if(elH != null) {
			conH = elH.toString();
		}else {
			conH = "Fehler - Error: conH couldn't be parsed. I am line 169, VP.java";
		}
		if(enH != null) {
			nameH = enH.text();
		}else {
			nameH = "Fehler - Error: nameH couldn't be parsed. I am line 174, VP.java";
		}
		
		// Morgen
		Document m = Jsoup.parse(conM);
		Element elM = m.select("table.mon_list").first();
		Element enM = m.select("div.mon_title").first();		
		if(elM != null) {
			conM = elM.toString();
		}else {
			conM = "Fehler - Error: conM couldn't be parsed. I am line 184, VP.java";
		}
		if(enM != null) {
			nameM = enM.text();
		}else {
			nameM = "Fehler - Error: nameM couldn't be parsed. I am line 189, VP.java";
		}
		
		// Set the tabs
		String[] tab = {nameH, nameM};
		String[] con = {conH, conM};
		
		viewPager.setAdapter(new VpPagerAdapter(getSupportFragmentManager(),tab, con));
	    tabs.setViewPager(viewPager);        
	    final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
	            .getDisplayMetrics());
	    viewPager.setPageMargin(pageMargin);
	}
	
	/** 
	 * Download vom Vertretungsplan
	 */
	protected class DownloadVPTask extends AsyncTask<String, Integer, Void> {

		@Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(VP.this, "", getString(R.string.dialog_load));
        }

		@Override
		protected void onPostExecute(Void result) {
			if (conH != null && conM != null) {
					updateContent();
			}
			pd.dismiss();
		}

		@Override
		protected Void doInBackground(String... params) {
			vpd.work();
			String[] arr = vpd.getContents();
			nameH = arr[0];
			conH = arr[1];
			nameM = arr[2];
			conM = arr[3];
	        return null;
		}
		
	}
}