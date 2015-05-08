package de.pixelart.jgg.android.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import de.pixelart.jgg.android.R;

/**
 * Einführung Werbung
 * @author Deniz
 * @since 1.5.9
 */
public class FragmentAd extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View v = inflater.inflate(R.layout.fragment_help_ad, container, false);
		
		Button btn = (Button) v.findViewById(R.id.btn_next);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				FragmentEaster ee = new FragmentEaster();
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.tourwrapper, ee);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
		
		return v;
    }
}
