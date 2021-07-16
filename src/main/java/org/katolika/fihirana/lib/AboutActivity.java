package org.katolika.fihirana.lib;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import org.katolika.fihirana.lib.database.FihiranaViewModel;

import java.util.Locale;

public class AboutActivity extends BaseActivity {

	FihiranaViewModel fihiranaViewModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		fihiranaViewModel = new ViewModelProvider(this).get(FihiranaViewModel.class);

		TextView info = findViewById(R.id.txtinfo);
		fihiranaViewModel.getHiraCount().observe(this, count -> {
			info.setText(Html.fromHtml(getString(R.string.str_totalin_ny_hira, count)));
		});

		WebView webView = findViewById(R.id.webView);
		webView.setWebViewClient(new WebViewClient());
	    webView.loadUrl("file:///android_asset/info.html");

		findViewById(R.id.imgSync).setOnClickListener(view -> {
			startActivity(new Intent(this, DialogUpdate.class));
		});
	}

}
