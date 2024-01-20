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

import org.katolika.fihirana.lib.adapters.HiraAdapter;
import org.katolika.fihirana.lib.database.FihiranaViewModel;
import org.katolika.fihirana.lib.models.Hira;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryItemActivity extends BaseActivity {
	int start = 0;
    int limit = 50;
	ListView lv;
	FihiranaViewModel fihiranaViewModel;
	HiraAdapter hiraAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_item);


		int s_id = getIntent().getIntExtra("org.katolika.fihirana.lib.S_ID", 0);
		hiraAdapter = new HiraAdapter();

		RecyclerView recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(hiraAdapter);

		fihiranaViewModel = new ViewModelProvider(this).get(FihiranaViewModel.class);

		hiraAdapter.setOnClickListener(hira -> {
			Intent intent = new Intent(this, HiraItemActivity.class);
			intent.putExtra("org.katolika.fihirana.lib.H_ID", hira.getId());
			startActivity(intent);
		});
		fihiranaViewModel.getHiraByCategory(s_id, start, limit).observe(this, hiraList -> {
			hiraAdapter.submitList(hiraList);
		});
	}
}
