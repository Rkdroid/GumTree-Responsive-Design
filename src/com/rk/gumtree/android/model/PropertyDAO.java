package com.rk.gumtree.android.model;

public final class PropertyDAO {
	public final String Name;
	public final String Price;
	public final String Location;
	public final String Date_Posted;
	public final String Property_Type;
	public final String Number_Beds;
	public final String Seller_Type;
	public final String Description;
	public final String PhoneNumber;
	public final String Email;
	
	public PropertyDAO(String name, String price, String location, String date_Posted,
			String property_Type, String number_Beds, String seller_Type,
			String description, String phoneNumber, String email) {
		this.Name = name;
		this.Price = price;
		this.Location = location;
		this.Date_Posted = date_Posted;
		this.Property_Type = property_Type;
		this.Number_Beds = number_Beds;
		this.Seller_Type = seller_Type;
		this.Description = description;
		this.PhoneNumber = phoneNumber;
		this.Email  = email;
	}
	
	
}
