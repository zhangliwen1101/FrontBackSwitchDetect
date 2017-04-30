# FrontBackSwitchDetect
多种方法监听应用前后台切换

>加权限：

```
<!-- 允许程序获取当前或最近运行的应用 -->
<uses-permission android:name="android.permission.GET_TASKS" />
```
  

###第一种方法：
>Android在API 14之后，在Application类中，提供了一个应用生命周期回调的注册方法，用来对应用的生命周期进行集中管理，这个接口叫registerActivityLifecycleCallbacks，可以通过它注册自己的ActivityLifeCycleCallback，每一个Activity的生命周期都会回调到这里的对应方法。

```
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
```


###第二种：
>当应用切到后台的时候，运行在前台的进程由我们的app变成了桌面app，依据这一点，我们可以实现检测应用前后台切换的功能。在Activity的onStop生命周期中执行检测代码，如果发现当前运行在前台的进程不是我们自己的进程，说明应用切到了后台。
?       想想为什么要在onStop中检测，而不是onPause？这是由于A启动B时，生命周期的执行顺序如下：A.onPause->B.onCreate->B.onStart->B.onResume->A.onStop，也就是说，在A的onPause方法中，B的生命周期还没有执行，进程没有进入前台，当然是检测不到的。我们把代码移到onPause生命周期中，发现确实没有效果。

```
 @Override
    protected void onStart() {
        super.onStart();
        if (!isCurrentRunningForeground) {
            Log.v(TAG, ">>>>>>>>>>>>>>>>>>>切到前台 activity process");
        }
        
    }
    @Override
    protected void onStop() {
        super.onStop();
        isCurrentRunningForeground = isRunningForeground();
        if (!isCurrentRunningForeground) {
            Log.v(TAG, ">>>>>>>>>>>>>>>>>>>切到后台 activity process");
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
```
   

###第三种：
>由于是监听整个应用的前后台切换，所以上述重写可以实现在一个继承了Activity的父类BaseActivity里，直接继承Activity的类改为继承BaseActivity。如果有部分Activity是继承了FragmentActivity等而非直接继承Activity，同样建议新建一个父类，如BaseFragmentActivity等，在其中重写onResume，onStop，然后被继承。

```
public class Utils {
     public static boolean isActive = false;
      /*判断应用是否在前台*/
    public static boolean isForeground(Context context)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}



    @Override
    protected void onStart() {
        super.onStart();
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
        if(!Utils.isForeground(this))
        {
            Utils.isActive = false;
            Log.i(TAG, "应用再后台展示");
        }
    }
```

