package de.pixelart.jgg.android.model;

import com.google.gson.annotations.Expose;


public class Author {

    @Expose
    private String name;
    @Expose
    private String first_name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author withName(String name) {
        this.name = name;
        return this;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public Author withFirst_name(String first_name) {
        this.first_name = first_name;
        return this;
    }
}
