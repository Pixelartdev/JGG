package de.pixelart.jgg.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import de.pixelart.jgg.android.R;
import de.pixelart.jgg.android.model.Attachment;
import de.pixelart.jgg.android.utils.ImageLoader;

public class FotoAdapter extends BaseAdapter {

	private ArrayList<Attachment> listData;
	private LayoutInflater layoutInflater;
	@SuppressWarnings("unused")
	private Context mContext;
	public ImageLoader imageLoader;
	
	public FotoAdapter(Context context, ArrayList<Attachment> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		mContext = context;
		
		imageLoader = new ImageLoader(context);
	}

	public int getCount() {
		return listData.size();
	}

	public Object getItem(int position) {
		return listData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.row_foto, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.imgGrid);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Attachment foto = (Attachment) listData.get(position);
		ImageView image = holder.imageView;
		if(foto != null){	
		
		   if(foto.getMime_type().contains("image")) {
              imageLoader.DisplayImage(foto.getUrl(), image);
		   }
		}
		return convertView;
	}
	

	static class ViewHolder {
		ImageView imageView;
	}
}
