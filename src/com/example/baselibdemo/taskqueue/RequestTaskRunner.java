package com.example.baselibdemo.taskqueue;

import java.io.IOException;

import com.imove.base.utils.Log;
import com.imove.base.utils.http.WebUtils;
import com.imove.base.utils.task.AutoRunTaskQueue.AutoTaskRunner;

public class RequestTaskRunner implements AutoTaskRunner<RequestTask> {

	private final String TAG = "debug";
	
	private boolean isRun;
	
	@Override
	public boolean runTask(RequestTask task) {
		String url = task.url;
		try {
			String result = WebUtils.doGet(url);
			if (! isRun) {
				Log.i(TAG, "runTask 请求被终止， url：" + url);
				return true;
			}
			Log.i(TAG, "runTask result:" + result);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void start() {
		isRun = true;
	}

	@Override
	public void stop() {
		isRun = false;
	}

}
