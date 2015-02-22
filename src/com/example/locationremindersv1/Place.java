package com.example.locationremindersv1;

/**
 * Created by sesmith325 on 5/10/14.
 */
public class Place {
    public String name, date, item;
    public boolean checked;
    public int id;

    public Place(String name, String date, String item, boolean checked, int id){
        this.name = name;
        this.date = date;
        this.item = item;
        this.checked = checked;
        this.id = id;
    }

}
