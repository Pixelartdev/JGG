package de.pixelart.jgg.android.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.gson.Gson;

import de.pixelart.jgg.android.R;
import de.pixelart.jgg.android.model.Post;
import de.pixelart.jgg.android.model.SinglePost;
import de.pixelart.jgg.android.utils.ConnectionDetector;

/**
 * Zeigt den Nachrichten Inhalt eines Beitrages.
 * @author Deniz
 * @since 1.0.2
 */
public class DetailsFragment extends Fragment {
	
	View v;
	WebView content;
    ProgressDialog pd;
	ConnectionDetector cd;
	String Eurl = "?json=1&include=content,date&date_format=d.m.Y%20H:i";
	String url;
	String newContent;
	String title;
	String date;
	String author;
	TextView info;
	TextView head;
	SinglePost sp;
	Post p;
	EasyTracker easyTracker = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		v = inflater.inflate(R.layout.fragment_detail, container, false);
		
		easyTracker = EasyTracker.getInstance(getActivity());
		cd = new ConnectionDetector(getActivity());
		
		easyTracker.send(MapBuilder.createEvent("Open Fragment","Fragment", "Details", null).build());
		
		url = getActivity().getIntent().getStringExtra("news_url");
		url = url+ Eurl;
		date = getActivity().getIntent().getStringExtra("news_date");
		title = getActivity().getIntent().getStringExtra("news_title");
		author = getActivity().getIntent().getStringExtra("news_author");
		 
		if(isCon() == true){
		 new DownloadFilesTask().execute();
		}else{
			cd.makeAlert();
		}
        return v;
    }
	
	private Boolean isCon() {
		cd = new ConnectionDetector(getActivity());
		return cd.isConnectingToInternet();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(getActivity()).activityStart(getActivity());
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(getActivity()).activityStop(getActivity());
	}	
	
	private void updateContent() {
		content = (WebView) v.findViewById(R.id.content);
		info = (TextView) v.findViewById(R.id.news_info);
		head = (TextView) v.findViewById(R.id.news_headline);
		
		//img ersetzen
		if(newContent != null){
	    	Document document = Jsoup.parse(newContent);
		    document.select("dl").remove();
		    document.select("br").remove();
		    document.select("img").remove();
	    	newContent = document.toString();
	    	content.loadDataWithBaseURL("jgg://jgg.app", newContent, "text/html", "UTF-8", null);
		}
		
		if(author != null && date != null) {
			info.setText(Html.fromHtml("Von: <b>" + author + "</b> Am <b>" + date +"</b>"));
		}else if(title != null) {
			head.setText(Html.fromHtml(title));
		}
	}
	
	
	protected class DownloadFilesTask extends AsyncTask<String, Integer, Void> {

		@Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(getActivity(), "", "Bitte warten...");
        }

		@Override
		protected void onPostExecute(Void result) {
				updateContent();
				pd.dismiss();
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
			     InputStream source = retrieveStream(url);
	             Gson gson = new Gson();  
	             Reader reader = new InputStreamReader(source);   
	        	 sp  = gson.fromJson(reader, SinglePost.class);
	        	 p = sp.getPost();
	        	 newContent = p.getContent();
	        	 date = p.getDate();
                 reader.close();
             } catch (Exception e) { }
	        
	        return null;
	    }
	    
		private InputStream retrieveStream(String url) {
			DefaultHttpClient client = new DefaultHttpClient(); 
			HttpGet getRequest = new HttpGet(url);
			try {
				HttpResponse getResponse = client.execute(getRequest);
				final int statusCode = getResponse.getStatusLine().getStatusCode();
				if(statusCode != HttpStatus.SC_OK) { 
					Log.w("WPBA", "Error " + statusCode + " for URL " + url); 
					return null;
				}
				HttpEntity getResponseEntity = getResponse.getEntity();
				return getResponseEntity.getContent();
			} 
			catch (IOException e) {
				getRequest.abort();
				Log.w("WPBA", "Error for URL " + url, e);
			}
		return null;
	    }
		}
}