package de.pixelart.jgg.android.utils;

import de.pixelart.jgg.android.R;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

/**
 * Zeigt den Changelog in den EInstellungen an.
 * @author Deniz
 * @since 1.0.0
 */
public class dialogAdFree extends DialogPreference {

	private WebView wv;  
	
	public dialogAdFree(Context oContext, AttributeSet attrs){
        super(oContext, attrs);  
        setDialogLayoutResource(R.layout.dialog);
    }
	
	@Override
	protected void onBindDialogView(View view) {		
	    wv = (WebView) view.findViewById(R.id.clView);
	    wv.loadUrl("file:///android_asset/adfree.html");
	    super.onBindDialogView(view);
	}
}
