package com.rk.gumtree.android.util;




import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import static com.rk.gumtree.android.util.LogUtils.*;

public class Utils {
	
	
	public enum AppStart {
	    FIRST_TIME, FIRST_TIME_VERSION, NORMAL;
	}

	/**
	 * The app version code (not the version name!) that was used on the last
	 * start of the app.
	 */
	private static final String LAST_APP_VERSION = "last_app_version";
	
	public static final String PREFS_NAME = "MyPrefsFile";

	/**
	 * Caches the result of {@link #checkAppStart()}. To allow idempotent method
	 * calls.
	 */
	private static AppStart appStart = null;
	
	private static final String TAG = makeLogTag(Utils.class);
	

	/**
	 * Finds out started for the first time (ever or in the current version).
	 * 
	 * @return the type of app start
	 */
	public static AppStart checkAppStart(Context context) {
	        PackageInfo pInfo;
	        try {
	            pInfo = context.getPackageManager().getPackageInfo(
	                    context.getPackageName(), 0);
	            SharedPreferences  sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
	            int lastVersionCode = sharedPreferences.getInt(
	                    LAST_APP_VERSION, -1);
	            // String versionName = pInfo.versionName;
	            int currentVersionCode = pInfo.versionCode;
	            appStart = checkAppStart(currentVersionCode, lastVersionCode);
	            // Update version in preferences
	            sharedPreferences.edit()
	                    .putInt(LAST_APP_VERSION, currentVersionCode).commit();
	        } catch (NameNotFoundException e) {
	        	LOGW(TAG,
	                    "Unable to determine current app version from pacakge manager. Defenisvely assuming normal app start.");
	        }
	    return appStart;
	}

	public static AppStart checkAppStart(int currentVersionCode, int lastVersionCode) {
	    if (lastVersionCode == -1) {
	        return AppStart.FIRST_TIME;
	    } else if (lastVersionCode < currentVersionCode) {
	        return AppStart.FIRST_TIME_VERSION;
	    } else if (lastVersionCode > currentVersionCode) {
	        LOGW(TAG, "Current version code (" + currentVersionCode
	                + ") is less then the one recognized on last startup ("
	                + lastVersionCode
	                + "). Defenisvely assuming normal app start.");
	        return AppStart.NORMAL;
	    } else {
	        return AppStart.NORMAL;
	    }
	}

}
