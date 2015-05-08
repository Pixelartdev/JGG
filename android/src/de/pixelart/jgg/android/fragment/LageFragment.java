package de.pixelart.jgg.android.fragment;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import de.pixelart.jgg.android.R;

/**
 * Zeigt die Lagepläne der Schule an
 * @author Deniz
 * @since 1.0.2
 */
public class LageFragment extends Fragment {
	
	View v;
	ImageView ib;
	ImageView eg;
	ImageView og1;
	ImageView og2;
	int iber;
	int ieg;
	int iog1;
	int iog2;
	EasyTracker easyTracker = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		easyTracker = EasyTracker.getInstance(getActivity());
		easyTracker.send(MapBuilder.createEvent("Open Fragment","Fragment", "Lage Pläne", null).build());
 
		v = inflater.inflate(R.layout.fragment_lageplan, container, false);
		ib = (ImageView) v.findViewById(R.id.img_ibersicht);
		eg = (ImageView) v.findViewById(R.id.img_eg);
		og1 = (ImageView) v.findViewById(R.id.img_og1);
		og2 = (ImageView) v.findViewById(R.id.img_og2);
		ib.setOnClickListener(hand);
		eg.setOnClickListener(hand);
		og1.setOnClickListener(hand);
		og2.setOnClickListener(hand);
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
	
	View.OnClickListener hand = new View.OnClickListener() {
		public void onClick(View v) {
			switch(v.getId()) {
				case R.id.img_ibersicht:
					LageSingleFragment lage = new LageSingleFragment();
					Bundle data = new Bundle();
					data.putInt("img",R.drawable.jgg_ubersicht);
					FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
					lage.setArguments(data);
					transaction.replace(R.id.wrapper, lage);
					transaction.addToBackStack(null);
					transaction.commit();
					break;
				case R.id.img_eg:
					LageSingleFragment lage2 = new LageSingleFragment();
					Bundle data2 = new Bundle();
					data2.putInt("img",R.drawable.jgg_eg);
					FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();
					lage2.setArguments(data2);
					transaction2.replace(R.id.wrapper, lage2);
					transaction2.addToBackStack(null);
					transaction2.commit();
					break;
				case R.id.img_og1:
					LageSingleFragment lage3 = new LageSingleFragment();
					Bundle data3 = new Bundle();
					data3.putInt("img",R.drawable.jgg_og1);
					FragmentTransaction transaction3 = getActivity().getSupportFragmentManager().beginTransaction();
					lage3.setArguments(data3);
					transaction3.replace(R.id.wrapper, lage3);
					transaction3.addToBackStack(null);
					transaction3.commit();
					break;
				case R.id.img_og2:
					LageSingleFragment lage4 = new LageSingleFragment();
					Bundle data4 = new Bundle();
					data4.putInt("img",R.drawable.jgg_og2);
					FragmentTransaction transaction4 = getActivity().getSupportFragmentManager().beginTransaction();
					lage4.setArguments(data4);
					transaction4.replace(R.id.wrapper, lage4);
					transaction4.addToBackStack(null);
					transaction4.commit();
					break;
			}
		}
 	};
}
