package com.example.baselibdemo.httpconnect;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.baselibdemo.DemoCommonUtils;
import com.example.baselibdemo.R;
import com.imove.base.utils.Log;
import com.imove.base.utils.http.HttpConnectManager;
import com.imove.base.utils.http.JsonParser;
import com.imove.base.utils.http.OnRequestListener;
import com.imove.base.utils.http.Request;

public class HttpConnectManagerDemo extends Activity {

	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		ViewGroup rootViewGroup = (ViewGroup)findViewById(R.id.root_view);
		DemoCommonUtils.addButton(this, rootViewGroup, "请求", requestRunnable);
		mHandler = new Handler();
	}
	
	
	Runnable requestRunnable = new Runnable() {
		@Override
		public void run() {
			requestQueryPiao();
		}
	};
	
	private static final String REQUEST_URL = "http://piao.163.com/m/clientinfo/query.html?";
	private final int REQUEST_TYPE_PIAO = 10;
	
	private void requestQueryPiao() {
		Map<String, String> uriParam = new HashMap<String, String>();
		uriParam.put("app_id", "2");
		uriParam.put("mobileType", "iPhone");
		uriParam.put("ver", "2.6");
		uriParam.put("channel", "lede");
		uriParam.put("deviceId", "F9737541-9435-449A-A46C-6D8E824226AE");
		uriParam.put("apiVer", "11");
		uriParam.put("city", "440300");
		
		Map<String, String> postParam = new HashMap<String, String>();
		postParam.put("mobile_os_type", "iPhone");
		
		Map<String, String> headParam = new HashMap<String, String>();
		headParam.put("Cookie", "JSESSIONID=abc8J3QQN2ub7fWwQkNDu; JSESSIONID0=f6228b4604d655e36c52c54a54ff74f1");
		headParam.put("User-Agent", "NTES(ios 7.1.2;iPhone;640*960) Movie163/2.6");
		
		Request request = new Request(REQUEST_URL);
		request.setUriParam(uriParam);
		request.setHttpHead(headParam);
		request.setOnRequestListener(mRequestListener);
		request.setParser(new JsonParser(PiaoAppInfo.class));
		request.setRequestType(REQUEST_TYPE_PIAO);
		HttpConnectManager.getInstance(getApplicationContext()).doPost(request, postParam);
	}
	
	OnRequestListener mRequestListener = new OnRequestListener() {

		@Override
		public void onResponse(String url, int state, final Object result, int type,
				Request request, Map<String, String> headMap) {
			Log.v("debug", "onResponse");
			if(type == REQUEST_TYPE_PIAO) {
				if(state == HttpConnectManager.STATE_SUC && result != null) {
					mHandler.post(new Runnable() {
						public void run() {
							PiaoAppInfo info = (PiaoAppInfo)result;
							Toast.makeText(getApplicationContext(), "请求成功，最新Verision：" + info.object.version, Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					mHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
