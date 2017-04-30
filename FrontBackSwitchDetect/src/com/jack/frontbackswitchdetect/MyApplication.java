package com.jack.frontbackswitchdetect;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.os.Bundle;

public class MyApplication extends Application {
	 public int count = 0;
	    @Override
	    public void onCreate() {
	        super.onCreate();

	        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

	            @Override
	            public void onActivityStopped(Activity activity) {
	                Log.v("viclee", activity + "onActivityStopped");
	                count--;
	                if (count == 0) {
	                    Log.v("viclee", ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
	                }
	            }

	            @Override
	            public void onActivityStarted(Activity activity) {
	                Log.v("viclee", activity + "onActivityStarted");
	                if (count == 0) {
	                    Log.v("viclee", ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
	                }
	                count++;
	            }

	            @Override
	            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
	                Log.v("viclee", activity + "onActivitySaveInstanceState");
	            }

	            @Override
	            public void onActivityResumed(Activity activity) {
	                Log.v("viclee", activity + "onActivityResumed");
	            }

	            @Override
	            public void onActivityPaused(Activity activity) {
	                Log.v("viclee", activity + "onActivityPaused");
	            }

	            @Override
	            public void onActivityDestroyed(Activity activity) {
	                Log.v("viclee", activity + "onActivityDestroyed");
	            }

	            @Override
	            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
	                Log.v("viclee", activity + "onActivityCreated");
	            }
	        });
	    }
}	
