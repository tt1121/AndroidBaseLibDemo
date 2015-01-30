package com.example.baselibdemo.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.baselibdemo.DemoCommonUtils;
import com.example.baselibdemo.R;
import com.imove.base.utils.Log;
import com.imove.base.utils.ThreadPoolManagerNormal;

public class SharePreferenceDemo extends Activity {
	
	private final String TAG = SharePreferenceDemo.class.getSimpleName();
	
	private DemoSharePreference mSharePreference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		ViewGroup rootViewGroup = (ViewGroup)findViewById(R.id.root_view);
		DemoCommonUtils.addButton(this, rootViewGroup, "setValue", setValueRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "getValue", getValueRunnable);
		mSharePreference = new DemoSharePreference();
		ThreadPoolManagerNormal.init(getApplicationContext());
	}

	Runnable setValueRunnable = new Runnable() {
		@Override
		public void run() {
			ThreadPoolManagerNormal.execute(new Runnable() {
				@Override
				public void run() {
					long value = System.currentTimeMillis();
					boolean isSuc = mSharePreference.setValue(getApplicationContext(), DemoSharePreference.TYPE_UPDATE_TIME, value);
					Log.v(TAG, "setValue value:" + value + " - isSuc:" + isSuc);
				}
			});
		}
	};
	
	Runnable getValueRunnable = new Runnable() {
		@Override
		public void run() {
			ThreadPoolManagerNormal.execute(new Runnable() {
				@Override
				public void run() {
					long t = mSharePreference.getLong(getApplicationContext(), DemoSharePreference.TYPE_UPDATE_TIME);
					Log.v(TAG, "getValue value:" + t);
				}
			});
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
