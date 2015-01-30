/*
 * 版    权： 深圳市爱猫新媒体数据科技有限公司
 * 创建人: 李理
 * 创建时间: 2014年8月14日
 */
package com.example.baselibdemo.downloadmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselibdemo.R;
import com.example.baselibdemo.downloadmanager.adapter.DownloadItem;
import com.example.baselibdemo.downloadmanager.adapter.DownloadListAdapter;
import com.example.baselibdemo.downloadmanager.adapter.DownloadListAdapter.OnDownloadClickListener;
import com.imove.base.utils.FileDownloadManager;
import com.imove.base.utils.FileDownloadManager.OnDownloadUpdateListener;
import com.imove.base.utils.FileUtil;
import com.imove.base.utils.Log;
import com.imove.base.utils.SdcardUtil;
import com.imove.base.utils.ThreadPoolManagerQuick;
import com.imove.base.utils.downloadmanager.DownloadTaskInfo;
import com.imove.base.utils.downloadmanager.excutor.DownloadState;
import com.imove.base.utils.downloadmanager.excutor.IDownloadListener;


/**
 * [描述该类的作用]
 * @author 李理 
 */
public class DownloadListDemo extends Activity implements OnClickListener{

	private final String TAG = DownloadListDemo.class.getSimpleName();
	
	private Handler mHandler;
	
	private Spinner mDownloadSpinner;
	
	private ListView mListView;
	
	private DownloadListAdapter mAdapter;
	
