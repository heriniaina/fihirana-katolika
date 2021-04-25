package org.katolika.fihirana.lib;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryListActivity extends BaseActivity {

	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> fList;
	String[] sqliteIds;
	DatabaseHelper db;

	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list);

		fList = new ArrayList<HashMap<String, String>>();

		new loadCategoryList().execute();

		lv = (ListView) findViewById(R.id.list);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem

				String s_id = ((TextView) view.findViewById(R.id.s_id))
						.getText().toString();
				// Starting new intent

                Intent in = new Intent(getApplicationContext(),
						CategoryItemActivity.class);
//				// passing sqlite row id
				in.putExtra("org.katolika.fihirana.lib.S_ID", s_id);
				startActivity(in);
			}
		});
	}


	/**
	 * Background Async Task to get RSS data from URL
	 * */
	class loadCategoryList extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CategoryListActivity.this);
			pDialog.setMessage("Mitady ny sokajy ...");
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
					db = new DatabaseHelper(CategoryListActivity.this);

					Cursor c = db.getCategoryListNotEmpty();

					sqliteIds = new String[c.getCount()];

					int i = 0;
					if (c.moveToFirst()) {

						do {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("s_id", c.getString(0));
							map.put("s_title", c.getString(1));
							map.put("s_description", c.getString(2));
							map.put("cnt", "Hira: " + c.getString(3));

							fList.add(map);
							sqliteIds[i] = c.getString(0);
							i++;
						} while (c.moveToNext());
					}
					c.close();
					ListAdapter adapter = new SimpleAdapter(
							CategoryListActivity.this, fList,
							R.layout.row_category, new String[] { "s_id",
									"s_title", "s_description", "cnt" },
							new int[] { R.id.s_id, R.id.s_title,
									R.id.s_description, R.id.cnt });
					// updating listview

					lv.setAdapter(adapter);
					// registerForContextMenu(lv);

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
		}
	}

	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	}
}
