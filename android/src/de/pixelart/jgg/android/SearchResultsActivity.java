package de.pixelart.jgg.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.gson.Gson;

import de.pixelart.jgg.android.adapter.PostAdapter;
import de.pixelart.jgg.android.model.Author;
import de.pixelart.jgg.android.model.Posts;
import de.pixelart.jgg.android.model.Recent;
import de.pixelart.jgg.android.utils.ConnectionDetector;

public class SearchResultsActivity extends ActionBarActivity {

	TextView tv, num;
	String query, title, url;
	List<Posts> searchList = null;
	List<Posts> finalList = null;
	Recent recent;
	ListView searchListView = null;
	PostAdapter adapter;
	SwipeRefreshLayout swipeLayout;
    String burl = "http://www.jgg-mannheim.de/jggapi/get_search_results/?date_format=d.m.Y%20H:i&post_type=post&include=url,title,date,author,thumbnail&search=";
	ConnectionDetector cd;
	Button more;
	View footer;
	int page = 0;
	int total_pages = 0;
	boolean isMore = false;
	EasyTracker easyTracker = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.list_posts);
		
		easyTracker = EasyTracker.getInstance(SearchResultsActivity.this);
	
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
        
        searchListView = (ListView) findViewById(R.id.news_list);
		footer =  ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_footer, null, false);
		more = (Button) footer.findViewById(R.id.btn_more);
		searchListView.addFooterView(footer);
		
		// Swipe refresher settings
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setColorSchemeResources(R.color.flat_blue, R.color.flat_green, R.color.flat_red);
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			public void onRefresh() {
				if(isCon()) {
					swipeLayout.setRefreshing(true);
					new DownloadFilesTask().execute(url);
				}else {
					cd.makeAlert();
				}
			}
		});
		
		searchListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem == 0) {
					swipeLayout.setEnabled(true);
				}else {
					swipeLayout.setEnabled(false);
				}
			}
		});
		
		swipeLayout.post(new Runnable() {
			public void run() {
				if(isCon()) {
					swipeLayout.setRefreshing(true);
					new DownloadFilesTask().execute(url);
				}else {
					cd.makeAlert();
				}
			}
		});
		
		
		// set header
		LinearLayout header = new LinearLayout(this);
		num = new TextView(this);
		num.setId(2);
		Typeface tf = Typeface.createFromAsset(getAssets(),"italic.ttf");
		num.setTypeface(tf,Typeface.NORMAL);
		num.setTextSize(20);
		header.addView(num);
		searchListView.addHeaderView(header);
        
		handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private Boolean isCon() {
		cd = new ConnectionDetector(this);
		return cd.isConnectingToInternet();
	}

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	
            query = intent.getStringExtra(SearchManager.QUERY);
            
            easyTracker.send(MapBuilder.createEvent("Search","Search", query, null).build());
            
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,																  
			SearchProvider.AUTHORITY, SearchProvider.MODE);
			suggestions.saveRecentQuery(query, null);
            
            if(query.equals("do the harlem shake")) {
            	Intent shake = new Intent(SearchResultsActivity.this, Launcher.class);
            	shake.putExtra("shake", true);
            	startActivity(shake);
            	finish();
            }else {
				getSupportActionBar().setTitle("Suche:  " + query);
	            url = burl + query.replaceAll(" ", "%20");
				
				if(isCon()){
					isMore = false;
				    new DownloadFilesTask().execute(url);
				}else{
					cd.makeAlert();
				}
				
            }
		}
    }
	
	public void updateList() {
		
		if(title.equals("0")) {
			num.setText("");
			num.setText("Die Suche hat kein Ergebnis");
		}else {
			num.setText("");
			num.setText("Die Suche hat " + title + " Ergebnisse");
		}
		
		finalList = searchList;
		adapter = new PostAdapter(this, finalList);
		searchListView.setVisibility(View.VISIBLE);
		searchListView.setAdapter(adapter);
		tv = (TextView) findViewById(R.id.search_result);
		searchListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> a, View v, int position,	long id) {
					Posts posts = (Posts) searchListView.getItemAtPosition(position);
					Author author = (Author) posts.getAuthor();

					easyTracker.send(MapBuilder.createEvent("Search Result Post","Open Post", posts.getTitle(), null).build());
					
					Intent intent = new Intent(SearchResultsActivity.this, PostDetails.class);
					intent.putExtra("news_url", posts.getUrl());
					intent.putExtra("news_title", posts.getTitle());
					intent.putExtra("news_date", posts.getDate());
					intent.putExtra("news_author", author.getName());
					startActivity(intent);
				}
			});
		
		more.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				page++;
				isMore = true;
				new DownloadFilesTask().execute(url+"&page="+page);
			}
		});
	}
	
	private void additems() {
	    for(Posts posts : searchList){
				adapter.add(posts);
		}
	}
	
	protected class DownloadFilesTask extends AsyncTask<String, Integer, Void> {

		@Override
        protected void onPreExecute() {
			swipeLayout.setEnabled(true);
			swipeLayout.setRefreshing(true);
        }

		@Override
		protected void onPostExecute(Void unused) {
			if(page == total_pages){
				more.setClickable(false);
			    more.setText("letzte Seite, das war alles!");
			}
			
			if (searchList != null) {			
				if(isMore == true){
					additems();
				}else{
					updateList();
				}
			}
			swipeLayout.setRefreshing(false);
		}

		@Override
		protected Void doInBackground(String... params) {
			String url = params[0];
			try {
			     InputStream source = retrieveStream(url);
	             Gson gson = new Gson();        
	             Reader reader = new InputStreamReader(source);  
	        	 recent = gson.fromJson(reader, Recent.class);
	        	 title = String.valueOf(recent.getCount_total());
	        	 total_pages = recent.getPages();
	        	 searchList = new ArrayList<Posts>();
	        	   for (Posts posts : recent.getPosts()) {
	                     searchList.add(posts);
	               }
                 reader.close();
             } catch (Exception e) {
            	 Log.e("JGG Parser", e.toString());
             }
	        
	        return null;
	    }
	    
		private InputStream retrieveStream(String url) {
			DefaultHttpClient client = new DefaultHttpClient(); 
			HttpGet getRequest = new HttpGet(url);
			try {
				HttpResponse getResponse = client.execute(getRequest);
				final int statusCode = getResponse.getStatusLine().getStatusCode();
				if(statusCode != HttpStatus.SC_OK) { 
					Log.w("JGG", "Error " + statusCode + " for URL " + url); 
					return null;
				}
				HttpEntity getResponseEntity = getResponse.getEntity();
				return getResponseEntity.getContent();
			} 
			catch (IOException e) {
				getRequest.abort();
				Log.w("JGG", "Error for URL " + url, e);
			}
		return null;
	    }
		}
	}