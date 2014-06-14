package com.rk.gumtree.android;

import com.rk.gumtree.android.ui.fragment.ItemDetailFragment;
import com.rk.gumtree.android.ui.share.ShareView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class DetailsActivity extends ActionBarActivity{
	
	private ItemDetailFragment detailFrag;
	ActionBar mActionBar;
	private View mActionBarView;
	private Intent[] mSharedIntents;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_item_detail);
		mActionBar = getSupportActionBar();
		mActionBar.setCustomView(R.layout.share_action_layout);
		mActionBarView = mActionBar.getCustomView();
		mActionBar.setDisplayShowCustomEnabled(true);
		
		// Setup the intent to share
		mSharedIntents = new Intent[] { getEmailIntent(), getTxtIntent(), getImageIntent() };
				
		ShareView shareView = (ShareView) mActionBarView.findViewById(R.id.share_view);
		shareView.setShareIntent(mSharedIntents);
		
		 // If we are in two-pane layout mode, this activity is no longer necessary
        if (getResources().getBoolean(R.bool.has_two_panes)) {
            finish();
            return;
        }
        
        FragmentManager fm = getSupportFragmentManager();
        detailFrag = (ItemDetailFragment) fm.findFragmentById(R.id.item_detail);
        
        if(detailFrag == null){
        	detailFrag = new ItemDetailFragment();
        	fm.beginTransaction().add(R.id.item_detail, detailFrag).commit();
        }
	}

	
	private Intent getEmailIntent() {
		String to = "foo@bar.com";
		String subject = "yo dude";
		String body = "Here's an email body";

		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		String[] toArr = new String[] { to };
		intent.putExtra(Intent.EXTRA_EMAIL, toArr);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, body);
		return intent;
	}

	private Intent getTxtIntent() {
		String subject = "share subject";
		String text = "here's some share text";

		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		return intent;
	}

	private Intent getImageIntent() {
		Uri imageUri = getRandomImageUri();

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
		shareIntent.setType("image/jpeg");
		return shareIntent;
	}

	// Get the uri to a random image in the photo gallery
	private Uri getRandomImageUri() {
		Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { MediaStore.Images.Media._ID };
		Cursor cursor = null;
		try {
			cursor = getContentResolver().query(mediaUri, projection, null, null, null);
			cursor.moveToPosition((int) (Math.random() * cursor.getCount()));
			String id = cursor.getString(0);
			Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
			return uri;
		}
		catch (Exception e) {
			return null;
		}
		finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
	}
	

}
