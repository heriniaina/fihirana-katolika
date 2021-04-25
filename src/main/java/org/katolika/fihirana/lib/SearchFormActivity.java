package org.katolika.fihirana.lib;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchFormActivity extends BaseActivity {

	// Spinner element
	Spinner spinner;
	String search_from_category;
	DatabaseHelper db;
	private ProgressDialog pDialog;
	SharedPreferences prefs;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_form);

		ImageButton ib = (ImageButton) findViewById(R.id.search);
		ib.setVisibility(View.INVISIBLE);
		// Loading spinner data from database
		new loadSpinnerData().execute();

		prefs = getSharedPreferences("org.katolika.fihirana.lib", 0);



		EditText et =  findViewById(R.id.txt_from_title);

		et.append(prefs.getString("tadiavo", ""));

		et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					doSearch(v);
					return true;
				}
				return false;
			}
		});

	}

	public void doSearch(View view) {
		Intent intent = new Intent(this, SearchResultActivity.class);

		EditText txt_from_title = (EditText) findViewById(R.id.txt_from_title);
		String search_from_title = txt_from_title.getText().toString();

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("tadiavo", search_from_title);
		editor.apply();

		intent.putExtra("org.katolika.fihirana.lib.SEARCH_FROM_TITLE",
				search_from_title);

//		EditText txt_from_content = (EditText) findViewById(R.id.txt_from_content);
//		String search_from_content = txt_from_content.getText().toString();
//		intent.putExtra("org.katolika.fihirana.lib.SEARCH_FROM_CONTENT",
//				search_from_content);

		intent.putExtra(
				"org.katolika.fihirana.lib.SEARCH_FROM_CATEGORY",
				search_from_category);

		startActivity(intent);
	}

	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	}

	class MyData {
		String spinnerText;
		String value;

		public MyData(String spinnerText, String value) {
			this.spinnerText = spinnerText;
			this.value = value;
		}

		public String getSpinnerText() {
			return spinnerText;
		}

		public String getValue() {
			return value;
		}

		public String toString() {
			return spinnerText;
		}
	}

	/**
	 * Background Async Task to get RSS data from URL
	 * */
	class loadSpinnerData extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SearchFormActivity.this);
			pDialog.setMessage("Andraso kely ...");
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
					try
					{
						db = new DatabaseHelper(SearchFormActivity.this);


						Cursor c = db.getCategoryListNotEmpty();

						int i = 1;
						Spinner s = (Spinner)findViewById(R.id.spinner);
						final MyData items[] = new MyData[ c.getCount() + 1]; // misy an'ilay empty voalohany

						items[0] = new MyData("[Sokajy rehetra]", "");
						if (c.moveToFirst())
						{

							do {
								//satria ny row voalohany ihany no miseho
								items[i] = new MyData(c.getString(1), c.getString(0));
								i++;
							} while (c.moveToNext());
						}

						c.close();
						db.close();
						ArrayAdapter<MyData> adapter =
								new ArrayAdapter<MyData>(
										SearchFormActivity.this,
										android.R.layout.simple_spinner_item,
										items );
						adapter.setDropDownViewResource(
								android.R.layout.simple_spinner_dropdown_item);
						s.setAdapter(adapter);
						s.setOnItemSelectedListener(
								new AdapterView.OnItemSelectedListener()
								{
									public void onItemSelected(
											AdapterView<?> parent,
											View view,
											int position,
											long id)
									{
										MyData d = items[position];
										search_from_category = d.getValue();
									}

									public void onNothingSelected(AdapterView<?> parent)
									{

										// TODO nothing

									}
								}
						);
					}
					catch(Exception e)
					{
						Log.e("SearchForm l.160", e.toString());
					}
					finally {
						if (db != null) {
							db.close();
						}
					}
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
}
