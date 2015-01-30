package com.example.baselibdemo.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.imove.base.utils.Log;
import com.imove.base.utils.db.BasicSQLiteHelper;
import com.imove.base.utils.db.DataColumn;
import com.imove.base.utils.db.DataColumn.DataType;
import com.imove.base.utils.db.DbTableUtils;
import com.imove.base.utils.db.IDatabaseDao;
import com.imove.base.utils.downloadmanager.KeyConstants;

/**
 * [下载任务DAO]
 * 可能存在操作多个Table的情况，操作db的时候需要指定具体的类
 * 使用SDK前需要提前指定会有哪些Table
 * 
 * @author 李理
 * @date 2013年11月21日
 */
public class DownloadTaskDao implements IDatabaseDao{
	public static final String TAG = "DownloadTaskDao";

	private final String TABLE_NAME = "downloadTask";
	
	private final String T_DOWNLOAD_ID = "downloadId";
	private final String T_DOWNLOAD_URL = "url";
	private final String T_POST_PARAM = "postParam";
	private final String T_HTTP_METHOD = "httpMethod";
	private final String T_SAVE_PATH = "path";
	private final String T_FILE_NAME = "fileName";
	private final String T_FILE_SIZE = "fileSize";
	private final String T_DOWNLOAD_SIZE = "downloadSize";
	private final String T_FILE_VERSION = "version";

	private BasicSQLiteHelper mDbEntity;
	
	public DownloadTaskDao(Context context) {
		mDbEntity = MainDatabaseHelper.getInstance(context);
	}
	
	/**
	 * [该空构造用于反射初始化]
	 */
	public DownloadTaskDao() {}
	
