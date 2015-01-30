/*
 * 版    权： 深圳市爱猫新媒体数据科技有限公司
 * 创建人: 李理
 * 创建时间: 2014年8月15日
 */
package com.example.baselibdemo;

import android.app.Application;

import com.imove.base.utils.executor.ThreadPoolManager;

/**
 * @author 李理 
 */
public class MainApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ThreadPoolManager.initThreadPoolManager(this);
	}

}
