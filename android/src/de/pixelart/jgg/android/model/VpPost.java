package de.pixelart.jgg.android.model;

import com.google.gson.annotations.Expose;

public class VpPost {
	
    @Expose
    private String timetablegroupname;
    @Expose
    private String timetableurl;

    
    public String getTimetableName() {
        return timetablegroupname;
    }
    public void setTimetableName(String timetablegroupname) {
        this.timetablegroupname = timetablegroupname;
    }

    
    public String getTimetableurl() {
        return timetableurl;
    }
    public void setTimetableurl(String timetableurl) {
        this.timetableurl = timetableurl;
    }

}
