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

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.adapters.FavoriteAdapter;
import org.katolika.fihirana.lib.database.FavoriteViewModel;

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

	FavoriteViewModel favoriteViewModel;
	FavoriteAdapter favoriteAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);

		favoriteAdapter = new FavoriteAdapter();
		RecyclerView recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(favoriteAdapter);

		favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

		favoriteViewModel.getFavoriteList().observe(this, favoriteList -> {
			favoriteAdapter.submitList(favoriteList);
		});

		favoriteAdapter.setOnRowClickListener(hira -> {
			Intent intent = new Intent(getApplicationContext(),
					HiraItemActivity.class);
			intent.putExtra("org.katolika.fihirana.lib.H_ID", hira.getH_id());
			startActivity(intent);
		});
	}
}
