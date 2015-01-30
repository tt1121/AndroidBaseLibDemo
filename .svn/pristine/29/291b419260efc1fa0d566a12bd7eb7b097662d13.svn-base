package com.example.baselibdemo.downloadmanager;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.baselibdemo.DemoCommonUtils;
import com.example.baselibdemo.R;
import com.imove.base.utils.FacadeDownloadManager;
import com.imove.base.utils.FileUtil;
import com.imove.base.utils.Log;
import com.imove.base.utils.SdcardUtil;
import com.imove.base.utils.downloadmanager.DownloadConfiguration.DownloadTable;
import com.imove.base.utils.downloadmanager.DownloadTaskInfo;
import com.imove.base.utils.downloadmanager.excutor.DownloadState;
import com.imove.base.utils.downloadmanager.excutor.IDownloadListener;

public class DownloadManagerDemo extends Activity {
	
	private final String TAG = DownloadManagerDemo.class.getSimpleName();
	
	private FacadeDownloadManager mDownloadManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		ViewGroup rootViewGroup = (ViewGroup)findViewById(R.id.root_view);
		DemoCommonUtils.addButton(this, rootViewGroup, "下载文件", downloadRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "暂停顶层任务", pauseTaskRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "暂停所有", pauseAllRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "恢复所有", runAllTaskRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "删除所有", deleteAllRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "查询", queryRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "当前任务数", getAllTaskCountRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "release", releaseRunnable);
		
		String downloadPath = SdcardUtil.getSDPath() + "/baselib/download/";
		mDownloadManager = new FacadeDownloadManager.Builder()
				.setMaxActiveTaskCount(1)
				.setDownloadPath(downloadPath)
				.setDownloadTable(DownloadTable.DOWNLOAD_FILE_TABLE)
				.setTaskInstanceName(TAG)
				.build(getApplicationContext());
		mDownloadManager.addDownloadListener(mDownloadListener);
	}
	
	IDownloadListener mDownloadListener = new IDownloadListener() {
		@Override
		public boolean onDownloadStateChanged(DownloadState state) {
			Log.v(TAG, "state: " + state.getState() + " - progress:" + state.getProgress() + " - download:" + state.getDownloadLen() 
					+ " - Total:" + state.getTotalLen() + " - uri:" + state.getUri());
			return false;
		}
	};
	
	String[] urls = new String[] {
		"http://w.x.baidu.com/alading/anquan_soft_down_normal/17707",
		"http://gdown.baidu.com/data/wisegame/31884458c0828d21/WiFiMaster_503.apk",
		"http://www.71lady.com/uploads/allimg/140627/2_140627152242_1.jpg",
		"http://img4.zdface.com/Image/2014-07-02/20140702110412271.jpg",
		"http://gdown.baidu.com/data/wisegame/fea8666c2c58ff4e/Qzone_71.apk",
		"http://gdown.baidu.com/data/wisegame/abad88473a955c3a/QQ_134.apk",
		"http://gdown.baidu.com/data/wisegame/d9004ba4d2b7d6a4/shoujitaobao_102.apk",
	};
	
	int urlIndex = 0;
	String lastDownloadUrl;
	
	Runnable downloadRunnable = new Runnable() {
		@Override
		public void run() {
			if (urlIndex >= urls.length) {
				urlIndex = 0;
			}
			
			String url = urls[urlIndex++];
			String name = FileUtil.getFileName(url);
			int state = mDownloadManager.downloadFile(url, name);
			lastDownloadUrl = url;
			Log.d(TAG, "downloadFile state:" + state + " - name:" + name);
		}
	};
	
	Runnable pauseAllRunnable = new Runnable() {
		@Override
		public void run() {
			mDownloadManager.pauseAll();
			Log.d(TAG, "pauseAll");
		}
	};
	
	Runnable pauseTaskRunnable = new Runnable() {
		@Override
		public void run() {
			List<DownloadTaskInfo> list = mDownloadManager.queryAllTask();
			if (list.size() == 0) {
				Log.v(TAG, "pauseTask 当前无下载任务");
				return;
			}
			
			String url = list.get(0).uri;
			mDownloadManager.pauseTask(url);
			Log.d(TAG, "pauseTask url： " + url);
		}
	};
	
	Runnable deleteAllRunnable = new Runnable() {
		@Override
		public void run() {
			mDownloadManager.deleteAllTask();
			Log.d(TAG, "deleteAll");
		}
	};
	
	Runnable getAllTaskCountRunnable = new Runnable() {
		@Override
		public void run() {
			int count = mDownloadManager.getAllTaskCount();
			Log.d(TAG, "getAllTaskCount: " + count);
		}
	};
	
	Runnable queryRunnable = new Runnable() {
		@Override
		public void run() {
			if (lastDownloadUrl == null) {
				Log.e(TAG, "lastDownloadUrl is null");
				return;
			}
			DownloadTaskInfo taskInfo = mDownloadManager.query(lastDownloadUrl);
			if (taskInfo == null) {
				Log.e(TAG, "query 未查到Task url:" + lastDownloadUrl);
				return;
			}
			Log.d(TAG, "query name:" + taskInfo.fileName + " - file:" + taskInfo.downloadLen + "/" + taskInfo.fileName + " - uri:" + taskInfo.uri);
		}
	};
	
	Runnable runAllTaskRunnable = new Runnable() {
		@Override
		public void run() {
			mDownloadManager.runAllTask();
			Log.d(TAG, "runAllTask");
		}
	};
	
	Runnable releaseRunnable = new Runnable() {
		@Override
		public void run() {
			mDownloadManager.release();
			Log.d(TAG, "release");
		}
	};
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
