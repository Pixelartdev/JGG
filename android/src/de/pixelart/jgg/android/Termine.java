package de.pixelart.jgg.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import de.pixelart.jgg.android.utils.ConnectionDetector;

/**
 * Zeigt aktuelle Termine an.
 * @author Deniz
 * @since 1.0.0
 */
public class Termine extends ActionBarActivity {

	String url = "http://www.jgg-mannheim.de/termine/";
	WebView content;
	String newContent;
	ProgressDialog pd;
	ConnectionDetector cd;
	EasyTracker easyTracker = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_termin);
		
		setActionBar();
		easyTracker = EasyTracker.getInstance(Termine.this);
		
		if(isCon()) {
		  new DownloadTermineTask().execute(url);
		}else {
			cd.makeAlert();
		}
		
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
	
	private void setActionBar() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int back = prefs.getInt("termin_back", 0xFF3498DB);
		int text = prefs.getInt("termin_text", 0xFFFFFFFF);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Termine");
		toolbar.setTitleTextColor(text);
		setSupportActionBar(toolbar);
		
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(back));
		getSupportActionBar().setDisplayShowTitleEnabled(true);
	}
	
	private Boolean isCon() {
		cd = new ConnectionDetector(this);
		return cd.isConnectingToInternet();
	}

	private class DownloadTermineTask extends AsyncTask<String, Integer, Void> {

		@Override
		protected void onProgressUpdate(Integer... values) {
		}

		@Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(Termine.this, "", "Bitte warten...");
        }

		@Override
		protected void onPostExecute(Void result) {
			updateContent();
			pd.dismiss();
		}

		@Override
		protected Void doInBackground(String... params) {
			getContent(url);
			return null;
		}
	}


	public void getContent(String url) {
		InputStream is = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
														   is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			
			newContent = sb.toString();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateContent() {
		Document doc = Jsoup.parse(newContent);
		doc.getElementsByClass("aec-credit").remove();
		Elements info = doc.select("aside#text-3");
		newContent = info.toString();
		// Styles
		newContent = newContent + "<link rel=\"stylesheet\" id=\"jq_ui_css-css\" href=\"http://www.jgg-mannheim.de/wp-content/plugins/ajax-event-calendar/css/jquery-ui-1.8.16.custom.css\" type=\"text/css\" media=\"all\">";
		newContent = newContent + "<link rel=\"stylesheet\" id=\"custom-css\" href=\"http://www.jgg-mannheim.de/wp-content/plugins/ajax-event-calendar/css/custom.css\" type=\"text/css\" media=\"all\">";
		// Javascripts
		newContent = newContent + "<script type=\"text/javascript\" src=\"http://www.jgg-mannheim.de/wp-includes/js/jquery/jquery.js\"></script>";
		newContent = newContent + "<script type=\"text/javascript\" src=\"http://www.jgg-mannheim.de/wp-content/plugins/ajax-event-calendar/js/jquery.init_show_calendar.js\"></script>";	
		content = (WebView) findViewById(R.id.terminView);
		content.loadDataWithBaseURL("jgg://jgg.app", newContent, "text/html", "UTF-8", null);
	}
}
