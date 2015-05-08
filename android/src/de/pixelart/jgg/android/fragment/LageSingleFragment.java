package de.pixelart.jgg.android.fragment;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.pixelart.jgg.android.R;
import de.pixelart.jgg.android.utils.TouchImageView;

/**
 * Zeigt einen einzelnen Lageplan an.
 * @author Deniz
 * @since 1.0.2
 */
public class LageSingleFragment extends Fragment {
	
	View v;
	TouchImageView tiv;
	int res;
	EasyTracker easyTracker = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		easyTracker = EasyTracker.getInstance(getActivity());
		easyTracker.send(MapBuilder.createEvent("Open Fragment","Fragment", "Lage Plan", null).build());
 
		v = inflater.inflate(R.layout.fragment_lage_single, container, false);
		tiv = (TouchImageView) v.findViewById(R.id.show_lage);
		Bundle bundle = this.getArguments();
		if(bundle != null){
		    res = bundle.getInt("img");
		}
		tiv.setImageResource(res);
		return v;
    }
	
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(getActivity()).activityStart(getActivity());
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(getActivity()).activityStop(getActivity());
	}	
}
