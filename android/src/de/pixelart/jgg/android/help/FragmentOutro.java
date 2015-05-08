package de.pixelart.jgg.android.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import de.pixelart.jgg.android.R;

/**
 * Einführung Outro
 * @author Deniz
 * @since 1.5.9
 */
public class FragmentOutro extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View v = inflater.inflate(R.layout.fragment_help_outro, container, false);
		
		Button btn = (Button) v.findViewById(R.id.btn_next);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		
		return v;
    }
}
