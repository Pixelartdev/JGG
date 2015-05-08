package de.pixelart.jgg.android.gcm;

import de.pixelart.jgg.android.Launcher;
import de.pixelart.jgg.android.PostDetails;
import de.pixelart.jgg.android.R;
import de.pixelart.jgg.android.VP;
import de.pixelart.jgg.android.utils.VpDownload;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;


/**
 * This class Handles what to do when the user presses the NotificationFragment
 */
public class GcmIntentHandle extends ActionBarActivity {
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        String todo = getIntent().getStringExtra("todo");
        
        if(todo.equals("newPost")) {
        	newPost();
        }else if(todo.equals("updatePost")) {
        	updatePost();
        }else if(todo.equals("message")) {
        	message();
        }else if(todo.equals("vpNew")) {
        	newVP();
        }else if(todo.equals("vpUpdate")) {
        	updateVP();
        }
    }
    
    private void newPost() {
    	String title = getIntent().getStringExtra("post_title");
    	String url = getIntent().getStringExtra("post_url");
    	String id = getIntent().getStringExtra("post_id");
    	String author = getIntent().getStringExtra("post_author");
    	
    	Intent intent = new Intent(GcmIntentHandle.this, PostDetails.class);
 		intent.putExtra("news_title", title);
 		intent.putExtra("news_url", url);
 		intent.putExtra("news_id", id);
 		intent.putExtra("news_author", author);
 		startActivity(intent);
 		finish();
    }
    
    private void updatePost() {
    	String title = getIntent().getStringExtra("post_title");
    	String url = getIntent().getStringExtra("post_url");
    	String id = getIntent().getStringExtra("post_id");
    	String author = getIntent().getStringExtra("post_author");
    	
    	Intent intent = new Intent(GcmIntentHandle.this, PostDetails.class);
 		intent.putExtra("news_title", title);
 		intent.putExtra("news_url", url);
 		intent.putExtra("news_id", id);
 		intent.putExtra("news_author", author);
 		startActivity(intent);
 		finish();
    }
    
    private void message() {
    	String ms = getIntent().getStringExtra("msg");
    	ms = Html.fromHtml(ms).toString();
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(getString(R.string.title_gcm_alert));
		alertDialogBuilder
			.setMessage(ms)
			.setCancelable(false)
			.setPositiveButton(getString(R.string.btn_ok),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
					Intent i = new Intent(GcmIntentHandle.this, Launcher.class);
					startActivity(i);
					finish();
				}});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
    }
    
    private void newVP() {
    	loadVp();
    	Intent intent = new Intent(GcmIntentHandle.this, VP.class);
 		startActivity(intent);
 		finish();
    }
    
    private void updateVP() {
    	loadVp();
    	Intent intent = new Intent(GcmIntentHandle.this, VP.class);
 		startActivity(intent);
 		finish();
    }
    
    private void loadVp() {
    	VpDownload vpd = new VpDownload(this);
    	vpd.delete();
    	vpd.work();
    }
}