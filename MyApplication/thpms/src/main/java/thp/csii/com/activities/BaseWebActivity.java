package thp.csii.com.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;



import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.views.ProgressWebView;


/**
 * @author http://blog.csdn.net/finddreams
 * @Description:WebView界面，带自定义进度条显示
 */
public class BaseWebActivity extends BaseActivity {

	protected ProgressWebView mWebView;
	private ProgressBar web_progressbar;
	private LinearLayout ll_back;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	//private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	SetStatusColor();
		setContentView(R.layout.activity_baseweb);

		mWebView = (ProgressWebView) findViewById(R.id.baseweb_webview);
		mWebView.hideprogressbar();
		mWebView.getSettings().setJavaScriptEnabled(true);

		imageViewBack.setBackgroundResource(R.drawable.u194);
		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		ll_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseWebActivity.this.finish();
			}
		});
		initData();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		//client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	protected void initData() {
		Intent intent = getIntent();
		//Bundle bundle = intent.getExtras();
		String url = intent.getStringExtra("url");
		String title=intent.getStringExtra("name");
		setTitleString(title);
		//setTitleString(intent.getStringExtra("name"));

		// if(!TextUtils.isEmpty(url)&&TextUtils.isEmpty(title)){
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setSupportMultipleWindows(true);
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.loadUrl(url);
		// }

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mWebView = null;

	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();
	}
}