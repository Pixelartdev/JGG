package de.pixelart.jgg.android.model;

import com.google.gson.annotations.Expose;

public class Thumbnails_images {

@Expose
private Full_image full;
private Medium_image medium;
	
	
	public Full_image getFull() {
        return full;
    }

    public void setFull(Full_image full) {
        this.full = full;
    }
    
    public Medium_image getMedium() {
        return medium;
    }

    public void setMedium(Medium_image medium) {
        this.medium = medium;
    }
}
