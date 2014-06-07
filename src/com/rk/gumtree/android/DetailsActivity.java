package com.rk.gumtree.android;

import com.rk.gumtree.android.ui.fragment.ItemDetailFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

public class DetailsActivity extends ActionBarActivity{
	
	private ItemDetailFragment detailFrag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_item_detail);
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

}
