package de.pixelart.jgg.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.pixelart.jgg.android.R;
import de.pixelart.jgg.android.model.CatItem;

public class CatAdapter extends BaseAdapter {

	private ArrayList<CatItem> listData;
	private LayoutInflater layoutInflater;
	@SuppressWarnings("unused")
	private Context mContext;
	
	public CatAdapter(Context context, ArrayList<CatItem> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		mContext = context;
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
			convertView = layoutInflater.inflate(R.layout.row_cat, null);
			holder = new ViewHolder();
			holder.headlineView = (TextView) convertView.findViewById(R.id.title);
			holder.countView = (TextView) convertView.findViewById(R.id.count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CatItem catItem = (CatItem) listData.get(position);
		
		if(catItem.getParent() > 0) {
			holder.headlineView.setText(Html.fromHtml(Html.fromHtml("&#8212; ")+catItem.getTitle()));
		}else {
			holder.headlineView.setText(Html.fromHtml(catItem.getTitle()));
		}
		
		holder.countView.setText(Html.fromHtml("Hat " + catItem.getPostCount() + " Artikel"));
		return convertView;
	}

	static class ViewHolder {
		TextView headlineView;
		TextView countView;
	}
}
