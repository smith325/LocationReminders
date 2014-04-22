package com.example.licationremindersv1;

import java.util.Calendar;



public class Item {
	private Integer id=null;
	private String item=null;
	private String store=null;
	private Calendar date=Calendar.getInstance();
	
	public Item(String items, String stores, Calendar dates) {	
		// TODO - fill in here, please note you must have more arguments here
		items = item;
		stores = store;
		dates = date; 
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
