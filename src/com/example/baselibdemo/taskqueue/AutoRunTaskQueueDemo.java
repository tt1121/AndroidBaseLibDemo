package com.example.baselibdemo.taskqueue;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.baselibdemo.DemoCommonUtils;
import com.example.baselibdemo.R;
import com.imove.base.utils.Log;
import com.imove.base.utils.task.AutoRunTaskQueue;
import com.imove.base.utils.task.AutoRunTaskQueue.OnAutoRunTaskListener;

public class AutoRunTaskQueueDemo extends Activity {
	
	private final String TAG = AutoRunTaskQueueDemo.class.getSimpleName();
	
	private AutoRunTaskQueue<RequestTask> mTaskQueue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		ViewGroup rootViewGroup = (ViewGroup)findViewById(R.id.root_view);
		DemoCommonUtils.addButton(this, rootViewGroup, "添加任务", addTaskRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "启动任务", startRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "停止任务", stopRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "清空任务", clearRunnable);
		DemoCommonUtils.addButton(this, rootViewGroup, "队列长度", printRunnable);
		
		RequestTaskRunner runner = new RequestTaskRunner();
		mTaskQueue = new AutoRunTaskQueue<RequestTask>(runner);
		mTaskQueue.setOnAutoRunTaskListener(mRunTaskListener);
	}
	
	OnAutoRunTaskListener<RequestTask> mRunTaskListener = new OnAutoRunTaskListener<RequestTask>() {

		@Override
		public void onRunTaskEnd(boolean isSuc, RequestTask task) {
			Log.d(TAG, "Task 运行结束 isSuc：" + isSuc + " - size:" + mTaskQueue.getWorkSize() + " - url:" + task.url + " - 重复尝试次数：" + task.currentRetryCount);
			if (mTaskQueue.getWorkSize() == 0) {
				Log.e(TAG, "队列任务全部执行完成");
			}
		}
	};

	String[] urls = new String[] {
		"http://www.baidu.com/",
		"http://www.taobao.com/",
		"http://www.jd.com/",
		"http://www.4399.com/",
		"http://www.163.com/",
		"http://www.youku.com/i/",
		"http://sz.58.com/",
		"http://www.vip.com/"
	};
	
	private int mUrlIndex = 0;
	Runnable addTaskRunnable = new Runnable() {
		@Override
		public void run() {
			if (mUrlIndex >= urls.length) {
				mUrlIndex = 0;
			}
			RequestTask task = new RequestTask();
			task.url = urls[mUrlIndex++];
			mTaskQueue.addTask(task);
			Log.v(TAG, "添加请求任务： " + task.url);
		}
	};
	
	Runnable startRunnable = new Runnable() {
		@Override
		public void run() {
			mTaskQueue.start();
			Log.v(TAG, "start");
		}
	};
	
	Runnable stopRunnable = new Runnable() {
		@Override
		public void run() {
			mTaskQueue.stopRun();
			Log.v(TAG, "stopRun");
		}
	};
	
	Runnable clearRunnable = new Runnable() {
		@Override
		public void run() {
			mTaskQueue.clearTasks();
			Log.v(TAG, "clearTasks");
		}
	};
	
	Runnable printRunnable = new Runnable() {
		@Override
		public void run() {
			int workSize = mTaskQueue.getWorkSize();
			Log.v(TAG, "workSize:" + workSize);
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
