package com.example.baselibdemo.db;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.baselibdemo.DemoCommonUtils;
import com.example.baselibdemo.R;
import com.imove.base.utils.Log;
import com.imove.base.utils.SdcardUtil;
import com.imove.base.utils.ThreadPoolManagerQuick;
import com.imove.base.utils.executor.ThreadPoolManager;

public class DbManagerDemo extends Activity {

	private static final String TAG = "debug";		
	
	private DownloadTaskDao mDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		ViewGroup rootViewGroup = (ViewGroup)findViewById(R.id.root_view);
		DemoCommonUtils.addButton(this, rootViewGroup, "增加", addRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "删除", deleteRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "修改", alterRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "查找", queryRunnable);
		mDao = new DownloadTaskDao(getApplicationContext());
		ThreadPoolManager.initThreadPoolManager(this);
	}

	private int mFlagIndex = 1;
	private String mLastDownloadId;
	
	Runnable addRunnable = new Runnable() {
		@Override
		public void run() {
			ThreadPoolManagerQuick.execute(new Runnable() {
				public void run() {
					DownloadBean downloadBean = new DownloadBean();
					downloadBean.downloadId = downloadBean.hashCode() + "_test";
					downloadBean.downloadLen = 10000 + mFlagIndex;
					downloadBean.fileName = "NetEaseNews_348.apk_" + mFlagIndex;
					downloadBean.url = "http://gdown.baidu.com/data/wisegame/a8e777ec7edf5f51/NetEaseNews_348.apk";;
					downloadBean.path = SdcardUtil.getSDPath() + "/baselibDemo/";
					long id = mDao.add(downloadBean);
					if (id > 0) {
						mLastDownloadId = downloadBean.downloadId;
					}
					Log.v(TAG, "add:" + id);
				}
			});
		}
	};
	
	Runnable deleteRunnable = new Runnable() {
		@Override
		public void run() {
			if(mLastDownloadId == null) {
				Log.v(TAG, "delete, mLastDownloadId is null");
				return;
			}
			ThreadPoolManagerQuick.execute(new Runnable() {
				public void run() {
					boolean isSuc = mDao.delete(mLastDownloadId);
					Log.v(TAG, "delete:" + isSuc);
					mLastDownloadId = null;
				}
			});
		}
	};
	
	Runnable alterRunnable = new Runnable() {
		@Override
		public void run() {
			if(mLastDownloadId == null) {
				Log.v(TAG, "alter, mLastDownloadId is null");
				return;
			}
			ThreadPoolManagerQuick.execute(new Runnable() {
				public void run() {
					DownloadBean downloadBean = mDao.query(mLastDownloadId);
					if (downloadBean == null) {
						return;
					}
					downloadBean.fileName = downloadBean.hashCode() + "_alter_" + System.currentTimeMillis();
					boolean isSuc = mDao.alter(downloadBean);
					Log.v(TAG, "alter:" + isSuc);
				}
			});
		}
	};
	
	Runnable queryRunnable = new Runnable() {
		@Override
		public void run() {
			if(mLastDownloadId == null) {
				Log.v(TAG, "query, mLastDownloadId is null");
				return;
			}
			ThreadPoolManagerQuick.execute(new Runnable() {
				public void run() {
					DownloadBean downloadBean = mDao.query(mLastDownloadId);
					Log.v(TAG, "query:" + downloadBean);
				}
			});
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
