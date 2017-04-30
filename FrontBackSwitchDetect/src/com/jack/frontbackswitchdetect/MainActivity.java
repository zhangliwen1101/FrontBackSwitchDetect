package com.jack.frontbackswitchdetect;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{
	public static final String TAG = "viclee";
    private boolean isCurrentRunningForeground = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (!isCurrentRunningForeground) {
//            Log.v(TAG, ">>>>>>>>>>>>>>>>>>>切到前台 activity process");
//        }
        
///////////////////////////////////////////////
        if(!Utils.isActive)
        {
            Utils.isActive = true;
            /*一些处理，如弹出密码输入界面*/
            Log.i(TAG, "应用再前台展示");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        isCurrentRunningForeground = isRunningForeground();
//        if (!isCurrentRunningForeground) {
//            Log.v(TAG, ">>>>>>>>>>>>>>>>>>>切到后台 activity process");
//        }
/////////////////////////////////////////////////
        if(!Utils.isForeground(this))
        {
            Utils.isActive = false;
            Log.i(TAG, "应用再后台展示");
        }
    }
    
    public boolean isRunningForeground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(this.getApplicationInfo().processName)) {
                    Log.d(TAG, "MainActivity isRunningForeGround");
                    return true;
                }
            }
        }
        Log.d(TAG, "MainActivity isRunningBackGround");
        return false;
    }
    
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn) {
			Toast.makeText(MainActivity.this, "哈哈，啥也没有兄弟", Toast.LENGTH_LONG).show();
//            startActivity(new Intent(this,Main2Activity.class));
        }
	}
}
