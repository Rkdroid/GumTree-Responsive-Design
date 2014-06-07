package com.rk.gumtree.android.provider;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.tjeannin.provigen.InvalidContractException;
import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.ProviGenProvider;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.ContentUri;
import com.tjeannin.provigen.annotation.Contract;
import com.tjeannin.provigen.annotation.Column.Type;

public class GumTreeProvider extends ProviGenProvider{
	
	
	public GumTreeProvider() throws InvalidContractException {
		super(PropertyContract.class);
	}

	@Override
	public void onCreateDatabase(SQLiteDatabase database) {
		super.onCreateDatabase(database);
	}
	
	@Contract(version=1)
	public static interface PropertyContract extends ProviGenBaseContract{
		
		@Column(Type.TEXT)
		public static final String COLUMN_NAME = "Property_Name";
		
		@Column(Type.TEXT)
		public static final String COLUMN_PRICE = "Property_Price";
		
		@Column(Type.TEXT)
		public static final String COLUMN_LOCATION = "Property_Location";
		
		@Column(Type.TEXT)
		public static final String COLUMN_DATE_POSTED = "Property_Date_Posted";
		
		@Column(Type.TEXT)
		public static final String COLUMN_TYPE = "Property_Type";
		
		@Column(Type.TEXT)
		public static final String COLUMN_NUMBER_BEDS = "Property_Number_Beds";
		
		@Column(Type.TEXT)
		public static final String COLUMN_SELLER_TYPE = "Property_Seller_Type";
		
		@Column(Type.TEXT)
		public static final String COLUMN_DESCRIPTION = "Property_Description";
		
		@Column(Type.TEXT)
		public static final String COLUMN_PHNUMBER = "Property_PhNumber";
		
		@Column(Type.TEXT)
		public static final String COLUMN_EMAIL = "Property_Email";
		
		@ContentUri
		public static final Uri CONTENT_URI = Uri.parse("content://com.rk.gumtree.android.provider/GumtreeProperty");
		
	}
	
	public static void insertRecords(final Context context, final int rcnt){
			final Resources res = context.getResources();
			int resId1 = res.getIdentifier("property_villa_name" + rcnt, "string", context.getPackageName());
			int resId2 = res.getIdentifier("property_villa_price" + rcnt, "string", context.getPackageName());
			int resId3 = res.getIdentifier("property_villa_location" + rcnt, "string", context.getPackageName());
			int resId4 = res.getIdentifier("Property_villa_Date_Posted" + rcnt, "string", context.getPackageName());
			int resId5 = res.getIdentifier("property_villa_Description" + rcnt, "string", context.getPackageName());
			int resId6 = res.getIdentifier("property_villa_phone" + rcnt, "string", context.getPackageName());
			int resId7 = res.getIdentifier("property_villa_email" + rcnt, "string", context.getPackageName());
			int resId8 = res.getIdentifier("property_villa_beds" + rcnt, "string", context.getPackageName());
			
			ContentValues values = new ContentValues();
			values.put(PropertyContract.COLUMN_NAME, context.getString(resId1));
			values.put(PropertyContract.COLUMN_PRICE, context.getString(resId2));
			values.put(PropertyContract.COLUMN_LOCATION, context.getString(resId3));
			values.put(PropertyContract.COLUMN_DATE_POSTED, context.getString(resId4));
			values.put(PropertyContract.COLUMN_DESCRIPTION, context.getString(resId5));
			values.put(PropertyContract.COLUMN_PHNUMBER, context.getString(resId6));
			values.put(PropertyContract.COLUMN_EMAIL, context.getString(resId7));
			values.put(PropertyContract.COLUMN_NUMBER_BEDS, context.getString(resId8));
			
			context.getContentResolver().insert(PropertyContract.CONTENT_URI, values);
}
	
	

}
