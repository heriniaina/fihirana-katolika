package org.katolika.fihirana.lib;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.katolika.fihirana.lib.models.Hira;

import java.io.InputStream;

public class HiraItemActivity extends BaseActivity  {

	DatabaseHelper db;
    FavoriteDb fb;
	int isFavorite = 0;
	int h_id = 0;
	int f_id = 0;

	String h_text;
	ImageButton btn_favorite_on;
	ImageButton btn_favorite_off;
	ImageButton btn_share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hira_item);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		db = new DatabaseHelper(this);
        fb = new FavoriteDb(this);

		/*
        LinearLayout h_view = (LinearLayout) findViewById(R.id.h_view);
		
		OnSwipeTouchListener listener = new OnSwipeTouchListener() {

			@Override
		    public void onSwipeRight() {
		        Toast.makeText(HiraItemActivity.this, "right", Toast.LENGTH_SHORT).show();
		    }
			
			@Override
		    public void onSwipeLeft() {
		        Toast.makeText(HiraItemActivity.this, "left", Toast.LENGTH_SHORT).show();
		    }
		    
        };
        h_view.setOnTouchListener(listener);
        */
		getHira();
		
				

	}

	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
        if(fb != null) {
            fb.close();
        }
	}
	
	private void getHira()
	{
		Intent intent = getIntent();
		h_id = intent.getIntExtra("org.katolika.fihirana.lib.H_ID", 0);
		f_id = intent.getIntExtra("org.katolika.fihirana.lib.F_ID", 0);
		
		btn_favorite_on = findViewById(R.id.favorite_on);
		btn_favorite_off = findViewById(R.id.favorite_off);
		btn_share = findViewById(R.id.share);
		
		

		
		
		isFavorite = fb.isFavorite(h_id);

		
		final Hira hira = db.getHiraItem(h_id);
		

		if (hira != null) {

			btn_share.setVisibility(View.VISIBLE);
			
			// fill hira info
			TextView t_title = findViewById(R.id.h_title);
			t_title.setText(hira.getH_title());



            WebView t_text = findViewById(R.id.h_text);

            t_text.getSettings().setSupportZoom(true);
            t_text.getSettings().setBuiltInZoomControls(true);
            t_text.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            t_text.setScrollbarFadingEnabled(true);
                try {
                    t_text.loadUrl("file:///android_asset/files/" + h_id + ".html");
                } catch (Exception e) {
                    // Should never happen!
                    t_text.loadData("Tsy hita ny hira, miala tsiny.", null, null);
                }



			// WebView h_text = (WebView) findViewById(R.id.h_text);
			// h_text.getSettings().setBuiltInZoomControls(true);
			// h_text.setInitialScale(0);
			// h_text.getSettings().setUseWideViewPort(false);
			// h_text.loadDataWithBaseURL("file:///android_asset/", t,
			// "text/html", "utf-8", null);

		}


		if (isFavorite == 0) {
			btn_favorite_off.setVisibility(View.VISIBLE);
			btn_favorite_on.setVisibility(View.GONE);
		} else {
			btn_favorite_off.setVisibility(View.GONE);
			btn_favorite_on.setVisibility(View.VISIBLE);
		}

		btn_favorite_on.setOnClickListener(new OnClickListener() {
			public void onClick(View item) {
				btn_favorite_off.setVisibility(View.VISIBLE);
				btn_favorite_on.setVisibility(View.GONE);
				fb.removeFavorite(h_id);
				Toast.makeText(HiraItemActivity.this,
						"Nesorina tao amin'ny hira tianao ny hira.",
						Toast.LENGTH_LONG).show();
				isFavorite = 0;
			}
		});

		btn_favorite_off.setOnClickListener(new OnClickListener() {
			public void onClick(View item) {
				if(hira != null){
					btn_favorite_off.setVisibility(View.GONE);
					btn_favorite_on.setVisibility(View.VISIBLE);
					fb.saveFavorite(hira);
					Toast.makeText(HiraItemActivity.this,
							"Voatahiry ho tianao ny hira.", Toast.LENGTH_LONG)
							.show();
					isFavorite = 1;
				}


			}
		});

		btn_share.setOnClickListener( new OnClickListener(){
			public void onClick(View item) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				String toSend = "Lohateny: " + hira.getH_title() + "\n";
				if(hira.getF_title().size() > 0)
				{
					toSend += "Fihirana: " + hira.getF_title().get(0) + " " + hira.getF_page().get(0) + "\n";
				}
                toSend += "Rohy: http://katolika.org/fihirana/hira/show/" + hira.getId() + "\n" +
                        "------------\n" ;
                try {
                    InputStream is = getAssets().open("files/" + hira.getId() + ".html");

                    // We guarantee that the available method returns the total
                    // size of the asset... of course, this does mean that a single
                    // asset can't be more than 2 gigs.
                    int size = is.available();

                    // Read the entire asset into a local byte buffer.
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();

                    // Convert the buffer into a string.
                    h_text = new String(buffer);


                    toSend += Html.fromHtml(h_text).toString() +
                            "\n------------\nkatolika.org";

                }
                catch (Exception e) {
                    e.printStackTrace();
                }


				sendIntent.putExtra(Intent.EXTRA_TEXT, toSend);
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
			}
		});

	}
}
