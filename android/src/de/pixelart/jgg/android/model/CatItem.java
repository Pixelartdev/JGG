package de.pixelart.jgg.android.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CatItem implements Serializable {

	private String title;
	private String id;
	private String slug;
	private String description;
	private String post_count;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPostCount() {
		return post_count;
	}

	public void setPostCount(String post_count) {
		this.post_count = post_count;
	}
	
	public String getDesc() {
		return description;
	}

	public void setDesc(String description) {
		this.description = description;
	}
	
	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
}
