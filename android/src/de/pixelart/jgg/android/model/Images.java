package de.pixelart.jgg.android.model;

import com.google.gson.annotations.Expose;

public class Images {

    @Expose
    private Thumbnail thumbnail;
    

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Images withThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }
}
