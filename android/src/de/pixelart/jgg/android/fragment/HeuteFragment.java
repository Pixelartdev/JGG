package de.pixelart.jgg.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import de.pixelart.jgg.android.R;

/**
 * Zeigt die Lageplšne der Schule an
 * @author Deniz
 * @since 1.5
 */
public class HeuteFragment extends Fragment {
	
	View v;
	WebView wH;
	EasyTracker easyTracker = null;
	String content, css;
	
	public HeuteFragment(String content) {
		this.content = content;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		easyTracker = EasyTracker.getInstance(getActivity());
		easyTracker.send(MapBuilder.createEvent("Open Fragment","Fragment", "Vertretungsplan Heute", null).build());
 
		v = inflater.inflate(R.layout.fragment_vp_heute, container, false);
		wH = (WebView) v.findViewById(R.id.wH);
		wH.getSettings().setLoadWithOverviewMode(true);
		wH.getSettings().setUseWideViewPort(true);
		
		css = "<style type=\"text/css\">" 
				+"h1 { color: #ee7f00; font-size: 150%; font-weight: normal;} "
				+"h1 strong { font-size: 200%; font-weight: normal; }"
				+"h2 { font-size: 125%;}"
				+"h1, h2 { margin: 0; padding: 0;}"
				+"th { background: #000; color: #fff; }"
				+"table.mon_list th, td { padding: 8px 4px;}"
				+""
				+".inline_header {"
				+"	font-weight: bold;"
				+"}"
				+"body {"
				+"	background: #d5d5d5;"
				+"}"
				+""
				+"table.mon_list {"
				+"	color: #000000;"
				+"	width: 100%; "
				+"	font-size: 120%;"
				+"	border: 1px;"
				+"	border-style:solid;"
				+"	border-collapse:collapse;"
				+"}"
				+""
				+"td.info,"
				+"th.list,"
				+"td.list,"
				+"tr.list {"
				+"	border: 1px;"
				+"	border-style: solid;"
				+"	border-color: black;"
				+"	margin: 0px;"
				+"	border-collapse:collapse;"
				+"	padding: 3px;"
				+"}"
				+""
				+"tr.odd { background: #fad3a6; }"
				+"tr.even { background: #fdecd9; }"
				+""
				+"</style>";
		
		if(content != null) {
		    content = "<meta http-equiv=\"Conent-Type\" content=\"text/html; charset=iso-8859-1\" />\n"+ content;
		    content= css+content;
		    wH.loadDataWithBaseURL("jgg://jgg.app", content, "text/html", "UTF-8", null);
		}
		
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
