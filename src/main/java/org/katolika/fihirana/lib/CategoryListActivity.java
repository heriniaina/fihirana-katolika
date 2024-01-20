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

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.adapters.SokajyAdapter;
import org.katolika.fihirana.lib.database.FihiranaViewModel;
import org.katolika.fihirana.lib.models.Sokajy;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryListActivity extends BaseActivity {

	private SokajyAdapter sokajyAdapter;
	private FihiranaViewModel fihiranaViewModel;

	ArrayList<HashMap<String, String>> fList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list);

		sokajyAdapter = new SokajyAdapter();

		RecyclerView recyclerView = findViewById(R.id.recyclerView);

		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(sokajyAdapter);

		fihiranaViewModel = new ViewModelProvider(this).get(FihiranaViewModel.class);
		fihiranaViewModel.getSokajyListWithCount().observe(this, sokajyList -> {
			sokajyAdapter.submitList(sokajyList);
		});

		sokajyAdapter.setOnRowClickListener(sokajy -> {
			Intent intent = new Intent(getApplicationContext(),
					CategoryItemActivity.class);
			intent.putExtra("org.katolika.fihirana.lib.S_ID", sokajy.getId());
			startActivity(intent);
		});

	}

}
