/*
 * 版    权： 深圳市爱猫新媒体数据科技有限公司
 * 创建人: 李理
 * 创建时间: 2014年8月14日
 */
package com.example.baselibdemo.downloadmanager.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.baselibdemo.R;
import com.imove.base.utils.StringUtils;

/**
 * [描述该类的作用]
 * @author 李理 
 */
public class DownloadListAdapter extends BaseAdapter {

	private List<DownloadItem> mDownloadItems;

	private LayoutInflater mLayoutInflater;
	
	private AdapterView mParentView;
	
	private final int TAG_KEY_DATA = 0xffff0011;
	
	public DownloadListAdapter(Context context, AdapterView parentView) {
		mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mParentView = parentView;
	}
	
	public void addDownloadItem(DownloadItem item) {
		if (item == null || item.downloadUrl == null) {
			return;
		}
		if (mDownloadItems == null) {
			mDownloadItems = new ArrayList<DownloadItem>();
		}
		mDownloadItems.add(item);
		this.notifyDataSetChanged();
	}

	public DownloadItem findDownloadItem(String downloadUrl) {
		if (mDownloadItems == null || downloadUrl == null) {
			return null;
		}
		for(DownloadItem item : mDownloadItems) {
			if (downloadUrl.equals(item.downloadUrl)) {
				return item;
			}
		}
		return null;
	}
	
	public boolean deleteItem(String downloadUrl) {
		if (mDownloadItems == null || downloadUrl == null) {
			return false;
		}
		Iterator<DownloadItem> it = mDownloadItems.iterator();
		while(it.hasNext()) {
			DownloadItem item = it.next();
			if (downloadUrl.equals(item.downloadUrl)) {
				it.remove();
				return true;
			}
		}
		return false;
	}
	
	public void deleteAllItem() {
		if (mDownloadItems == null) {
			return;
		}
		mDownloadItems.clear();
		notifyDataSetChanged();
	}

	public void notifyItemView(String downloadUrl) {
		if (mParentView == null || downloadUrl == null) {
			return;
		}
		int position = -1;
		DownloadItem downloadItem = null;
		for(int i = 0; i < mDownloadItems.size();i++) {
			DownloadItem item = mDownloadItems.get(i);
			if (downloadUrl.equals(item.downloadUrl)) {
				downloadItem = item;
				position = i;
				break;
			}
		}
		if (position == -1) {
			return;
		}
		
		for(int i = 0;i < mParentView.getChildCount();i++) {
			View view = mParentView.getChildAt(i);
			DownloadItem item = (DownloadItem)view.getTag(TAG_KEY_DATA);
			if (downloadItem == item) {
				getView(position, view, mParentView);
			}
		}
	}
	
	@Override
	public int getCount() {
		return mDownloadItems == null ? 0 : mDownloadItems.size();
	}

	@Override
	public Object getItem(int position) {
		if (mDownloadItems == null) {
			return null;
		}
		return mDownloadItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DownloadItem item = mDownloadItems.get(position);
		DownloadHolder holder = null;
		if (convertView == null) {
			holder = new DownloadHolder();
			convertView = mLayoutInflater.inflate(R.layout.download_list_item, null);
			holder.fileNameTv = (TextView)convertView.findViewById(R.id.txt_file_name);
			holder.speedTv = (TextView)convertView.findViewById(R.id.txt_speed);
			holder.downloadSizeTv = (TextView)convertView.findViewById(R.id.txt_download_size);
			holder.downloadBtn = (Button)convertView.findViewById(R.id.btn_download);
			holder.downloadBtn.setOnClickListener(listener);
			holder.deleteBtn = (Button)convertView.findViewById(R.id.btn_delete);
			holder.deleteBtn.setOnClickListener(listener);
			convertView.setTag(holder);
		} else {
			holder = (DownloadHolder)convertView.getTag();
		}
		holder.fileNameTv.setText(item.fileName);
		holder.downloadSizeTv.setText(item.downloadSize == null ? "" : item.downloadSize);
		String speed = item.speed == null ? "" : item.speed;
		holder.speedTv.setText(speed);
		int visibility = View.VISIBLE;
		String stateStr = "";
		if (item.state == DownloadItem.STATE_IDLE) {
			stateStr = "下载";
		} else if (item.state == DownloadItem.STATE_PENDING) {
			stateStr = "等待";
		} else if (item.state == DownloadItem.STATE_DOWNLOADING) {
			stateStr = "暂停";
		} else if (item.state == DownloadItem.STATE_FAIL) {
			stateStr = "失败";
		} else if (item.state == DownloadItem.STATE_SUC) {
			stateStr = "完成";
		}
		holder.downloadBtn.setText(stateStr);
		holder.downloadBtn.setVisibility(visibility);
		holder.downloadBtn.setTag(item);
		holder.deleteBtn.setTag(item);
		convertView.setTag(TAG_KEY_DATA, item);
		return convertView;
	}
	
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			DownloadItem item = (DownloadItem)v.getTag();
			int id = v.getId();
			if (id == R.id.btn_download) {
				if (mListener != null) {
					mListener.onDownloadClick(item);
				}
			} else if (id == R.id.btn_delete) {
				if (mListener != null) {
					mListener.onDownloadDelete(item);
				}
			}
		}
	};
	
	private OnDownloadClickListener mListener;
	
	public void setOnDownloadClickListener(OnDownloadClickListener l) {
		mListener = l;
	}
	
	public interface OnDownloadClickListener {
		void onDownloadClick(DownloadItem item);
		void onDownloadDelete(DownloadItem item);
	}
	
	class DownloadHolder {
		TextView fileNameTv;
		TextView speedTv;
		TextView downloadSizeTv;
		Button downloadBtn;
		Button deleteBtn;
	}

}
