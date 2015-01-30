/*
 * 版    权： 深圳市爱猫新媒体数据科技有限公司
 * 创建人: 李理
 * 创建时间: 2014年8月14日
 */
package com.example.baselibdemo.downloadmanager.adapter;

/**
 * @author 李理 
 */
public class DownloadItem {

	public static final int STATE_IDLE = 0;
	public static final int STATE_PENDING = 1;
	public static final int STATE_DOWNLOADING = 2;
	public static final int STATE_SUC = 3;
	public static final int STATE_FAIL = 4;
	
	public int state = STATE_IDLE;
	
	public String fileName;
	
	public String speed;
	
	public String downloadSize;
	
	public String downloadUrl;
	
}
