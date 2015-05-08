
package de.pixelart.jgg.android.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class Post {

    @Expose
    private int id;
    @Expose
    private String content;
    @Expose
    private String date;
  /*  @Expose
    private List<Category> categories = new ArrayList<Category>();
    @Expose
    private List<Object> tags = new ArrayList<Object>();
  */
    @Expose
    private List<Attachment> attachments = new ArrayList<Attachment>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }
*/
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
