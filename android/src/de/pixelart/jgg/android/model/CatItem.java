package de.pixelart.jgg.android.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CatItem implements Serializable {

	private String title;
	private int id;
	private String slug;
	private String description;
	private int post_count;
	private int parent;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getPostCount() {
		return post_count;
	}

	public void setPostCount(int post_count) {
		this.post_count = post_count;
	}
	
	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
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
