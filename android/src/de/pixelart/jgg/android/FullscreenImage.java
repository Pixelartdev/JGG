package de.pixelart.jgg.android;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import de.pixelart.jgg.android.R;
import de.pixelart.jgg.android.utils.ConnectionDetector;
import de.pixelart.jgg.android.utils.TouchImageLoader;
import de.pixelart.jgg.android.utils.TouchImageView;

/**
 * Zeigt ein BilD im Vollbildmodus.
 * @author Deniz
 * @since
 */
public class FullscreenImage extends ActionBarActivity {
	
	TouchImageView img;
	TouchImageLoader imageLoader;
	String url;
	ConnectionDetector cd;
	EasyTracker easyTracker = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.row_fullscreen);
		
		cd = new ConnectionDetector(this);
		easyTracker = EasyTracker.getInstance(FullscreenImage.this);
		
		url = this.getIntent().getStringExtra("foto_uri");
		img = (TouchImageView) findViewById(R.id.show_lage);
		imageLoader = new TouchImageLoader(this);
		
		easyTracker.send(MapBuilder.createEvent("Fullscreen Image","Open Image", url, null).build());
		
		if(null != url && isCon() != false) {
		    imageLoader.DisplayImage(url, img);
		}else{cd.makeAlert();}
		
		img.setClickable(true); 
		img.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View arg0) {
	        }
	    });
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
}
