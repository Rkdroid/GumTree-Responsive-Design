package com.rk.gumtree.android.provider;

import static com.rk.gumtree.android.util.LogUtils.makeLogTag;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.rk.gumtree.android.R;
import com.rk.gumtree.android.model.PropertyDAO;
import com.tjeannin.provigen.InvalidContractException;
import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.ProviGenProvider;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.ContentUri;
import com.tjeannin.provigen.annotation.Contract;
import com.tjeannin.provigen.annotation.Column.Type;

import static com.rk.gumtree.android.util.LogUtils.*;

public class GumTreeProvider extends ProviGenProvider{
	
	private static final String TAG = makeLogTag(GumTreeProvider.class);
	
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
	
	/**
	 * Reads the Json File and returns the reader
	 * @param context
	 * @return
	 * @throws FileNotFoundException
	 */
	private static JsonReader getJsonReader (final Context context) throws FileNotFoundException{
		JsonReader reader = null;
		final Resources res = context.getResources();
		reader = new JsonReader(
		new InputStreamReader(res.openRawResource(R.raw.ads)));
		return reader;
	}
			
	
	public static void insertRecords(final Context context){
			JsonReader reader = null;
		try {
			reader = getJsonReader(context);
			Gson adGson = new Gson();
			JsonParser jsonParser = new JsonParser();
			JsonArray propArray =  jsonParser.parse(reader).getAsJsonArray();
			for ( JsonElement aUser : propArray ){
				PropertyDAO mPropertyAd = adGson.fromJson(aUser, PropertyDAO.class);
				if(mPropertyAd!=null){
					ContentValues values = new ContentValues();
					values.put(PropertyContract.COLUMN_NAME, mPropertyAd.name);
					values.put(PropertyContract.COLUMN_PRICE, mPropertyAd.price);
					values.put(PropertyContract.COLUMN_LOCATION, mPropertyAd.location);
					values.put(PropertyContract.COLUMN_DATE_POSTED, mPropertyAd.date_Posted);
					values.put(PropertyContract.COLUMN_DESCRIPTION, mPropertyAd.description);
					values.put(PropertyContract.COLUMN_PHNUMBER, mPropertyAd.phoneNumber);
					values.put(PropertyContract.COLUMN_EMAIL, mPropertyAd.email);
					values.put(PropertyContract.COLUMN_NUMBER_BEDS, mPropertyAd.number_Beds);
					
					context.getContentResolver().insert(PropertyContract.CONTENT_URI, values);
				}
			}
		   } catch (FileNotFoundException e) {
				LOGE(TAG, "File not found in raw folder", e);
		   	}catch (Exception e) {
		   		LOGE(TAG, "json input malfunctioned", e);
			}
}
	
	

}
