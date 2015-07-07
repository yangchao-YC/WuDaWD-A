package com.whuss.oralanswers;

import cn.jpush.android.api.JPushInterface;
import android.app.Application;
/**
 * ÍÆËÍ¹¦ÄÜapplication
 * @author guan
 *
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
	}

}
