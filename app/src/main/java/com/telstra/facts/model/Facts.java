package com.telstra.facts.model;

import java.util.ArrayList;

/**
 * Created by Anusuya on 3/12/2015
 * Attributes named as in json to work with GSON
 */
public class Facts {

    private String title;

    private ArrayList<FactDetail> rows;

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public ArrayList<FactDetail> getRows ()
    {
        return rows;
    }

    public void setRows (ArrayList<FactDetail> rows)
    {
        this.rows = rows;
    }

}
