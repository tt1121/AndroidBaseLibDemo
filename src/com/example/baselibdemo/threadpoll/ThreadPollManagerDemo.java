package com.example.baselibdemo.threadpoll;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.baselibdemo.DemoCommonUtils;
import com.example.baselibdemo.R;
import com.imove.base.utils.FileUtil;
import com.imove.base.utils.Log;
import com.imove.base.utils.SdcardUtil;
import com.imove.base.utils.ThreadPoolManagerNormal;
import com.imove.base.utils.executor.ThreadPoolManager;

public class ThreadPollManagerDemo extends Activity {
	
	private final String TAG = ThreadPollManagerDemo.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		ViewGroup rootViewGroup = (ViewGroup)findViewById(R.id.root_view);
		DemoCommonUtils.addButton(this, rootViewGroup, "初始化", initRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "长时间请求", longTimeRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "短耗时请求 ", shortTimeRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "Thread ", threadRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "Async ", asyncRunnable);
	}

	Runnable initRunnable = new Runnable() {
		@Override
		public void run() {
			ThreadPoolManager.initThreadPoolManager(getApplicationContext());
			Log.v(TAG, "初始化成功");
		}
	};
	
	Runnable longTimeRunnable = new Runnable() {
		@Override
		public void run() {
			ThreadPoolManagerNormal.execute(new Runnable() {
				@Override
				public void run() {
					try {
						java.lang.Thread.sleep(3 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Log.v(TAG, "longTime run end");
				}
			});
		}
	};
	
	Runnable shortTimeRunnable = new Runnable() {
		@Override
		public void run() {
			ThreadPoolManagerNormal.execute(new Runnable() {
				@Override
				public void run() {
					String filePath = SdcardUtil.getSDPath() + "/baselib/a.txt"; 
					FileUtil.saveData("test", filePath, false);
					Log.v(TAG, " shortTime run end");
				}
			});
		}
	};
	
	Runnable threadRunnable = new Runnable() {
		@Override
		public void run() {
			new com.imove.base.utils.Thread() {
				@Override
				public void run() {
					try {
						java.lang.Thread.sleep(3 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Log.v(TAG, "thread run end");
				}
			}.start();
		}
	};
	
	Runnable asyncRunnable = new Runnable() {
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			new com.imove.base.utils.AsyncTask() {
				@Override
				protected Object doInBackground(Object... params) {
					try {
						java.lang.Thread.sleep(3 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Log.v(TAG, "AsyncTask execute end");
					return "";
				}
			}.execute();
		}
	};
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
