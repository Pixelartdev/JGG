package de.pixelart.jgg.android;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.pixelart.jgg.android.fragment.LageFragment;
import de.pixelart.jgg.android.utils.ConnectionDetector;
import de.pixelart.jgg.android.utils.OnSwipeTouchListener;

/**
 * Hier beginnt die ganze App.
 * @author Deniz
 * @since 1.0.2
 * @version 1.6.1
 */
public class Launcher extends ActionBarActivity {

	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    String SENDER_ID, regid, responseBody, version, appversion, swipes;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
	String condition = "0";
	Context context;
	RelativeLayout news, vp, termin, setting, content;
	TextView raum;
	ConnectionDetector cd;
	EasyTracker easyTracker = null;
	MediaPlayer player;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);	
		
		context = this;
		setActionBar();
		easyTracker = EasyTracker.getInstance(Launcher.this);
		SENDER_ID  = getString(R.string.gcm_sender);
		swipes = "";
		
		player = MediaPlayer.create(Launcher.this, R.raw.harlemshake);		
		content = (RelativeLayout) findViewById(R.id.content_launch);
		news = (RelativeLayout) findViewById(R.id.btn_news);
		vp = (RelativeLayout) findViewById(R.id.btn_vp);
		termin = (RelativeLayout) findViewById(R.id.btn_termin);
		setting = (RelativeLayout) findViewById(R.id.btn_setting);
		raum = (TextView) findViewById(R.id.raumplan);
		
		news.setOnClickListener(hand);
		vp.setOnClickListener(hand);
		termin.setOnClickListener(hand);
		setting.setOnClickListener(hand);
		raum.setOnClickListener(hand);
		
		if(getIntent().getBooleanExtra("shake", false)) {
			harlemShake();
		}
		
		content.setOnTouchListener(new OnSwipeTouchListener(context) {
			public void onSwipeTop() {
				swipes += "up,";
				Log.e("jgg swipe","top");
				Log.e("jgg swipe","swipes: "+swipes);
				showEasterEgg();
			}
			
			public void onSwipeRight() {
				swipes += "right,";
				Log.e("jgg swipe","right");
				Log.e("jgg swipe","swipes: "+swipes);
				showEasterEgg();
			}
			
			public void onSwipeBottom() {
				swipes += "down,";
				Log.e("jgg swipe","bottom");
				Log.e("jgg swipe","swipes: "+swipes);
				showEasterEgg();
			}
			
			public void onSwipeLeft() {
				swipes += "left,";
				Log.e("jgg swipe","left");
				Log.e("jgg swipe","swipes: "+swipes);
				showEasterEgg();
			}
		});
		
		startup();
		
		condition = getIntent().getStringExtra("alert");
		if(condition != null){
			alert();
		}
	}
	
	private void setActionBar() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int back = prefs.getInt("launcher_back", 0xFF34495E);
		int text = prefs.getInt("launcher_text", 0xFFFFFFFF);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if(toolbar != null) {
			toolbar.setTitle("Johanna Geissmar Gymnasium");
			toolbar.setTitleTextColor(text);
			setSupportActionBar(toolbar);
		}
		
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(back));
		getSupportActionBar().setDisplayShowTitleEnabled(true);
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
		player.stop();
	}	
	
	
	/**
	* Start Methoden 
	**/
	private void startup() {
		// check if play services are present
    /**    if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            if (regid.isEmpty()) {
            	if(isCon()) {
            		new registerInBackground().execute();
            	}
        	}
        } */
		
		doIntro();
		harlemCount();
		colorCount();
		doUpdateDone();
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		/* Update Check */
		if(pref.getBoolean("isUpdateCheck" , false) && isCon()) {
	    	new updateTask().execute();
		}
	}
	
	/**
	 * Intro App
	 */
	private void doIntro() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if(pref.getBoolean("first_start", true)) {
			Intent i = new Intent(Launcher.this, Help.class);
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean("first_start", false);
			editor.commit();
			startActivity(i);
		}
	}
	
	/**
	 * Update Nachricht
	 */
	private void alert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.noti_title);
		alertDialogBuilder
			.setMessage(R.string.noti_text)
			.setCancelable(false)
			.setPositiveButton("Jetzt updaten",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
					  Uri.parse("https://play.google.com/store/apps/details?id=de.pixelart.jgg.android"));
					startActivity(browserIntent);
				}})
			.setNegativeButton(Html.fromHtml("Sp&auml;ter"),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	// click listener for the dashboard buttons
	View.OnClickListener hand = new View.OnClickListener() {
			public void onClick(View v) {
				switch(v.getId()) {
					case R.id.btn_news:
						easyTracker.send(MapBuilder.createEvent("Dashboard Buttons","Open Activity", "btn_news", null).build());
						if(isCon()) {
							Intent news = new Intent(Launcher.this, News.class);
							startActivity(news);
						}else {
							cd.makeAlert();
						}
						break;
					case R.id.btn_vp:
						easyTracker.send(MapBuilder.createEvent("Dashboard Buttons","Open Activity", "btn_vp", null).build());					
						Intent vp = new Intent(Launcher.this, VP.class);
						startActivity(vp);
						break;
					case R.id.btn_termin:
						easyTracker.send(MapBuilder.createEvent("Dashboard Buttons","Open Activity", "btn_termin", null).build());
						if(isCon()) {
							Intent termin = new Intent(Launcher.this, Termine.class);
							startActivity(termin);
						}else {
							cd.makeAlert();
						}
						break;
					case R.id.btn_setting:
						easyTracker.send(MapBuilder.createEvent("Dashboard Buttons","Open Activity", "btn_setting", null).build());
						Intent setting = new Intent(Launcher.this, SettingsActivity.class);
						startActivity(setting);
						break;
					case R.id.raumplan:
						easyTracker.send(MapBuilder.createEvent("Dashboard Buttons","Open Activity", "btn_raumplan", null).build());
						LageFragment lage = new LageFragment();
						FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
						transaction.replace(R.id.wrapper, lage);
						transaction.addToBackStack(null);
						transaction.commit();
						break;
				}
			}
     	};
	
	
	/**
	 * Ruft aktuellste Version ab
	 */
	private class updateTask extends AsyncTask <Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			if(appversion != null && appversion.equals(getString(R.string.version))) {
			}else {
				showNotification(null,null,false);
			}
		}

		protected Void doInBackground(Void... unused) {
		//	updatecheck();
			return null;
			
		}
	}
	
	/**
	 * Checkt ob die aktuellste Version installiert ist
	 * @return 
	 */
