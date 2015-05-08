package de.pixelart.jgg.android;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import de.pixelart.jgg.android.help.FragmentIntro;


/**
 *  Zeigt das Einführungs Tutorial
 * @author Deniz
 * @since 1.5.9
 *
 */
public class Help extends ActionBarActivity {  

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		FragmentIntro intro = new FragmentIntro();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.tourwrapper, intro);
		transaction.addToBackStack(null);
		transaction.commit();
		
	}
}
