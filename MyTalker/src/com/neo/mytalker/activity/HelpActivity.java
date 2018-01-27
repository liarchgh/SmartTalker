package com.neo.mytalker.activity;

import com.neo.mytalker.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HelpActivity extends Activity {
	WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		mWebView=(WebView) findViewById(R.id.help_webview);
		mWebView.loadUrl("file:///android_asset/help/index.html");
		mWebView.getSettings().setJavaScriptEnabled(true);
	}

}
