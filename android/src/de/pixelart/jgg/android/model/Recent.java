package de.pixelart.jgg.android.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class Recent {

    @Expose
    private int count;
    @Expose
    private int count_total;
    @Expose
    private int pages;  
    @Expose
    private List<Posts> posts = new ArrayList<Posts>();

    
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount_total() {
        return count_total;
    }

    public void setCount_total(int count_total) {
        this.count_total = count_total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    
    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }
}
