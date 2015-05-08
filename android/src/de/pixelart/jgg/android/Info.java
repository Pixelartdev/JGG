package de.pixelart.jgg.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

public class Info extends ActionBarActivity {  

	ImageView brand;
	int counts = 0;
	EasyTracker easyTracker = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		easyTracker = EasyTracker.getInstance(Info.this);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Impressum");
		setSupportActionBar(toolbar);
		
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		brand = (ImageView) findViewById(R.id.brand);
		brand.setClickable(true);
		brand.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				counts++;
				easyTracker.send(MapBuilder.createEvent("Click Count","Info Screen", String.valueOf(counts), null).build());
				
				if(counts == 3){
					profiAlert();
					easyTracker.send(MapBuilder.createEvent("Click Count","Info Screen", "Profi Modus", null).build());
					enableSetting();
				}
				if(counts == 15){
					easyTracker.send(MapBuilder.createEvent("Click Count","Info Screen", "Kein Reallife", null).build());
					RealLifeAlert();
				}  
			}			
		});
	}
	
	private void enableSetting() {
		SharedPreferences prefs = getSharedPreferences("PREFERENCE_APP", 0);
		SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("BonusAktiv", true);
        editor.commit();
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
	
	private void profiAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Hallo im Profi-Modus");
		alertDialogBuilder
			.setMessage("Du bist jetzt im Profi-Modus und hast damit ein paar coole Sachen freigeschaltet.\n" +
					"Du kannst jetzt z.B. die Farben der App ändern!\n" +
					"- Deniz")
			.setCancelable(false)
			.setPositiveButton("Ok Deniz",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				}})
			.setNegativeButton(Html.fromHtml("Schließen"),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
					finish();
				}});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	private void RealLifeAlert(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Kein Real Life?");
		alertDialogBuilder
			.setMessage("Du hast jetzt 20 mal drauf geklickt, hast du kein real life?")
			.setCancelable(false)
			.setPositiveButton("Doch, habe ich",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
				}})
			.setNegativeButton(Html.fromHtml("Nein, was ist das?"),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					WussteIchAlert();
					dialog.dismiss();
				}});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	private void WussteIchAlert(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Hör auf!");
		alertDialogBuilder
			.setMessage("Ok, jetzt reicht es!")
			.setCancelable(false)
			.setPositiveButton("Ok, mache ich",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
					finish();
				}})
			.setNegativeButton(Html.fromHtml("Ok, mache ich!"),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
					finish();
				}});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
/*	
	private void baboAlert(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Willkommen im Babo-Modus");
		alertDialogBuilder
			.setMessage("Hier gibts nur extras für den ultimative Boss!\n" +
					"Erstmal deine Handynummer:" + handynr +
					"\nDeine E-Mail:" + email)
			.setCancelable(false)
			.setPositiveButton("ok Babo",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
				}})
			.setNegativeButton(Html.fromHtml("Schließen"),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
				}});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	} } */
}
