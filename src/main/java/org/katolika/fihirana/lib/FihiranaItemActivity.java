package org.katolika.fihirana.lib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.adapters.HiraAdapter;
import org.katolika.fihirana.lib.interfaces.ItemClickListener;
import org.katolika.fihirana.lib.interfaces.OnLoadMoreListener;
import org.katolika.fihirana.lib.models.Fihirana;
import org.katolika.fihirana.lib.models.Hira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FihiranaItemActivity extends BaseActivity {

	private ProgressDialog pDialog;

	String[] sqliteIds;

	ListView lv;

	DatabaseHelper db;

	Handler mainHandler = new Handler();
	int f_id;
	int f_page;
	int limit = 30;
	int start = 0;
	SharedPreferences prefs;

	RecyclerView recyclerView;
	HiraAdapter hiraAdapter;
	List<Hira> hiraList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fihirana_item);

		db = new DatabaseHelper(FihiranaItemActivity.this);
		prefs = getApplicationContext().getSharedPreferences("org.katolika.fihirana.lib", 0);


		Intent intent = getIntent();
		f_id = intent.getIntExtra("org.katolika.fihirana.lib.F_ID", 0);
		f_page = prefs.getInt("fihirana" + f_id, 0);

		Fihirana f = db.getFihiranaItem(f_id);

		// fill fihirana info
		TextView f_title = findViewById(R.id.f_title);
		f_title.setText(f.getName());

		TextView f_description = findViewById(R.id.f_description);
		f_description.setText(f.getDescription());

		EditText et = findViewById(R.id.txtPage);
		if(f_page > 0 ) et.append(String.valueOf(f_page));
		et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_GO) {
					doGoToPage(v);
					InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					in.hideSoftInputFromWindow(
							v.getApplicationWindowToken(),
							InputMethodManager.HIDE_IMPLICIT_ONLY);

					return true;
				}
				return false;
			}
		});
		InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(et.getApplicationWindowToken(), InputMethodManager.RESULT_UNCHANGED_HIDDEN);

		//
		recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);

		hiraList = new ArrayList<>();

		hiraAdapter = new HiraAdapter(hiraList, recyclerView);
		recyclerView.setAdapter(hiraAdapter);

		hiraAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				start = start + limit;
				LoadData runnable = new LoadData();
				new Thread(runnable).start();
			}
		});

		hiraAdapter.setOnClickListener(new ItemClickListener() {
			@Override
			public void onClick(View v, int position) {
				// Starting new intent
				Hira h = hiraList.get(position);
				if(h != null)
				{
					Intent in = new Intent(getApplicationContext(),
							HiraItemActivity.class);
					in.putExtra("org.katolika.fihirana.lib.H_ID", h.getId());
					in.putExtra("org.katolika.fihirana.lib.F_ID", f_id);
					startActivity(in);
				}

			}
		});


		LoadData runnable = new LoadData();
		new Thread(runnable).start();

	}

	private void init()
	{
		hiraList.clear();
		start = 0;
	}
	public void doGoToPage(View v) {
		EditText txtPage = (EditText) findViewById(R.id.txtPage);
		try{
			f_page = Integer.parseInt(txtPage.getText().toString());
		}
		catch (NumberFormatException e)
		{
			f_page = 0;
		}


		init();

		LoadData runnable = new LoadData();
		new Thread(runnable).start();
		//try to hide keyboard
		try {
			InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			in.hideSoftInputFromWindow(
					txtPage.getApplicationWindowToken(),
				InputMethodManager.HIDE_IMPLICIT_ONLY);
		
		}
		catch (Exception e)
		{
			Log.e("KEYBOARD", e.getMessage());
		}
	}

	class LoadData implements Runnable {

		@Override
		public void run() {
			if (f_page != 0) {
				hiraList.addAll(db.getHiraListOnPage(f_id, f_page, limit, start));
			} else {
				hiraList.addAll(db.getHiraList(f_id, limit, start));
			}
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("fihirana" + f_id, f_page);
			editor.apply();


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
					hideKeyboard(FihiranaItemActivity.this);
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
