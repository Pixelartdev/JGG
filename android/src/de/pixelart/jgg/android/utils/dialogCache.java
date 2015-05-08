package de.pixelart.jgg.android.utils;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.pixelart.jgg.android.R;

/**
 * Ermöglicht das löschen der in der Cache gespeocherten Bilder.
 * @author Deniz
 * @since 1.0.2
 */
public class dialogCache extends DialogPreference {

	private Context con;
	
	public dialogCache(Context oContext, AttributeSet attrs){
        super(oContext, attrs);  
        setDialogLayoutResource(R.layout.dialog_cache);
		con = oContext;
    }
	
	@Override
	protected void onBindDialogView(View view) {
	Button ok = (Button)view.findViewById(R.id.btn_prev);
		ok.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
				//    FileCache fc = new FileCache(con);
				 //   fc.clear();
					ImageLoader il = new ImageLoader(con);
					il.clearCache();
					TouchImageLoader til = new TouchImageLoader(con);
					til.clearCache();
					getDialog().dismiss();
				}
			});
		Button exit = (Button)view.findViewById(R.id.btn_more);
		exit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
				getDialog().dismiss();
				}
			});
		super.onBindDialogView(view);
	}
}
