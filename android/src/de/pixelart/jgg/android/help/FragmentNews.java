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
 * Einführung News
 * @author Deniz
 * @since 1.5.9
 */
public class FragmentNews extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View v = inflater.inflate(R.layout.fragment_help_news, container, false);
		
		Button btn = (Button) v.findViewById(R.id.btn_next);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				FragmentVp vp = new FragmentVp();
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.tourwrapper, vp);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
		
		return v;
    }
}
