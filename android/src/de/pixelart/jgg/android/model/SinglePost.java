
package de.pixelart.jgg.android.model;

import com.google.gson.annotations.Expose;

public class SinglePost {

    @Expose
    private String status;
    @Expose
    private Post post;
    @Expose
    private String previous_url;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getPrevious_url() {
        return previous_url;
    }

    public void setPrevious_url(String previous_url) {
        this.previous_url = previous_url;
    }

}
