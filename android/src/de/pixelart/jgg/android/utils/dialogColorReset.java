package de.pixelart.jgg.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.pixelart.jgg.android.R;

/**
 * Ermöglicht das resetten der Farben in dem Bonusbereich
 * @author Deniz
 * @since 1.0.2
 */
public class dialogColorReset extends DialogPreference {
	
	Context con;
	
	public dialogColorReset(Context oContext, AttributeSet attrs){
        super(oContext, attrs);  
        setDialogLayoutResource(R.layout.dialog_colorclear);
        con = oContext;
    }
	
	@Override
	protected void onBindDialogView(View view) {
	Button ok = (Button)view.findViewById(R.id.btn_prev);
		ok.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
					SharedPreferences.Editor editor = prefs.edit();
					editor.remove("launcher_back");
					editor.remove("launcher_text");
					editor.remove("news_back");
					editor.remove("news_text");
					editor.remove("vp_back");
					editor.remove("vp_text");
					editor.remove("termin_back");
					editor.remove("termin_text");
					editor.remove("details_back");
					editor.remove("details_text");
					editor.commit();
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
