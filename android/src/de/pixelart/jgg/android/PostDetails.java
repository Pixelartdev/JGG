package de.pixelart.jgg.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import de.pixelart.jgg.android.adapter.PostPagerAdapter;
import de.pixelart.jgg.android.utils.PagerSlidingTabStrip;

/**
 * Zeigt einen vorher ausgewählten Beitrag.
 * @author Deniz
 * @since 1.0.0
 */
public class PostDetails extends ActionBarActivity {

	ViewPager viewPager;
	String url, title;
	EasyTracker easyTracker = null;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		setActionBar();
		easyTracker = EasyTracker.getInstance(PostDetails.this);
		
		url = getIntent().getStringExtra("news_url");
		title = getIntent().getStringExtra("news_title");
	    
	    viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(new PostPagerAdapter(getSupportFragmentManager(), PostDetails.this));
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
	    tabs.setViewPager(viewPager);
        
	    final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
	            .getDisplayMetrics());
	    viewPager.setPageMargin(pageMargin);
	    
	    // ADS
		AdView adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(getString(R.string.ad_unit_1));
		LinearLayout layout = (LinearLayout) findViewById(R.id.adLayout);
		layout.setVisibility(View.VISIBLE);
		layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        
	}
	
	private void setActionBar() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int back = prefs.getInt("details_back", 0xFF34495E);
		int text = prefs.getInt("details_text", 0xFFFFFFFF);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitleTextColor(text);
		setSupportActionBar(toolbar);
		
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(back));
		getSupportActionBar().setDisplayShowTitleEnabled(false);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.details, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
	        case R.id.menu_share:
	        	easyTracker.send(MapBuilder.createEvent("Share","Action Button", title, null).build());
	        	shareContent();
	            return true;
	        case R.id.menu_web:
	        	easyTracker.send(MapBuilder.createEvent("View Browser","Action Button", title, null).build());
	        	Intent i = new Intent(Intent.ACTION_VIEW);
	        	i.setData(Uri.parse(url));
	        	startActivity(i);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}	

	private void shareContent() {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(title) + "\n" + url);
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, Html.fromHtml("Teilen &uuml;ber...")));

	}
}