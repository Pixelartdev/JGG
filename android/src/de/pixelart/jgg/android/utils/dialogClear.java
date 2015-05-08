package de.pixelart.jgg.android.utils;

import android.content.Context;
import android.preference.DialogPreference;
import android.provider.SearchRecentSuggestions;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.pixelart.jgg.android.R;
import de.pixelart.jgg.android.searchProvider;

/**
 * Löschte den Suchverlauf.
 * @author Deniz
 * @since 1.0.1
 */
public class dialogClear extends DialogPreference {

	private Context con;
	
	public dialogClear(Context oContext, AttributeSet attrs){
        super(oContext, attrs);  
        setDialogLayoutResource(R.layout.dialog_clear);
		con = oContext;
    }
	
	@Override
	protected void onBindDialogView(View view) {
	Button ok = (Button)view.findViewById(R.id.btn_prev);
		ok.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					xy();
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
	
	private void xy(){
		SearchRecentSuggestions suggestions = new SearchRecentSuggestions(con,
		       searchProvider.AUTHORITY, searchProvider.MODE);
		suggestions.clearHistory();
	}
}
