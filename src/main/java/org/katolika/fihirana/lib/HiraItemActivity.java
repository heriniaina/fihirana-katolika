package org.katolika.fihirana.lib;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import org.katolika.fihirana.lib.database.FavoriteViewModel;
import org.katolika.fihirana.lib.database.FihiranaViewModel;
import org.katolika.fihirana.lib.models.Hira;

import java.io.InputStream;

public class HiraItemActivity extends BaseActivity  {
	private static final String TAG = "HiraItemActivity";
	FihiranaViewModel fihiranaViewModel;
	FavoriteViewModel favoriteViewModel;

	int h_id = 0;

	String h_text = "";
	ImageButton btn_favorite_on;
	ImageButton btn_favorite_off;
	ImageButton btn_share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hira_item);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


		Intent intent = getIntent();
		h_id = intent.getIntExtra("org.katolika.fihirana.lib.H_ID", 0);

		btn_favorite_off = findViewById(R.id.favorite_off);
		btn_favorite_on = findViewById(R.id.favorite_on);
		btn_share = findViewById(R.id.share);


		fihiranaViewModel = new ViewModelProvider(this).get(FihiranaViewModel.class);
		favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

		fihiranaViewModel.getHiraItem(h_id).observe(this, hira -> {

			// fill hira info
			TextView t_title = findViewById(R.id.h_title);
			t_title.setText(hira.getH_title());

			WebView t_text = findViewById(R.id.h_text);

			t_text.getSettings().setSupportZoom(true);
			t_text.getSettings().setBuiltInZoomControls(true);
			t_text.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			t_text.setScrollbarFadingEnabled(true);


			if(hira.getH_text() != null && !hira.getH_text().isEmpty()) {
				h_text = hira.getH_text();
			}
			else {
				try {
					InputStream is = getAssets().open("files/" + hira.getId() + ".html");
					int size = is.available();

					// Read the entire asset into a local byte buffer.
					byte[] buffer = new byte[size];
					is.read(buffer);
					is.close();
					h_text = new String(buffer);
					hira.setH_text(h_text);
					fihiranaViewModel.updateHira(hira);
				} catch (Exception e) {
					Log.e(TAG, "onCreate: text file not accessible " + e.getMessage());
				}
			}

			t_text.loadDataWithBaseURL (null, h_text, "text/html", "UTF-8", null);

			btn_favorite_on.setOnClickListener(item -> {
				favoriteViewModel.removeFavorite(h_id);
				Toast.makeText(HiraItemActivity.this,
						R.string.favorite_removed,
						Toast.LENGTH_SHORT).show();

			});

			btn_favorite_off.setOnClickListener(item -> {
				favoriteViewModel.saveFavorite(hira);
				Toast.makeText(HiraItemActivity.this,
						R.string.favorite_saved, Toast.LENGTH_SHORT)
						.show();

			});

			btn_share.setOnClickListener(item -> {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				String toSend = "Lohateny: " + hira.getH_title() + "\n";
//					if(hira.getF_title().size() > 0)
//					{
//						toSend += "Fihirana: " + hira.getF_title().get(0) + " " + hira.getF_page().get(0) + "\n";
//					}
				toSend += "Rohy: https://katolika.org/fihirana/hira/show/" + hira.getId() + "\n" +
						"------------\n" ;

				toSend += hira.getH_text();
				toSend += "\n------------\nkatolika.org";


				sendIntent.putExtra(Intent.EXTRA_TEXT, toSend);
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
			});

		});

		favoriteViewModel.isFavorite(h_id).observe(this, isFavorite -> {
			if (isFavorite == 0) {
				btn_favorite_off.setVisibility(View.VISIBLE);
				btn_favorite_on.setVisibility(View.GONE);
			} else {
				btn_favorite_off.setVisibility(View.GONE);
				btn_favorite_on.setVisibility(View.VISIBLE);
			}
		});
	}
}
