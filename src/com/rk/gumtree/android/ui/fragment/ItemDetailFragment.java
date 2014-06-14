package com.rk.gumtree.android.ui.fragment;



import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.rk.gumtree.android.R;
import com.rk.gumtree.android.app.GumtreeApp;
import com.rk.gumtree.android.model.PropertyDAO;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import static com.rk.gumtree.android.util.LogUtils.*;
import static com.rk.gumtree.android.app.GumtreeApp.*;

public class ItemDetailFragment extends Fragment{
	View mView;
	private ViewPager mViewpager;
	private static final String TAG = makeLogTag(ItemDetailFragment.class);
	private ImageLoader imageLoader = ImageLoader.getInstance();
	GoogleMap mGoogleMap;
	private PropertyDAO detailPropertyDAO;
	
	public ItemDetailFragment(){
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		mView = inflater.inflate(R.layout.item_detail_view, container, false);
		mViewpager = (ViewPager) mView.findViewById(R.id.vpItempager);
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		SupportMapFragment fragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map1);
		mGoogleMap = fragment.getMap();
		reloadView();
		showLocation();
		return mView;
	}
	
	private void showLocation(){
		if(mGoogleMap!=null){
			MarkerOptions markerOptions = null;
			LatLng position = null;
			mGoogleMap.clear();
			
			markerOptions = new MarkerOptions();
			position = new LatLng(51.511338, -0.094732);
			markerOptions.position(position);
			markerOptions.title("London, UK");
			mGoogleMap.addMarker(markerOptions);
			
			if(position!=null){
				mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12));
				CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(position);
				mGoogleMap.animateCamera(cameraPosition);			
			}
		}
}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		PagerAdapter mAdapter = new ImagePagerAdapter(getActivity());
		mViewpager.setAdapter(mAdapter);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.action_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_call:
			if(detailPropertyDAO!=null){
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + detailPropertyDAO.phoneNumber));
				startActivity(intent);
			}
			break;
		case R.id.menu_sms:
			if(detailPropertyDAO!=null){
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", "I am intersted in the property"); 
				sendIntent.putExtra("address", detailPropertyDAO.phoneNumber);
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivity(sendIntent);
			}
			break;
		case R.id.menu_email:
			if(detailPropertyDAO!=null){
				String to = detailPropertyDAO.email;
	            String subject = "Propert Ad in Gumtree";
	            String message = "I am intersted in the property";
	 
	            Intent email = new Intent(Intent.ACTION_SEND);
	            email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
	            email.putExtra(Intent.EXTRA_SUBJECT, subject);
	            email.putExtra(Intent.EXTRA_TEXT, message);
	 
	            // need this to prompts email client only
	            email.setType("message/rfc822");
	            startActivity(Intent.createChooser(email, "Choose an Email client"));
			}
            break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private class ImagePagerAdapter extends PagerAdapter{
		private final Context mContext;
		DisplayImageOptions options;
		public ImagePagerAdapter(Context context){
			this.mContext = context;
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
		public int getCount() {
			
			return mImages.length;
		}
		
		@Override
		public Object instantiateItem(ViewGroup collection, int position) {
			final ImageView iv = new ImageView(mContext);
			iv.setScaleType(ScaleType.CENTER_CROP);
			final String imageUri = "assets://" + image_path + "/" + mImages[position];
			imageLoader.displayImage(imageUri, iv, options, null);
			iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			collection.addView(iv, 0);
			return iv;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			
			return view.equals(object);
		}
		
		@Override
		public void destroyItem(ViewGroup collection, int position, Object view) {
			collection.removeView((View) view);
		}
	}
	
	public void reloadView(){
		GumtreeApp gPropertyState = (GumtreeApp) getActivity().getApplicationContext();
		detailPropertyDAO = gPropertyState.getAppPropertyDAO();
		if(detailPropertyDAO!=null){
			TextView genericText = (TextView) mView.findViewById(R.id.detail_text_price_value);
			genericText.setText(detailPropertyDAO.price);
			genericText = (TextView) mView.findViewById(R.id.name_detail_text_view);
			genericText.setText(detailPropertyDAO.name);
			genericText = (TextView) mView.findViewById(R.id.detail_text_date_value);
			genericText.setText(detailPropertyDAO.date_Posted);
			genericText = (TextView) mView.findViewById(R.id.name_detail_text_desc_value);
			genericText.setText(detailPropertyDAO.description);
			genericText = (TextView) mView.findViewById(R.id.detail_text_numbed_value);
			genericText.setText(detailPropertyDAO.number_Beds);
			genericText = (TextView) mView.findViewById(R.id.detail_text_loc_value);
			genericText.setText(detailPropertyDAO.location);
		}
	}

}
