package com.rk.gumtree.android.ui.fragment;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.rk.gumtree.android.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rk.gumtree.android.model.PropertyDAO;
import com.rk.gumtree.android.provider.GumTreeProvider.PropertyContract;

import static com.rk.gumtree.android.app.GumtreeApp.*;
import static com.rk.gumtree.android.app.GumtreeApp.image_path;
import static com.rk.gumtree.android.app.GumtreeApp.mImages;
import static com.rk.gumtree.android.util.LogUtils.*;

public class ItemListFragment extends ListFragment implements 
			AdapterView.OnItemClickListener,LoaderCallbacks<Cursor>{
	
	private static final String TAG = makeLogTag(ItemListFragment.class);
	
	private GumAdapter mAdapter;
	
	private boolean mIsTwoPaneLayout;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	OnPropItemSelectedListener mPropItemSelectedListener = null;
	
	View ColoredView;
	
	
	public interface OnPropItemSelectedListener{
		/**
		 * Called when a given item is selected
		 * @param index
		 */
		public void onPropItemSelected(int index);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mIsTwoPaneLayout = getResources().getBoolean(R.bool.has_two_panes);
		mAdapter = new GumAdapter(getActivity(), null);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView =  inflater.inflate(R.layout.list_item_fragment, container, false);
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Set up ListView, assign adapter and set some listeners. The adapter was previously
        // created in onCreate().
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(this);
        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause image loader to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    
                } else {
                    
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });

        if (mIsTwoPaneLayout) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }

        
            // Initialize the loader, and create a loader identified by ContactsQuery.QUERY_ID
            getLoaderManager().initLoader(PropertyQuery.QUERY_ID, null, this);
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		if(id == PropertyQuery.QUERY_ID){
			
			return new CursorLoader(getActivity(),
					PropertyContract.CONTENT_URI,
					PropertyQuery.PROJECTION,
					null,
					null,
					null);
		}
		LOGE(TAG, "onCreateLoader - incorrect ID provided (" + id + ")");
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// This swaps the new cursor into the adapter.
        if (loader.getId() == PropertyQuery.QUERY_ID) {
            mAdapter.swapCursor(data);
        }
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (loader.getId() == PropertyQuery.QUERY_ID) {
			// When the loader is being reset, clear the cursor from the adapter. This allows the
            // cursor resources to be freed.
            mAdapter.swapCursor(null);
        }
	}
	
	public void setOnPropItemSelectedListener(OnPropItemSelectedListener listener){
		mPropItemSelectedListener = listener;
	}
	
	private final class GumAdapter extends CursorAdapter{
		private LayoutInflater mInflater;
		DisplayImageOptions options;
		public GumAdapter(Context context, Cursor c) {
			super(context, c);
			mInflater = LayoutInflater.from(context);
			
			options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_stub)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.displayer(new SimpleBitmapDisplayer())
			.build();
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final ViewHolder holder  = (ViewHolder) view.getTag();
			final String imageUri = "assets://" + image_path + "/" + mImages[cursor.getPosition()];
			imageLoader.displayImage(imageUri, holder.mImageView, options, null);
			holder.mName.setText(cursor.getString(PropertyQuery.PROP_NAME));
			holder.mDate.setText(cursor.getString(PropertyQuery.PROP_DATE));
			holder.mPrice.setText(cursor.getString(PropertyQuery.PROP_PRICE));
			holder.mAddress.setText(cursor.getString(PropertyQuery.PROP_ADDRESS));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
			final View itemLayout = mInflater.inflate(R.layout.row_propertyad_list, viewGroup, false);
			final ViewHolder holder = new ViewHolder();
			holder.mImageView = (ImageView) itemLayout.findViewById(R.id.thumbnail_image_view);
			holder.mName = (TextView) itemLayout.findViewById(R.id.name_text_view);
			holder.mDate = (TextView) itemLayout.findViewById(R.id.date_text_view);
			holder.mPrice = (TextView) itemLayout.findViewById(R.id.price_text_view);
			holder.mAddress = (TextView) itemLayout.findViewById(R.id.proximity_text_view);
			
			itemLayout.setTag(holder);
			return itemLayout;
		}
		
		private class ViewHolder{
			ImageView mImageView;
			TextView mName;
			TextView mDate;
			TextView mPrice;
			TextView mAddress;
		}
		
	}

	public interface PropertyQuery{
		final static int QUERY_ID = 1;
		 // The projection for the CursorLoader query. This is a list of columns that the Contacts
        // Provider should return in the Cursor.
		final static String[] PROJECTION = {
			
			PropertyContract._ID,
			
			PropertyContract.COLUMN_NAME,
			
			PropertyContract.COLUMN_PRICE,
			
			PropertyContract.COLUMN_LOCATION,
			
			PropertyContract.COLUMN_DATE_POSTED,
			
			PropertyContract.COLUMN_TYPE,
			
			PropertyContract.COLUMN_NUMBER_BEDS,
			
			PropertyContract.COLUMN_SELLER_TYPE,
			
			PropertyContract.COLUMN_DESCRIPTION,
			
			PropertyContract.COLUMN_PHNUMBER,
			
			PropertyContract.COLUMN_EMAIL
		};
		
	       // The query column numbers which map to each value in the projection
        final static int ID = 0;
        final static int PROP_NAME = 1;
        final static int PROP_PRICE = 2;
        final static int PROP_ADDRESS = 3;
        final static int PROP_DATE = 4;
        final static int PROP_TYPE = 5;
        final static int PROP_NUMBER_BEDS = 6;
        final static int PROP_SELLER_TYPE = 7;
        final static int PROP_DESCRIPTION = 8;
        final static int PROP_PHONE_NUMBER = 9;
        final static int PROP_EMAIL = 10;
        
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	
		final Cursor cursor = mAdapter.getCursor();
		cursor.moveToPosition(position);
		setAppPropertyDAO(new PropertyDAO(cursor.getString(PropertyQuery.PROP_NAME), 
				cursor.getString(PropertyQuery.PROP_PRICE), cursor.getString(PropertyQuery.PROP_ADDRESS), 
				cursor.getString(PropertyQuery.PROP_DATE), cursor.getString(PropertyQuery.PROP_TYPE), 
				cursor.getString(PropertyQuery.PROP_NUMBER_BEDS), cursor.getString(PropertyQuery.PROP_SELLER_TYPE),
				cursor.getString(PropertyQuery.PROP_DESCRIPTION), cursor.getString(PropertyQuery.PROP_PHONE_NUMBER),
						cursor.getString(PropertyQuery.PROP_EMAIL)));
		if(mPropItemSelectedListener != null){
			mPropItemSelectedListener.onPropItemSelected(position);
		}
		 if (getResources().getBoolean(R.bool.has_two_panes)) {
			 if (ColoredView != null)
	             ColoredView.setBackgroundColor(Color.WHITE); //original color
	
		         view.setBackgroundColor(Color.LTGRAY); //selected color
		         ColoredView = view;
		 }
	}
	
}