/*	private String updatecheck() {
		if(!isCon()) {
			try {
				String postQuery = "http://pastebin.com/raw.php?i=CrUFmLFd";
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(postQuery);
				HttpResponse response = httpclient.execute(httppost); 
				HttpEntity entity = response.getEntity();
				appversion = entity.getContent().toString();
			}catch(Exception e) {
				Log.e("Update Check", "Error in http connection " + e.toString());
			}
		}
		
		return appversion;
	}   */
	
	/**
	 * Checkt ob Update durchgeführt wurde
	 */
	private void doUpdateDone() {
		final int currentVersion = R.string.version;
		SharedPreferences prefs = getSharedPreferences("PREFERENCE_APP", 0);
		int lastVersion = prefs.getInt("LastVersion", -1);
		if(currentVersion > lastVersion) {
			AlertDialog.Builder alert = new AlertDialog.Builder(Launcher.this);
			WebView wv = new WebView(Launcher.this);
			wv.loadUrl("file:///android_asset/new_changelog.html");
			wv.setWebViewClient(new WebViewClient() {
					@Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						view.loadUrl(url);
						return true;
					}});
			alert.setView(wv);
			alert.setNegativeButton(getString(R.string.btn_close), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						SharedPreferences prefs = getSharedPreferences("PREFERENCE_APP", 0);
						SharedPreferences.Editor editor = prefs.edit();
						editor.putInt("LastVersion", currentVersion);
						editor.commit();
						dialog.dismiss();
					}});
			alert.show();
		}			
	}
	
	/**
	* Statusbar Nachricht 
	**/
	private void showNotification(String title, String content, boolean inte){
		if(title == null) {
			title = getString(R.string.noti_title);
		}
		if(content == null) {
			content = getString(R.string.noti_text);
		}
		
		Intent intent = new Intent(this, Launcher.class);
		if(!inte) {
			intent.putExtra("alert", "1");
		}
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean vibrate = prefs.getBoolean("notifications_vibrate", true);
		PendingIntent pIntent = PendingIntent.getActivity(Launcher.this, 0, intent, 0);
        Uri soundUri = Uri.parse(prefs.getString("notifications_ringtone", "none"));
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		
        if(vibrate == true ){
        	Notification mNotification = new NotificationCompat.Builder(this)
				.setContentTitle(title)
				.setContentText(content)
				.setLargeIcon(bm)
				.setSmallIcon(R.drawable.ic_notification)
				.setContentIntent(pIntent)
				.setSound(soundUri)
				.setTicker(title + "\n" + content)
				.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE)
				.build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, mNotification);
        }else {
        	Notification mNotification = new NotificationCompat.Builder(this)
				.setContentTitle(title)
				.setContentText(content)
				.setLargeIcon(bm)
				.setSmallIcon(R.drawable.ic_notification)
				.setContentIntent(pIntent)
				.setSound(soundUri)
				.setTicker(title + "\n" + content)
				.setDefaults(Notification.DEFAULT_LIGHTS)
				.build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, mNotification);
        }
    }
	
	
	/**
	 * 
	 * GCM related stuff
	 * 
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(resultCode != ConnectionResult.SUCCESS) {
			if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			}else {
				finish();
			}
			return false;
		}
		return true;
	}
	
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if(registrationId.isEmpty()) {
			return "";
		}
	
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if(registeredVersion != currentVersion) {
			return "";
		}
		return registrationId;
	}
	
	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(Launcher.class.getSimpleName(), Context.MODE_PRIVATE);
	}
	
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		}catch(PackageManager.NameNotFoundException e) {
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	
	// register device online
	private class registerInBackground extends AsyncTask<Void, Integer, String> {
		@Override
        protected String doInBackground(Void... params) {
            String msg = "";
            try {
                if(gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered \n registration ID=" + regid;
                sendRegistrationIdToBackend();
				
                storeRegistrationId(context, regid);
            }catch(IOException ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }
	}
	
	/**
	 * This Function registers your devices for GCM on your WordPress Blog
	 */
	private void sendRegistrationIdToBackend() {
		String os = android.os.Build.VERSION.RELEASE;
		String model = getDeviceName();
		os = os.replaceAll(" ", "%20");
		model = model.replaceAll(" ", "%20");
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(getString(R.string.blogurl)+
				"?regId="+regid+
				"&os=Android%20"+os+
				"&model="+model);
		try {
			HttpResponse response = httpclient.execute(httppost);	
			responseBody = EntityUtils.toString(response.getEntity());
		}catch(ClientProtocolException e) {
		}catch(IOException e) {
		}
	}
	
	// Get the device model name with manufacturer
	public String getDeviceName() {
	    String manufacturer = Build.MANUFACTURER;
	    String model = Build.MODEL;
	    if(model.startsWith(manufacturer)) {
	        return capitalize(model);
	    }else {
	        return capitalize(manufacturer) + " " + model;
	    }
	}

	private String capitalize(String s) {
	    if(s == null || s.length() == 0) {
	        return "";
	    }
	    char first = s.charAt(0);
	    if (Character.isUpperCase(first)) {
	        return s;
	    }else {
	        return Character.toUpperCase(first) + s.substring(1);
	    }
	}
	
	// Save the device id
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	
	/** Easter Eggs */	
	
	/* ad easter egg */
	private void showEasterEgg() {
		if(swipes.equals("up,right,")) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("popup_ad", false);
			editor.commit();
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle(R.string.noad_title);
			alertDialogBuilder
				.setMessage(R.string.noad_text)
				.setCancelable(false)
				.setNegativeButton("Danke",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						swipes = "";
						dialog.cancel();
					}});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}
	
	/* harlem shake easter egg */
	private void harlemShake() {
		final Animation anim_lr = AnimationUtils.loadAnimation(this, R.anim.anim_left_right);
		final Animation anim_td = AnimationUtils.loadAnimation(this, R.anim.anim_top_down);
		final Animation anim_scale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
		final Animation anim_rotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
		
		//start
		player.start();
		news.startAnimation(anim_lr);
		
		
		//start all
		new Handler().postDelayed(new Runnable() {
			public void run() {
				vp.startAnimation(anim_rotate);
				termin.startAnimation(anim_scale);
				setting.startAnimation(anim_td);
			}
		}, 13300);
		
		//end all
		new Handler().postDelayed(new Runnable() {
			public void run() {
				news.clearAnimation();
				vp.clearAnimation();
				termin.clearAnimation();
				setting.clearAnimation();
				player.stop();
			}
		}, 27000);		
	}
	
	/* harlem notification */
	private void harlemCount() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int starts = prefs.getInt("harlem_starts", 0);
		
		Log.e("JGG harlem starts", "starts: "+starts);
		
		if(starts == 17 || starts == 20 || starts == 23) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle(R.string.title_know);
			alertDialogBuilder
				.setMessage(R.string.msg_harlem)
				.setCancelable(false)
				.setNegativeButton("Cool",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}else {
			starts++;
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("harlem_starts", starts);
			editor.commit();
		}
	}
	
	/* harlem notification */
	private void colorCount() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int starts = prefs.getInt("noti_starts", 0);
		
		Log.e("JGG color starts", "starts: "+starts);
		
		if(starts == 5 || starts == 15 || starts == 27) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle(R.string.title_know);
			alertDialogBuilder
				.setMessage(R.string.msg_color)
				.setCancelable(false)
				.setNegativeButton("Cool",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}else {
			starts++;
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("noti_starts", starts);
			editor.commit();
		}
	}
	/** Easter Egg Ende */
	
}