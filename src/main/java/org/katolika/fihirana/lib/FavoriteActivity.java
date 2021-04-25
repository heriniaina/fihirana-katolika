package org.katolika.fihirana.lib;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteActivity extends BaseActivity {

	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> fList;
	String[] sqliteIds;

	ListView lv;
	FavoriteDb db;
	Button btnLoadMore;
	int start = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_favorite);

		fList = new ArrayList<HashMap<String, String>>();

		new loadFavoriteList().execute();

		lv = (ListView) findViewById(R.id.list);
		
		// Creating a button - Load More
		btnLoadMore = new Button(this);
		btnLoadMore.setText(R.string.list_load_more);
		btnLoadMore.setBackgroundResource(R.drawable.btn_blue);
		btnLoadMore.setTextAppearance(this, R.style.ButtonText);
		btnLoadMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new loadFavoriteList().execute();

			}
		});

		lv.addFooterView(btnLoadMore);
		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				int h_id = Integer.parseInt(((TextView) view.findViewById(R.id.h_id)).getText().toString());
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						HiraItemActivity.class);
				// passing sqlite row id
				in.putExtra("org.katolika.fihirana.lib.H_ID", h_id);
				startActivity(in);
			}
		});
	}


	public void openSearch() {

		Intent intent = new Intent(this, SearchFormActivity.class);
		startActivity(intent);

	}

	public void openSettings() {
	}

	/*
	 * public boolean onCreateOptionsMenu(Menu menu) { // Inflate the menu items
	 * for use in the action bar MenuInflater inflater = getMenuInflater();
	 * inflater.inflate(R.menu.main_activity_actions, menu); return
	 * super.onCreateOptionsMenu(menu); }
	 */



	/**
	 * Background Async Task to get RSS data from URL
	 * */
	class loadFavoriteList extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		int limit = 50;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(FavoriteActivity.this);
			pDialog.setMessage("Mitady hira ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting all stored website from SQLite
		 * */

		@Override
		protected String doInBackground(String... args) {
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					db = new FavoriteDb(
							FavoriteActivity.this);

					
					Cursor c = db.getFavoriteList();

					int total = c.getCount();
					sqliteIds = new String[total];

					int i = 0;
					if (c.moveToFirst()) {

						// fill search result info
						TextView description = (TextView) findViewById(R.id.description);
						description.setText("Hira hita: " + total);
						do {

							HashMap<String, String> map = new HashMap<String, String>();
							map.put("h_id", c.getString(0));
							map.put("h_title", c.getString(1));
							if(c.getString(3) != null)
							{
								map.put("f_page",
									c.getString(2) + ", pejy: "
											+ c.getString(3));
							}
							else
							{
								map.put("f_page", "");
							}

							fList.add(map);
							sqliteIds[i] = c.getString(0);
							i++;
						} while (c.moveToNext());
						c.close();

						
						// registerForContextMenu(lv);
						btnLoadMore.setText(R.string.list_load_more);
					} else {
						btnLoadMore.setText(R.string.list_no_more_song);
					}
					
					ListAdapter adapter = new SimpleAdapter(
							FavoriteActivity.this, fList,
							R.layout.row_hira, new String[] { "h_id",
									"h_title", "f_page" }, new int[] {
									R.id.h_id, R.id.h_title, R.id.f_page });
					// updating listview

					
					
					// get listview current position - used to maintain scroll
					// position
					int currentPosition = lv.getFirstVisiblePosition();
					lv.setAdapter(adapter);
					if (start == 0) {
						lv.setSelectionFromTop(currentPosition, 0);

					} else {
						lv.setSelectionFromTop(currentPosition + 1, 0);
					}
					start += limit;
				}
			});
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String args) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			if (db != null) {
				db.close();
			}
		}
	}
	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	}
}
