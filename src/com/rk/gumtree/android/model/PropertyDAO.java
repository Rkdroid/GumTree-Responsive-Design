package com.rk.gumtree.android.model;

public final class PropertyDAO {
	public final String name;
	public final String price;
	public final String location;
	public final String date_Posted;
	public final String property_Type;
	public final String number_Beds;
	public final String seller_Type;
	public final String description;
	public final String phoneNumber;
	public final String email;
	
	public PropertyDAO(String name, String price, String location, String date_Posted,
			String property_Type, String number_Beds, String seller_Type,
			String description, String phoneNumber, String email) {
		this.name = name;
		this.price = price;
		this.location = location;
		this.date_Posted = date_Posted;
		this.property_Type = property_Type;
		this.number_Beds = number_Beds;
		this.seller_Type = seller_Type;
		this.description = description;
		this.phoneNumber = phoneNumber;
		this.email  = email;
	}
	
	
}