	public boolean add(List<DownloadBean> list) {
		if (list == null || list.size() <= 0) {
			return false;
		}

		boolean result = false;
		try {
			mDbEntity.beginTransaction();
			for (DownloadBean item : list) {
				add(item);
			}
			mDbEntity.setTransactionSuccessful();
			mDbEntity.endTransaction();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public long add(DownloadBean downloadBean) {
		if (downloadBean == null) {
			return -1;
		}
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(T_DOWNLOAD_ID, downloadBean.downloadId);
		initialValues.put(T_DOWNLOAD_URL, downloadBean.url);
		initialValues.put(T_POST_PARAM, downloadBean.postParam);
		initialValues.put(T_HTTP_METHOD, downloadBean.httpMethod);
		initialValues.put(T_SAVE_PATH, downloadBean.path);
		initialValues.put(T_FILE_NAME, downloadBean.fileName);
		initialValues.put(T_FILE_SIZE, downloadBean.filelen);
		initialValues.put(T_DOWNLOAD_SIZE, downloadBean.downloadLen);
		initialValues.put(T_FILE_VERSION, downloadBean.fileVersion);
		long result = mDbEntity.insert(TABLE_NAME, null, initialValues);
		return result;
	}

	public boolean alter(DownloadBean downloadBean) {
		if (downloadBean == null) {
			return false;
		}
		ContentValues initialValues = new ContentValues();
		if (downloadBean.fileName != null) {
			initialValues.put(T_FILE_NAME, downloadBean.fileName);
		}
		if (downloadBean.filelen != 0) {
			initialValues.put(T_FILE_SIZE, downloadBean.filelen);
		}
		if (downloadBean.downloadLen != 0) {
			initialValues.put(T_DOWNLOAD_SIZE, downloadBean.downloadLen);
		}
		boolean result = mDbEntity.update(TABLE_NAME, initialValues, 
				T_DOWNLOAD_ID + "=\"" + downloadBean.downloadId + "\"", null) > 0;
		return result;
	}
	
	public boolean delete(String downloadID) {
		return delete(T_DOWNLOAD_ID + "=?", new String[] { String.valueOf(downloadID) });
	}

	private boolean delete(String whereClause, String[] values) {
		if (whereClause == null) {
			return mDbEntity.delete(TABLE_NAME, null, null) > 0;
		} else {
			return mDbEntity.delete(TABLE_NAME, whereClause, values) > 0;
		}
	}
	
	public DownloadBean query(String downloadId) {
		List<DownloadBean> result = getList(null, T_DOWNLOAD_ID + "=?", 
				new String[] { downloadId }, null, null, null, null);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	public List<DownloadBean> queryAll() {
		return getList(null, null, null, null,null, null,null);
	}
	
	public List<DownloadBean> getList(String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		
		List<DownloadBean> list = new ArrayList<DownloadBean>();
		DownloadBean task = null;
		Cursor c = null;
		
		try {
			c = mDbEntity.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
			boolean hadata = (c == null) ? false : c.moveToFirst();
			if (hadata) {
				int downloadIdIndex = c.getColumnIndex(T_DOWNLOAD_ID);
				int urlIndex = c.getColumnIndex(T_DOWNLOAD_URL);
				int postParamIndex = c.getColumnIndex(T_POST_PARAM);
				int methodIndex = c.getColumnIndex(T_HTTP_METHOD);
				int pathIndex = c.getColumnIndex(T_SAVE_PATH);
				int nameIndex = c.getColumnIndex(T_FILE_NAME);
				int sizeIndex = c.getColumnIndex(T_FILE_SIZE);
				int downloadSizeIndex = c.getColumnIndex(T_DOWNLOAD_SIZE);
				int versionIndex = c.getColumnIndex(T_FILE_VERSION);
				do {
					task = new DownloadBean();
					if (downloadIdIndex != -1) {
						task.downloadId = c.getString(downloadIdIndex);
					}
					if (urlIndex != -1) {
						task.url = c.getString(urlIndex);
					}
					if (postParamIndex != -1) {
						task.postParam = c.getString(postParamIndex);
					}
					if (methodIndex != -1) {
						task.httpMethod = c.getString(methodIndex);
					}
					if (pathIndex != -1) {
						task.path = c.getString(pathIndex);
					}
					if (nameIndex != -1) {
						task.fileName = c.getString(nameIndex);
					}
					if (sizeIndex != -1) {
						task.filelen = c.getInt(sizeIndex);
					}
					if (downloadSizeIndex != -1) {
						task.downloadLen = c.getInt(downloadSizeIndex);
					}
					if (versionIndex != -1) {
						task.fileVersion = c.getInt(versionIndex);
					}
					
					if (task.path == null) {
						continue;
					}
					if (task.filelen == task.downloadLen) {
						//下载完成的文件进行文件检查
						File file = new File(task.path);
						if (! file.exists()) {
							task.downloadState = KeyConstants.DOWNLOAD_STATE_NONE;
							task.downloadLen = 0;
						}
					}
					
					list.add(task);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}

		return list;
	}
	
	@Override
	public void createDao(SQLiteDatabase db) {
		Log.v(TAG, "createDao");
		ArrayList<DataColumn> list = new ArrayList<DataColumn>();
		list.add(new DataColumn(T_DOWNLOAD_ID, DataType.TEXT, null, false));
		list.add(new DataColumn(T_DOWNLOAD_URL, DataType.TEXT, null, false));
		list.add(new DataColumn(T_POST_PARAM, DataType.TEXT, null, true));
		list.add(new DataColumn(T_HTTP_METHOD, DataType.TEXT, null, false));
		list.add(new DataColumn(T_SAVE_PATH, DataType.TEXT, null, false));
		list.add(new DataColumn(T_FILE_NAME, DataType.TEXT, null, false));
		list.add(new DataColumn(T_FILE_SIZE, DataType.INTEGER, null, true));
		list.add(new DataColumn(T_DOWNLOAD_SIZE, DataType.INTEGER, null, true));
		list.add(new DataColumn(T_FILE_VERSION, DataType.INTEGER, 0, true));
		DbTableUtils.createTable(db, TABLE_NAME, list);
	}

	@Override
	public void upgradeDao(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			boolean isExist = isExistTable(db, TABLE_NAME);
			if (isExist) {
				if (oldVersion <= 1) {
					// 增加type、version字段
					ArrayList<DataColumn> list = new ArrayList<DataColumn>();
					list.add(new DataColumn(T_FILE_VERSION, DataType.INTEGER,
							0, true));
					for (DataColumn column : list) {
						StringBuilder builder = new StringBuilder();
						builder.append("ALTER TABLE ");
						builder.append(TABLE_NAME);
						builder.append(" add COLUMN ");
						builder.append(column.name).append(" ");
						builder.append(column.type);
						if (!column.defCanNull) {
							builder.append(" NOT NULL");
						}
						if (column.defValue != null) {
							builder.append(" DEFAULT ").append(
									String.valueOf(column.defValue));
						}

						String sql = builder.toString();
						db.execSQL(sql);
					}
				}
			} else {
				createDao(db);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isExistTable(SQLiteDatabase db, String tableName) {
		String sql = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					return true;
				}
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return false;
	}
}
