package com.rk.gumtree.android.app;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.rk.gumtree.android.model.PropertyDAO;

import android.app.Application;
import android.content.Context;

public class GumtreeApp extends Application{
	
	public static String[] mImages;
	public static final String image_path = "classified_Images";
	public static PropertyDAO appPropertyDAO;
	
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
	}
	
	
    public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}


	public static PropertyDAO getAppPropertyDAO() {
		return appPropertyDAO;
	}


	public static void setAppPropertyDAO(PropertyDAO appPropertyDAO) {
		GumtreeApp.appPropertyDAO = appPropertyDAO;
	}
    

}
