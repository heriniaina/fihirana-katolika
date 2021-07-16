package org.katolika.fihirana.lib;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.adapters.SearchResultAdapter;
import org.katolika.fihirana.lib.database.FihiranaViewModel;

public class SearchResultActivity extends BaseActivity {

	private static final String TAG = "SearchResultActivityTAG";
	SearchResultAdapter searchResultAdapter;

	RecyclerView recyclerView;
	int start = 0;
	final int INITIAL_LIMIT = 30;
	int limit;
	Handler mainHandler = new Handler();
	String search_from_title = "";
	String search_from_content = "";
	int search_from_category = 0;
	FihiranaViewModel fihiranaViewModel;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search_result);

		limit = INITIAL_LIMIT;

		recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		searchResultAdapter = new SearchResultAdapter();

		searchResultAdapter.setOnClickListener(hira -> {
			Intent intent = new Intent(this, HiraItemActivity.class);
			intent.putExtra("org.katolika.fihirana.lib.H_ID", hira.getId());
			startActivity(intent);
		});

		recyclerView.setAdapter(searchResultAdapter);

		fihiranaViewModel = new ViewModelProvider(this).get(FihiranaViewModel.class);



		Intent intent = getIntent();
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
					.getIntExtra("org.katolika.fihirana.lib.SEARCH_FROM_CATEGORY", 0);

		}

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
				@Override
				public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
					super.onScrolled(recyclerView, dx, dy);
					Log.d(TAG, "onScrolled: count " + searchResultAdapter.getItemCount());
					if((linearLayoutManager.findLastVisibleItemPosition() == linearLayoutManager.getItemCount() -1)
							&& (linearLayoutManager.getItemCount() > linearLayoutManager.findLastCompletelyVisibleItemPosition())
					) {
							limit += INITIAL_LIMIT;

						Log.d(TAG, "onScrolled: " + limit);
						refreshRecyclerView();
					}
				}

			});
        }

		refreshRecyclerView();

	}

	private void refreshRecyclerView() {
		fihiranaViewModel.getHiraFromSearch(limit, search_from_title, search_from_category, search_from_content).observe(this, hiraList -> {
			Log.d(TAG, "refreshRecyclerView: count " + searchResultAdapter.getItemCount());
			TextView tv = (TextView) findViewById(R.id.error_message);

			if(hiraList == null || hiraList.size() == 0){
				tv.setVisibility(View.VISIBLE);
				tv.setText(R.string.str_hira_not_found);
			}
			else
			{
				tv.setVisibility(View.GONE);
				searchResultAdapter.submitList(hiraList, () -> {

					searchResultAdapter.setLoading(false);
				});
			}
		});
	}



}
