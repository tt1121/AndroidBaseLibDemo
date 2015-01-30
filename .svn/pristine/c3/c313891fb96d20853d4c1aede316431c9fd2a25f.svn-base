package com.example.baselibdemo.preferences;

import com.imove.base.utils.BasePreference;

public class DemoSharePreference extends BasePreference {

	private final String PREFERENCE_NAME = "Settng_Demo";
	
	public static final int TYPE_UPDATE_TIME = 1;
	public static final int TYPE_IS_PUSH_MSG = 2;
	
	private final String KEY_UPDATE_TIME = "updateTime";
	private final String KEY_IS_PUSH_MSG = "isPushMsg";
	
	@Override
	protected String getPreferenceName() {
		return PREFERENCE_NAME;
	}

	@Override
	protected String getKey(int type) {
		switch (type) {
		case TYPE_UPDATE_TIME:
			return KEY_UPDATE_TIME;
		case TYPE_IS_PUSH_MSG:
			return KEY_IS_PUSH_MSG;
		default:
			break;
		}
		return null;
	}
}
