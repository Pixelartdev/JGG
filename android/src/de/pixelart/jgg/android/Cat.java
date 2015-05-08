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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.gson.Gson;

import de.pixelart.jgg.android.adapter.PostList;
import de.pixelart.jgg.android.model.Author;
import de.pixelart.jgg.android.model.CatItem;
import de.pixelart.jgg.android.model.Posts;
import de.pixelart.jgg.android.model.Recent;
import de.pixelart.jgg.android.utils.ConnectionDetector;

/**
 * Zeigt eine Liste mit Beiträgen einer ausgewählten Kategorie an.
 * @author Deniz
 * @since 1.0.1 
 */
public class Cat extends ActionBarActivity {
	
	ListView catListView = null;
	List<Posts> catList = null;
	List<Posts> finalList = null;
	PostList adapter;
	Recent recent;
	SwipeRefreshLayout swipeLayout;
	CatItem cat;
    String url;
    ConnectionDetector cd;
	Button more;
	View footer;
	int page = 1;
	int total_pages = 1;
	boolean isMore = false;
	EasyTracker easyTracker = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_posts);
		
		easyTracker = EasyTracker.getInstance(Cat.this);
		
		cat = (CatItem) this.getIntent().getSerializableExtra("cat");
		url = "http://www.jgg-mannheim.de/jggapi/get_category_posts/?date_format=d.m.Y%20H:i&include=url,title,date,author,thumbnail&id=" + cat.getId();
        
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Kategorie:  " + cat.getTitle());
		
		catListView = (ListView) findViewById(R.id.news_list);
		footer =  ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_footer, null, false);
		more = (Button) footer.findViewById(R.id.btn_more);
		catListView.addFooterView(footer);
		
		// Swipe refresher settings
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setColorSchemeResources(R.color.flat_blue, R.color.flat_green, R.color.flat_red);
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			public void onRefresh() {
				if(isCon()) {
					new DownloadFilesTask().execute(url);
				}else {
					cd.makeAlert();
				}
			}
		});
		
		catListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
					isMore = false;
					swipeLayout.setRefreshing(true);
					new DownloadFilesTask().execute(url);
				}else {
					cd.makeAlert();
				}
			}
		});
		
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
	
	public void updateList() {
		finalList = catList;
		adapter = new PostList(this, finalList);
		catListView.setVisibility(View.VISIBLE);
		catListView.setAdapter(adapter);
		catListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> a, View v, int position,	long id) {
				Posts posts = (Posts) finalList.get(position);
				Author author = (Author) posts.getAuthor();
				
				easyTracker.send(MapBuilder.createEvent("Category Post","Open Post", posts.getTitle(), null).build());
				
				Intent intent = new Intent(Cat.this, PostDetails.class);
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
	    for(Posts posts : catList){
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
		protected void onPostExecute(Void result) {
			if(page == total_pages){
				more.setClickable(false);
			    more.setText("letzte Seite, das war alles!");
			}
			
			if (catList != null) {			
				if(isMore == true){
					additems();
					isMore = false;
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
	        	 total_pages = recent.getPages();
	        	 catList = new ArrayList<Posts>();
	        	   for (Posts posts : recent.getPosts()) {
	                     catList.add(posts);
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
