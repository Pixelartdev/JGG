package de.pixelart.jgg.android.model;

import com.google.gson.annotations.Expose;

public class Attachment {

    @Expose
    private String url;
    @Expose
    private String mime_type;
    @Expose
    private Images images;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Attachment withUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public Attachment withMime_type(String mime_type) {
        this.mime_type = mime_type;
        return this;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Attachment withImages(Images images) {
        this.images = images;
        return this;
    }

}
