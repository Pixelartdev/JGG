package de.pixelart.jgg.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import de.pixelart.jgg.android.adapter.CatList;
import de.pixelart.jgg.android.model.CatItem;
import de.pixelart.jgg.android.utils.ConnectionDetector;


/**
 * Zeigt eine Liste mit allen Kategorien an
 * @author Deniz
 * @since 1.0.1
 */
public class Cat_List extends ActionBarActivity {
	
	ArrayList<CatItem> catList = null;
	ListView catListView = null;
	SwipeRefreshLayout swipeLayout;
    String url = "http://www.jgg-mannheim.de/jggapi/get_category_index";
    ConnectionDetector cd;
    EasyTracker easyTracker = null;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_cat);
		
		easyTracker = EasyTracker.getInstance(Cat_List.this);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Kategorien");
	
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
		
		catListView= (ListView) findViewById(R.id.cat_list);
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
					swipeLayout.setRefreshing(true);
				    new DownloadFilesTask().execute(url);
				}else {
					cd.makeAlert();
				}
			}
		});
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
		catListView.setVisibility(View.VISIBLE);
		catListView.setAdapter(new CatList(this, catList));
		catListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> a, View v, int position,	long id) {
				Object o = catListView.getItemAtPosition(position);
				CatItem catData = (CatItem) o;
				
				easyTracker.send(MapBuilder.createEvent("Categories","Open Category", catData.getTitle(), null).build());
				
				Intent intent = new Intent(Cat_List.this, Cat.class);
				intent.putExtra("cat", catData);
				startActivity(intent);
			}});
	}

	private class DownloadFilesTask extends AsyncTask<String, Integer, Void> {

		@Override
		protected void onProgressUpdate(Integer... values) {
		}

		@Override
        protected void onPreExecute() {
			swipeLayout.setEnabled(true);
			swipeLayout.setRefreshing(true);
        }

		@Override
		protected void onPostExecute(Void result) {
			if (null != catList) {
				updateList();
				swipeLayout.setRefreshing(false);
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			String url = params[0];
			JSONObject json = getJSONFromUrl(url);
			parseJson(json);
			return null;
		}
	}
	
	public JSONObject getJSONFromUrl(String url) {
		InputStream is = null;
		JSONObject jObj = null;
		String json = null;
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
			json = sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return jObj;
	}

	public void parseJson(JSONObject json) {
	
		try {
			if (json.getString("status").equalsIgnoreCase("ok")) {
				JSONArray posts = json.getJSONArray("categories");

				catList = new ArrayList<CatItem>();

				for (int i = 0; i < posts.length(); i++) {
					JSONObject post = (JSONObject) posts.getJSONObject(i);
					CatItem item = new CatItem();
					item.setTitle(post.getString("title"));
					item.setDesc(post.getString("description"));
					item.setId(post.getString("id"));
					item.setSlug(post.getString("slug"));
					item.setPostCount(post.getString("post_count"));
					
					catList.add(item);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
