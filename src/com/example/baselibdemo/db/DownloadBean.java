package com.example.baselibdemo.db;

import com.imove.base.utils.downloadmanager.KeyConstants;

/**
 * [下载任务Bean]<br/>
 * 
 * @author 李理
 * @date 2013年11月19日
 */
public class DownloadBean {

	public String downloadId;
	
	public String url;
	
	public String postParam;
	
	public String httpMethod = "Get";
	
	/**
	 * 存储路径
	 */
	public String path;
	
	/**
	 * 文件名
	 */
	public String fileName;
	
	/**
	 * 文件总大小 
	 * 字节
	 */
	public int filelen;
	
	/**
	 * 已下载大小
	 */
	public int downloadLen;
	
	/**
	 * {@link com.qvod.player.utils.downloadmanager.KeyConstants#DOWNLOAD_STATE_START}
	 * {@link com.qvod.player.utils.downloadmanager.KeyConstants#DOWNLOAD_STATE_*}
	 */
	public int downloadState = KeyConstants.DOWNLOAD_STATE_UNKOWN;
	
	/**
	 * 文件版本
	 */
	public int fileVersion;
}

