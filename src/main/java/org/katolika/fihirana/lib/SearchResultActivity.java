package org.katolika.fihirana.lib;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.adapters.HiraAdapter;
import org.katolika.fihirana.lib.interfaces.ItemClickListener;
import org.katolika.fihirana.lib.models.Hira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchResultActivity extends BaseActivity {

	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> fList;
	String[] sqliteIds;
	List<Hira> hiraList;
	HiraAdapter hiraAdapter;
	ListView lv;
	RecyclerView recyclerView;
	DatabaseHelper db;
	Button btnLoadMore;
	int start = 0;
	int limit = 30;
	Handler mainHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search_result);

		db = new DatabaseHelper(this);
		recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		hiraList = new ArrayList<>();
		hiraAdapter = new HiraAdapter(hiraList, recyclerView);

		hiraAdapter.setOnClickListener(new ItemClickListener() {
			@Override
			public void onClick(View v, int position) {
				Hira hira = hiraList.get(position);

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						HiraItemActivity.class);
				// passing sqlite row id
				in.putExtra("org.katolika.fihirana.lib.H_ID", hira.getId());
				startActivity(in);
			}
		});

		recyclerView.setAdapter(hiraAdapter);

		LoadData runnable = new LoadData(hiraList);
		new Thread(runnable).start();

	}


	/*
	 * public boolean onCreateOptionsMenu(Menu menu) { // Inflate the menu items
	 * for use in the action bar MenuInflater inflater = getMenuInflater();
	 * inflater.inflate(R.menu.main_activity_actions, menu); return
	 * super.onCreateOptionsMenu(menu); }
	 */


	class LoadData implements Runnable{
		List<Hira> hiraList;

		LoadData(List<Hira> hl){
			hiraList = hl;
		}
		@Override
		public void run() {
			Intent intent = getIntent();
			String search_from_title = "";
			String search_from_content = "";
			String search_from_category = "";

			if (intent
					.hasExtra("org.katolika.fihirana.lib.SEARCH_FROM_TITLE")) {
				search_from_title = intent
						.getStringExtra("org.katolika.fihirana.lib.SEARCH_FROM_TITLE");

			}
			if (intent
					.hasExtra("org.katolika.fihirana.lib.SEARCH_FROM_CONTENT")) {
				search_from_content = intent
						.getStringExtra("org.katolika.fihirana.lib.SEARCH_FROM_CONTENT");

			}
			if (intent
					.hasExtra("org.katolika.fihirana.lib.SEARCH_FROM_CATEGORY")) {
				search_from_category = intent
						.getStringExtra("org.katolika.fihirana.lib.SEARCH_FROM_CATEGORY");

			}

			List<Hira> tmpHiraList = db.getHiraFromSearch(search_from_title,
					search_from_content, search_from_category, limit,
					start);
			if(tmpHiraList.size() > 0)
			{
				hiraList.addAll(tmpHiraList);
			}


			mainHandler.post(new Runnable() {
				@Override
				public void run() {
					hiraAdapter.notifyDataSetChanged();
					TextView tv = (TextView) findViewById(R.id.error_message);
					tv.setVisibility(View.VISIBLE);
					if(hiraList.size() == 0){

						tv.setText("Tsy nisy hira hita");
					}
					else
					{
						tv.setVisibility(View.GONE);
					}
					hiraAdapter.setLoaded();
				}
			});
		}
	}

	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	}
}