	private FileDownloadManager mDownloadManager;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);
        findViewById(R.id.btn_download).setOnClickListener(this);
        findViewById(R.id.btn_pause_all).setOnClickListener(this);
        findViewById(R.id.btn_resume_all).setOnClickListener(this);
        findViewById(R.id.btn_delete_all).setOnClickListener(this);
        findViewById(R.id.btn_record_pause).setOnClickListener(this);
        findViewById(R.id.btn_record_resume).setOnClickListener(this);
        findViewById(R.id.btn_download_network).setOnClickListener(this);
        mDownloadSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.download_urls, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDownloadSpinner.setAdapter(adapter);
        
        mListView = (ListView)findViewById(R.id.listview);
        mAdapter = new DownloadListAdapter(this, mListView);
        mAdapter.setOnDownloadClickListener(mDownloadClickListener);
        mListView.setAdapter(mAdapter);
        
		mHandler = new Handler();
		

        ThreadPoolManagerQuick.execute(new Runnable() {
        	public void run() {
        		initDownloadManger();
			}
        });
    }

	private void initDownloadManger() {
		String downloadPath = SdcardUtil.getSDPath() + "/baselib/download/";
		mDownloadManager = FileDownloadManager.getInstance(getApplicationContext());
		mDownloadManager.init(downloadPath);
		mDownloadManager.addDownloadListener(mDownloadListener);
		mDownloadManager.setOnDownloadUpdateListener(mDownloadUpdateListener);
		isAllowDownloadNoWifi = false;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				allowDownloadNoWifi(isAllowDownloadNoWifi);
			}
		});
		
		final List<DownloadTaskInfo> list = mDownloadManager.queryAllTask();
		if (list == null) {
			return;
		}
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				for(DownloadTaskInfo info : list) {
					String url = info.uri;
					String fileName = FileUtil.getFileName(url);
					DownloadItem item = new DownloadItem();
					item.fileName = fileName;
					item.downloadUrl = url;
					if (info.filelen == info.downloadLen) {
						item.state = DownloadItem.STATE_SUC;
					}
					mAdapter.addDownloadItem(item);
				}
				
				ThreadPoolManagerQuick.execute(new Runnable() {
		        	public void run() {
		        		mDownloadManager.resumeAllRecordTask();
		        	}
				});
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_download:
			download();
			break;
		case R.id.btn_pause_all:
			pauseAllTask();
			break;
		case R.id.btn_resume_all:
			resumeAllTask();
			break;
		case R.id.btn_delete_all:
			deleteAllTask();
			break;
		case R.id.btn_record_pause:
			pauseAllAndRecordTask();
			break;
		case R.id.btn_record_resume:
			resumeAllRecordTask();
			break;
		case R.id.btn_download_network:
			isAllowDownloadNoWifi = !isAllowDownloadNoWifi;
			allowDownloadNoWifi(isAllowDownloadNoWifi);
			break;
		default:
			break;
		}
	}

	private boolean isAllowDownloadNoWifi;
	private void allowDownloadNoWifi(boolean b) {
		String text = null;
		if (b) {
			text = "允许2G/3G下载";
		} else {
			text = "只允许Wifi下载";
		}
		TextView textView = (TextView)findViewById(R.id.btn_download_network);
		textView.setText(text);
		mDownloadManager.setAllowDownloadNoWifi(b);
	}
	
	public void pauseAllAndRecordTask() {
		mDownloadManager.pauseAllAndRecordTask();
	}
	
	public void resumeAllRecordTask() {
		mDownloadManager.resumeAllRecordTask();
	}
	
	private void pauseAllTask() {
		mDownloadManager.pauseAll();
	}
	
	private void resumeAllTask() {
		mDownloadManager.runAllTask();
	}
	
	private void deleteAllTask() {
		mDownloadManager.deleteAllTask();
		mAdapter.deleteAllItem();
	}
	
	private void download() { 
		String downloadUrl = mDownloadSpinner.getSelectedItem().toString();
		
		if (mAdapter.findDownloadItem(downloadUrl) != null) {
			return;
		}
		Log.v(TAG, "download: " + downloadUrl);
		download(downloadUrl, true);
	}
	
	private void download(final String downloadUrl, boolean isInsertTask) {
		final String fileName = FileUtil.getFileName(downloadUrl);;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				DownloadItem item = new DownloadItem();
				item.fileName = fileName;
				item.downloadUrl = downloadUrl;
				mAdapter.addDownloadItem(item);
			}
		});
		Map<String, String> header = new HashMap<String, String>();
		header.put("Accept", "*/*");
		mDownloadManager.downloadFile(downloadUrl, header, fileName, FileDownloadManager.RUN_TYPE_INSERT_QUEUE_AND_RUN);
	}
	
	IDownloadListener mDownloadListener = new IDownloadListener() {
		
		DownloadState lastState;
		
		@Override
		public boolean onDownloadStateChanged(final DownloadState state) {
			Log.v(TAG, "state: " + state.getState() + " - "+ state.getUri() + " - obj:" + state);
//			Log.v(TAG, "state: " + state.getState() + " - progress:" + state.getProgress() + " - download:" + state.getDownloadLen() 
//					+ " - Total:" + state.getTotalLen() + " - uri:" + state.getUri());
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					DownloadItem item = mAdapter.findDownloadItem(state.getDownloadId());
					if (item == null) {
						Log.e(TAG, "未找到对应的下载任务");
						return;
					}
					int downloadState;
					switch (state.getState()) {
					case DownloadState.STATE_PENDING:
						downloadState = DownloadItem.STATE_PENDING;
						break;
					case DownloadState.STATE_INTO_DOWNLOADING_QUEUE:
					case DownloadState.STATE_DOWNLOADING:
					case DownloadState.STATE_PREPARE:
					case DownloadState.STATE_START:
						downloadState = DownloadItem.STATE_DOWNLOADING;
						break;
					case DownloadState.STATE_NONE:
					case DownloadState.STATE_PAUSE:
						downloadState = DownloadItem.STATE_IDLE;
						break;
					case DownloadState.STATE_SUC:
						downloadState = DownloadItem.STATE_SUC;
						break;
					case DownloadState.STATE_FAIL:
						downloadState = DownloadItem.STATE_FAIL;
						break;
					default:
						return;
					};
					if (downloadState == item.state) {
						return;
					}
					item.state = downloadState;
					item.downloadSize = FileUtil.parseFileSizeF(state.getDownloadLen())
							+ "/" + FileUtil.parseFileSizeF(state.getTotalLen());
					if (state.getState() != DownloadState.STATE_DOWNLOADING) {
						item.speed = ""; 
					} else {
						if (lastState == null || ! lastState.getDownloadId().equals(state.getDownloadId())) {
							item.speed = FileUtil.parseFileSizeF(state.getDownloadLen());
						} else {
							item.speed = FileUtil.parseFileSizeF(state.getDownloadLen() - lastState.getDownloadLen());
						}
					}
					mAdapter.notifyItemView(item.downloadUrl);
					
					lastState = state;
				}
			});
			return false;
		}
	};
	
	OnDownloadUpdateListener mDownloadUpdateListener = new OnDownloadUpdateListener() {

		@Override
		public void onDownloadUpdate(final List<DownloadTaskInfo> downloadings) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					for(DownloadTaskInfo task : downloadings) {
						DownloadItem item = mAdapter.findDownloadItem(task.downloadId);
						if (item == null) {
							continue;
						}
						item.downloadSize = FileUtil.parseFileSizeF(task.downloadLen)
								+ "/" + FileUtil.parseFileSizeF(task.filelen);
						item.speed = FileUtil.parseFileSizeF(task.downloadrate);
						mAdapter.notifyItemView(item.downloadUrl);
					}
				}
			});
		}
	};

	OnDownloadClickListener mDownloadClickListener = new OnDownloadClickListener() {

		@Override
		public void onDownloadClick(DownloadItem item) {
			if (item.state == DownloadItem.STATE_DOWNLOADING) {
				mDownloadManager.pauseTask(item.downloadUrl);
			} else if (item.state == DownloadItem.STATE_PENDING || 
					item.state == DownloadItem.STATE_FAIL ||
					item.state == DownloadItem.STATE_IDLE) {
				mDownloadManager.runTask(item.downloadUrl, FileDownloadManager.RUN_TYPE_INSERT_QUEUE_AND_RUN);
			} else if (item.state == DownloadItem.STATE_SUC) {
				Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onDownloadDelete(DownloadItem item) {
			deleteTask(item);
		} 
	};
	
	private void deleteTask(DownloadItem item) {
		mDownloadManager.deleteTask(item.downloadUrl, true);
		mAdapter.deleteItem(item.downloadUrl);
		mAdapter.notifyDataSetChanged();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDownloadManager != null) {
			mDownloadManager.pauseAllAndRecordTask();
			mDownloadManager.release();
		}
		android.os.Process.killProcess(Process.myPid());
	}
	
}
