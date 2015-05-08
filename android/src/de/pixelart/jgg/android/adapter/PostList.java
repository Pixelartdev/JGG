package de.pixelart.jgg.android.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.pixelart.jgg.android.R;
import de.pixelart.jgg.android.model.Author;
import de.pixelart.jgg.android.model.Medium_image;
import de.pixelart.jgg.android.model.Posts;
import de.pixelart.jgg.android.model.Thumbnails_images;
import de.pixelart.jgg.android.utils.ImageLoader;

public class PostList extends BaseAdapter {

	private List<Posts> listData;
	private LayoutInflater layoutInflater;
	public ImageLoader imageLoader;
	/* !!*/
	Posts posts;
	Thumbnails_images thimg;
	Medium_image img;
	Author author;
	/* !! */
	
	
	public PostList(Context context, List<Posts> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		
		imageLoader = new ImageLoader(context);
	}

	public int getCount() {
		return listData.size();
	}
	
	public void add(Posts posts) {
	    listData.add(posts);
	    notifyDataSetChanged();
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
			convertView = layoutInflater.inflate(R.layout.row_news, null);
			holder = new ViewHolder();
			holder.headlineView = (TextView) convertView.findViewById(R.id.title);
			holder.commentView = (TextView) convertView.findViewById(R.id.comment);
			holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
			holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		posts = (Posts) listData.get(position);
        if(posts != null){	
        	author = (Author) posts.getAuthor();
 			if(posts.getThumb_img() != null){
 				thimg = posts.getThumb_img();
 				if(thimg.getMedium() != null){
 					img = thimg.getMedium();
 				}else{
 					thimg.setMedium(null);
 					img.setUrl(null);
 				}
 			}else{
 				posts.setThumb_img(null);
 			}
			
			holder.headlineView.setText(Html.fromHtml(posts.getTitle()));
			holder.reportedDateView.setText(Html.fromHtml("<b>"+posts.getDate()+"</b>"));
			holder.commentView.setText(Html.fromHtml("Von: <b>"  + author.getName()));
			
		    ImageView image = holder.imageView;		    
		    try {
				if(img.getUrl() != null && posts.getThumb_img() != null && thimg.getMedium() != null) {
				    image.setImageResource(0);
					imageLoader.DisplayImage(img.getUrl(), image);
				}else {
					image.setImageResource(0);
					image.setImageResource(R.drawable.list_placeholder);
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
	  }		
		return convertView;
	}
	

	static class ViewHolder {
		TextView headlineView;
		TextView commentView;
		TextView reportedDateView;
		ImageView imageView;
	}
}