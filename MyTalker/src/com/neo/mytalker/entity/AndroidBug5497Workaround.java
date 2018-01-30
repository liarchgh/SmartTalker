package com.neo.mytalker.entity;

//Workaround to get adjustResize functionality for input methos when the fullscreen mode is on
//found by Ricardo
//taken from http://stackoverflow.com/a/19494006

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class AndroidBug5497Workaround {

	static // For more information, see
	// https://code.google.com/p/android/issues/detail?id=5497
	// To use this class, simply invoke assistActivity() on an Activity that already
	// has its content view set.
    Activity ac;

	public static void assistActivity(Activity activity) {
		new AndroidBug5497Workaround(activity);
		ac=activity;
	}

	private View mChildOfContent;
	private int usableHeightPrevious;
	private FrameLayout.LayoutParams frameLayoutParams;

	private AndroidBug5497Workaround(Activity activity) {
		FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
		mChildOfContent = content.getChildAt(0);
		mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			public void onGlobalLayout() {
				possiblyResizeChildOfContent();
			}
		});
		
		frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
		ac=activity;
		//possiblyResizeChildOfContent();
	}
	
	private void possiblyResizeChildOfContent() {
      int usableHeightNow = computeUsableHeight();
      if (usableHeightNow != usableHeightPrevious) {
          int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
          float dp2px= ac.getResources().getDisplayMetrics().density;
          int heightDifference = usableHeightSansKeyboard - usableHeightNow-1-(int)(5*dp2px);
          
          if (heightDifference > (usableHeightSansKeyboard/4)) {
              // keyboard probably just became visible
              frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
          } else {
              // keyboard probably just became hidden
              frameLayoutParams.height = usableHeightSansKeyboard;
          }
          frameLayoutParams.gravity=Gravity.BOTTOM;
          mChildOfContent.requestLayout();
          usableHeightPrevious = usableHeightNow;
          Log.i("ZX",""+usableHeightSansKeyboard+" "+heightDifference+" "+frameLayoutParams.height);
  		Resources resources = ac.getResources();
  		DisplayMetrics dm = resources.getDisplayMetrics();
  		float density = dm.density;
  		int width = dm.widthPixels;
  		int height = dm.heightPixels;
  		
          Log.i("ZX","currentsize："+width+" "+height);
      }
  }

	private int computeUsableHeight() {
		Rect r = new Rect();
		mChildOfContent.getWindowVisibleDisplayFrame(r);
		if (r.top == 0) {
			r.top = 0;// 状态栏目的高度
		}
		return (r.bottom - r.top);
	}

}