package com.rk.gumtree.android;


import java.io.IOException;

import com.rk.gumtree.android.ui.fragment.ItemDetailFragment;
import com.rk.gumtree.android.ui.fragment.ItemListFragment;
import com.rk.gumtree.android.util.Utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import static com.rk.gumtree.android.util.Utils.AppStart;
import static com.rk.gumtree.android.util.LogUtils.*;
import static com.rk.gumtree.android.app.GumtreeApp.*;

import static com.rk.gumtree.android.provider.GumTreeProvider.*;

public class MainActivity extends ActionBarActivity implements 
					ItemListFragment.OnPropItemSelectedListener{
	
	private ShareActionProvider mShareActionProvider;
	ItemDetailFragment mItemDetailFragment;
	ItemListFragment mItemListFragment;
	AppStart appStart = AppStart.FIRST_TIME;
	private static final String TAG = makeLogTag(MainActivity.class);
	boolean mIsDualPane = false;
	int mItemIndex = 0;
	InsertDBTask mDBTask; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		try {
			mImages = getAssets().list(image_path);
		} catch (IOException e) {
			LOGE(TAG, "Unable to load images from assets folder", e);
		}
		appStart = Utils.checkAppStart(getApplicationContext());
		switch(appStart){
		case FIRST_TIME:
			LOGD(TAG, "First Time launch");
	/**
	 *  As we have very few records to insert in DB, the insertion can be done without any background thread also,
	 *  but as recommended by google all the IO operations need to be done on background thread, used asynctask here.
	 *	will get executed only once the application is launched for the first time.
	 *
	 *	List of Ads is obtained from Json file stored in raw folder of resources. data parsed using GSON
	 */
			mDBTask = new InsertDBTask();
			mDBTask.execute();
			break;
		case FIRST_TIME_VERSION:
			LOGD(TAG, "Version launch");
			break;
		case NORMAL:
			LOGD(TAG, "Normal launch");
			break;
		default:
			break;
		}
		mItemListFragment = (ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.item_list);
		mItemDetailFragment = (ItemDetailFragment) getSupportFragmentManager().findFragmentById(R.id.item_detail);
		
		View DetailView = findViewById(R.id.item_detail);
		mIsDualPane = DetailView != null && DetailView.getVisibility() == View.VISIBLE;
		mItemListFragment.setOnPropItemSelectedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
	if(mIsDualPane){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

        /** Getting the actionprovider associated with the menu item whose id is share */
        MenuItem item = (MenuItem)menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        /** Setting a share intent */
        mShareActionProvider.setShareIntent(getDefaultShareIntent());
		}
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(mDBTask != null){
			mDBTask.cancel(true);
		}
	}
	
	  /** Returns a share intent */
    private Intent getDefaultShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT,"Extra Text");
        return intent;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onPropItemSelected(int index) {
		mItemIndex = index;
		if(mIsDualPane){
			mItemDetailFragment.reloadView();
		}else{
			// use separate Activity
			Intent intent = new Intent(this, DetailsActivity.class);
			startActivity(intent);
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("ItemIndex", mItemIndex);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		restoreSelection(savedInstanceState);
	}
	
	void restoreSelection(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
        	 if (mIsDualPane) {
                 int artIndex = savedInstanceState.getInt("ItemIndex", 0);
                 mItemListFragment.setSelection(artIndex);
                 onPropItemSelected(artIndex);
             }
        }
	} 
	
	private class InsertDBTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
				insertRecords(MainActivity.this);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
		}
	}
}
