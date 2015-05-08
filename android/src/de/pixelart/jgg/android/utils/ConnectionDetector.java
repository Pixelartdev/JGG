package de.pixelart.jgg.android.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import de.pixelart.jgg.android.R;
 

/**
 * Checkt die Internet Verbindung
 * @author Deniz
 * @since 1.0.1
 */
public class ConnectionDetector {
      
        private Context con;
        private Boolean isConnected;
         
        public ConnectionDetector(Context context) {
            this.con = context;
        }
     
        public boolean isConnectingToInternet() {
            ConnectivityManager connectivity = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
              if (connectivity != null) {
                  NetworkInfo[] info = connectivity.getAllNetworkInfo();
                  if (info != null) 
                      for (int i = 0; i < info.length; i++) 
                          if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        	  return true;
                          }
              }
              return false;
        }
        
        public void Check() {
        	isConnected = isConnectingToInternet();
        	if(isConnected != true) {
        		makeAlert();
        	}
        }
        
        public void makeAlert() {
        	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
    		alertDialogBuilder.setTitle(R.string.internet_title);
    		alertDialogBuilder
    			.setMessage(R.string.internet_text)
    			.setCancelable(false)
    			.setPositiveButton("Wiederholen",new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog,int id) {
    					Check();
    				}})
    			.setNegativeButton(Html.fromHtml("Schließen"),new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog,int id) {
    					dialog.dismiss();
    				}});
    		AlertDialog alertDialog = alertDialogBuilder.create();
    		alertDialog.show();
    	}
}