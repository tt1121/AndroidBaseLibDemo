package com.example.baselibdemo;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class DemoCommonUtils {

	public static void addButton(Context context, ViewGroup rootView, String name, final Runnable runnable) {
		Button button = new Button(context);
		button.setTextSize(18);
		button.setTextColor(Color.BLACK);
		button.setText(name);
		rootView.addView(button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(runnable != null) {
					runnable.run();
				}
			}
		});
		
	}
}
