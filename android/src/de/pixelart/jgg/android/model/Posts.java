package de.pixelart.jgg.android.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class Posts {

    @Expose
    private int id;
    @Expose
    private String url;
    @Expose
    private String title;
    @Expose
    private String date;
 /*   @Expose
    private List<Category> categories = new ArrayList<Category>();
    @Expose
    private List<Object> tags = new ArrayList<Object>(); */
    @Expose
    private Author author;
    @Expose
    private Thumbnails_images thumbnail_images;
    @Expose
    private List<Attachment> attachments = new ArrayList<Attachment>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
/*
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
*/
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
    
    public Thumbnails_images getThumb_img() {
        return thumbnail_images;
    }

    public void setThumb_img(Thumbnails_images thumbnail_images) {
        this.thumbnail_images = thumbnail_images;
    }
/*
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    } */
}
