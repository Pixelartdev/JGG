package de.pixelart.jgg.android.model;

import com.google.gson.annotations.Expose;

public class Thumbnail {

    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Thumbnail withUrl(String url) {
        this.url = url;
        return this;
    }
}
