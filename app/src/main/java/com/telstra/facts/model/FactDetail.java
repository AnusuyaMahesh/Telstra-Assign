package com.telstra.facts.model;

/**
 * Created by Anusuya on 3/12/2015.
 * Attributes named as in json to work with GSON
 */
public class FactDetail {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHref() {
        return imageHref;
    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }

    private String title;

    private String description;

    private String imageHref;

}
