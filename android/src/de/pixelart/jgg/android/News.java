package de.pixelart.jgg.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
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
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.gson.Gson;

import de.pixelart.jgg.android.adapter.PostList;
import de.pixelart.jgg.android.model.Author;
import de.pixelart.jgg.android.model.Posts;
import de.pixelart.jgg.android.model.Recent;

public class News extends ActionBarActivity {
	
	List<Posts> newsList = null;
	List<Posts> finalList = null;
	Recent recent;
	PostList adapter;
	ListView newsListView = null;
	SwipeRefreshLayout swipeLayout;
	EasyTracker easyTracker = null;
	Button more;
	View footer;
	int page = 1;
	int total_pages = 10;
	boolean isMore = false;
	Context con;
    String url = "http://www.jgg-mannheim.de/jggapi/get_recent_posts/?date_format=d.m.Y%20H:i&include=url,title,date,author,thumbnail&count=";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_posts);
		
		con = this;
		setActionBar();
		easyTracker = EasyTracker.getInstance(News.this);
		
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
				.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		}catch(Exception e) {
			e.printStackTrace();
		} 
		
		newsListView = (ListView) findViewById(R.id.news_list);
		footer =  ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_footer, null, false);
		more = (Button) footer.findViewById(R.id.btn_more);
		newsListView.addFooterView(footer);
		
		isMore = false;
		
		// Swipe refresher settings
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setColorSchemeResources(R.color.flat_blue, R.color.flat_green, R.color.flat_red);
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			public void onRefresh() {
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(News.this);
				new DownloadFilesTask().execute(url+pref.getString("post_count", "10"));
			}
		});
		
		newsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
				swipeLayout.setRefreshing(true);
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(News.this);
				new DownloadFilesTask().execute(url+pref.getString("post_count", "10"));
			}
		});
		
	}
	
	private void setActionBar() {
		SharedPreferences prefs = getSharedPreferences("PREFERENCE_APP", 0);
		int back = prefs.getInt("news_back", 0xFFE74C3C);
		int text = prefs.getInt("news_text", 0xFFFFFFFF);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("News");
		toolbar.setTitleTextColor(text);
		setSupportActionBar(toolbar);
		
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(back));
		getSupportActionBar().setDisplayShowTitleEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.news, menu);
	
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		MenuItem searchItem = menu.findItem(R.id.search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_cat:
				Intent cat = new Intent (News.this, Cat_List.class);
				startActivity(cat);
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
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

	public void updateList() {
		finalList = newsList;
		adapter = new PostList(this, finalList);
		newsListView.setVisibility(View.VISIBLE);
		newsListView.setAdapter(adapter);
		newsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,	long id) {
				Posts posts = (Posts) finalList.get(position);
				Author author = (Author) posts.getAuthor();
				
				easyTracker.send(MapBuilder.createEvent("News Post","Open Post", posts.getTitle(), null).build());
				
				Intent intent = new Intent(News.this, PostDetails.class);
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
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(con);
				isMore = true;
				new DownloadFilesTask().execute(url+pref.getString("post_count", "10")+"&page="+page);
			}
		});
	}
	
	private void additems() {
	    for(Posts posts : newsList) {
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
			
			if (newsList != null) {			
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
			String uri = params[0];
			try {     
			     InputStream source = retrieveStream(uri);
	             Gson gson = new Gson();        
	             Reader reader = new InputStreamReader(source);  
	        	 recent = gson.fromJson(reader, Recent.class);
	        	 total_pages = recent.getPages();
	        	 newsList = new ArrayList<Posts>();
	        	   for (Posts posts : recent.getPosts()) {
	                     newsList.add(posts);
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