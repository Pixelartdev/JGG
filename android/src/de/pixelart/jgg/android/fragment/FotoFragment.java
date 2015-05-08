package de.pixelart.jgg.android.fragment;

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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.gson.Gson;

import de.pixelart.jgg.android.FullscreenImage;
import de.pixelart.jgg.android.R;
import de.pixelart.jgg.android.adapter.FotoAdapter;
import de.pixelart.jgg.android.model.Attachment;
import de.pixelart.jgg.android.model.Post;
import de.pixelart.jgg.android.model.SinglePost;
import de.pixelart.jgg.android.utils.ConnectionDetector;

/**
 * Zeigt die Bilder von einem vorher ausgewähltem Beitrag an.
 * @author Deniz Celebi
 * @since 1.0.2
 */
public class FotoFragment extends Fragment {
    
	ProgressDialog pd;
	String Eurl = "?json=1&include=attachments";
	String url;
	GridView fotoListView;
	TextView nopic;
	ConnectionDetector cd;
	SinglePost sp;
	Post p;
	ArrayList<String> fulls;
	ArrayList<Attachment> fotoList= null;
	EasyTracker easyTracker = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_foto, container, false);
		
		url = getActivity().getIntent().getStringExtra("news_url");
		easyTracker = EasyTracker.getInstance(getActivity());
		cd = new ConnectionDetector(getActivity());
		
		easyTracker.send(MapBuilder.createEvent("Open Fragment","Fragment", "Fotos", null).build());
		
		if(isCon() == true){
		new DownloadImgTask().execute(url+Eurl);
		}else{
			cd.makeAlert();
		}
		
		fotoListView= (GridView) v.findViewById(R.id.gridfoto);
		nopic = (TextView) v.findViewById(R.id.kein_bild);
        return v;
    }
	
	private Boolean isCon() {
		cd = new ConnectionDetector(getActivity());
		return cd.isConnectingToInternet();
	}
	
	public void updateGrid() {
		if(fotoList != null && fotoList.size() >= 1){
		fotoListView.setVisibility(View.VISIBLE);
		fotoListView.setAdapter(new FotoAdapter(getActivity(), fotoList));
		fotoListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,	long id) {	
				Log.e("JGG FOTOS", String.valueOf(fulls.size()));
				String uri = fulls.get(position);
				Log.e("JGG FOTOS", uri);
				Intent intent = new Intent(getActivity(), FullscreenImage.class);
			//	intent.putStringArrayListExtra("fotos", fulls);
				intent.putExtra("foto_uri",  uri);
				startActivity(intent);
			}
		});
		
		}else{
			nopic.setText("kein Bild verfügbar!");
		}
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
	
	
	private class DownloadImgTask extends AsyncTask<String, Integer, Void> {

		@Override
		protected void onProgressUpdate(Integer... values) {
		}

		@Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(getActivity(), "", "Bitte warten...");
        }

		@Override
		protected void onPostExecute(Void result) {
				updateGrid();
				pd.dismiss();
		}

		@Override
		protected Void doInBackground(String... params) {
			String url = params[0];
			try {
			     InputStream source = retrieveStream(url);
	             Gson gson = new Gson();        
	             Reader reader = new InputStreamReader(source);        
	        	 sp  = gson.fromJson(reader, SinglePost.class);
	        	 p = sp.getPost();
	        	 List<Attachment> atts = p.getAttachments();
	        	 fulls = new ArrayList<String>();
	        	 for(int i = 0; i < atts.size(); i++) {
	     	         Attachment att = (Attachment) atts.get(i);
	     	         fulls.add(att.getUrl());
	        	 }
	        	 fotoList = new ArrayList<Attachment>();
	        	 for(Attachment att: p.getAttachments()) {
	        		 fotoList.add(att);
	        	 }
	        	 
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