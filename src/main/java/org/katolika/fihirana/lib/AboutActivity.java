package org.katolika.fihirana.lib;

import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		

		DatabaseHelper db = new DatabaseHelper(this);

		int cnt = db.getHiraCount();

		TextView info = (TextView) findViewById(R.id.txtinfo);
		info.setText(Html.fromHtml("<b>Totalin'ny hira:</b> " + cnt));
		
		WebView webView = (WebView) findViewById(R.id.webView);	 
		webView.setWebViewClient(new WebViewClient());
	    webView.loadUrl("file:///android_asset/info.html");
	}



}
