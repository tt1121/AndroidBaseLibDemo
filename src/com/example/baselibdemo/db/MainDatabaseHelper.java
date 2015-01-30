package com.example.baselibdemo.db;

import java.util.HashSet;

import android.content.Context;

import com.imove.base.utils.db.BasicSQLiteHelper;
import com.imove.base.utils.db.IDatabaseDao;

/**
 * @author 李理
 * @date 2013年11月18日
 */
public class MainDatabaseHelper extends BasicSQLiteHelper {
	public static final String TAG = MainDatabaseHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "main.db";
	private static final int DATABASE_VERSION = 2;
	
	private static MainDatabaseHelper sInstance;
	
	public synchronized static MainDatabaseHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new MainDatabaseHelper(context);
		}
		return sInstance;
	}
	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public MainDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	protected HashSet<Class<? extends IDatabaseDao>> getTableDaoClass() {
		HashSet<Class<? extends IDatabaseDao>> set = new HashSet<Class<? extends IDatabaseDao>>();
		set.add(DownloadTaskDao.class);
		return set;
	}
}
