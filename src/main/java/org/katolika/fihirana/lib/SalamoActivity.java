package org.katolika.fihirana.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.adapters.SalamoAdapter;
import org.katolika.fihirana.lib.interfaces.ItemClickListener;
import org.katolika.fihirana.lib.interfaces.OnLoadMoreListener;
import org.katolika.fihirana.lib.models.Hira;

import java.util.ArrayList;
import java.util.List;

public class SalamoActivity extends BaseActivity {


	List<Hira> hiraList;

	DatabaseHelper db;
	SalamoAdapter salamoAdapter;
	RecyclerView recyclerView;
	SharedPreferences prefs;
	Handler mainHandler = new Handler();
	int faha = 0;
	int start = 0;
	int limit = 30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_salamo);
		db = new DatabaseHelper(this);

		final EditText editText = findViewById(R.id.editText);
		prefs = getSharedPreferences("org.katolika.fihirana.lib", 0);
		faha = prefs.getInt("salamo", 0);

		if(faha > 0 ) editText.append(String.valueOf(faha));
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				if(i == EditorInfo.IME_ACTION_GO)
				{
					if(!editText.getText().toString().equals(""))
					{
						faha = Integer.parseInt(editText.getText().toString());
					}

					doFilter();
					return true;
				}
				return false;
			}
		});

		Button button = findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!editText.getText().toString().equals(""))
				{
					faha = Integer.parseInt(editText.getText().toString());
				}

				doFilter();
			}
		});
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					faha = 0;
					doFilter();
				}
			}
		});
		hiraList = new ArrayList<>();

		recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		salamoAdapter = new SalamoAdapter(hiraList, recyclerView);
		recyclerView.setAdapter(salamoAdapter);

		salamoAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				start = start + limit;
				LoadData runnable = new LoadData();
				new Thread(runnable).start();
			}
		});

		salamoAdapter.setOnClickListener(new ItemClickListener() {
			@Override
			public void onClick(View v, int position) {
				Hira hira = hiraList.get(position);

				Intent in = new Intent(getApplicationContext(),
						HiraItemActivity.class);
				// passing sqlite row id
				in.putExtra("org.katolika.fihirana.lib.H_ID", hira.getId());
				startActivity(in);
			}
		});





		LoadData runnable = new LoadData();
		new Thread(runnable).start();


	}

	private void doFilter()
	{
		hiraList.clear();
		start = 0;
		LoadData runnable = new LoadData();
		new Thread(runnable).start();
	}

	class LoadData implements Runnable {

		@Override
		public void run() {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("salamo", faha );
			editor.apply();

			hiraList.addAll(db.getSalamoList(faha, limit,start));
			mainHandler.post(new Runnable() {
				@Override
				public void run() {
					salamoAdapter.notifyDataSetChanged();
					TextView tv = (TextView) findViewById(R.id.error_message);
					tv.setVisibility(View.VISIBLE);
					if(hiraList.size() == 0){

						tv.setText("Tsy nisy hira hita");
					}
					else
					{
						tv.setVisibility(View.GONE);
					}
					salamoAdapter.setLoaded();
					hideKeyboard(SalamoActivity.this);
				}
			});
		}
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



	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	}

	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		//Find the currently focused view, so we can grab the correct window token from it.
		View view = activity.getCurrentFocus();
		//If no view currently has focus, create a new one, just so we can grab a window token from it
		if (view == null) {
			view = new View(activity);
		}
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
