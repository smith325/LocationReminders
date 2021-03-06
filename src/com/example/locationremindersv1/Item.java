package com.example.locationremindersv1;

import java.util.Calendar;



public class Item {
	private Integer id=null;
	private String item=null;
	private String store=null;
    private String geoLocation = null;
	private Calendar date=Calendar.getInstance();
	
	public Item(String items, String stores, Calendar dates, String geoLocation) {
		// TODO - fill in here, please note you must have more arguments here
		items = item;
		stores = store;
		dates = date;
        geoLocation = geoLocation;
	}
	
	public Item(){
		item =" ";
		store =" ";
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}
    public String getLoc() {
        return geoLocation;
    }
    public void setItem(String item) {
		this.item = item;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
}
