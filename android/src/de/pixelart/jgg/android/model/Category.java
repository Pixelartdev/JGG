package de.pixelart.jgg.android.model;

import com.google.gson.annotations.Expose;


public class Category {

    @Expose
    private int id;
    @Expose
    private String title;
    @Expose
    private String description;
    @Expose
    private int parent;
    @Expose
    private int post_count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category withId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category withDescription(String description) {
        this.description = description;
        return this;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public Category withParent(int parent) {
        this.parent = parent;
        return this;
    }

    public int getPost_count() {
        return post_count;
    }

    public void setPost_count(int post_count) {
        this.post_count = post_count;
    }

    public Category withPost_count(int post_count) {
        this.post_count = post_count;
        return this;
    }

}
