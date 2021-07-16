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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.adapters.HiraAdapter;
import org.katolika.fihirana.lib.database.FihiranaViewModel;
import org.katolika.fihirana.lib.interfaces.ItemClickListener;
import org.katolika.fihirana.lib.interfaces.OnLoadMoreListener;
import org.katolika.fihirana.lib.models.Fihirana;
import org.katolika.fihirana.lib.models.Hira;
import org.katolika.fihirana.lib.models.HiraInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FihiranaItemActivity extends BaseActivity {

	private static final String TAG = "FihiranaItemActivityTAG";

	private FihiranaViewModel fihiranaViewModel;

	int f_id;
	int f_page;
	int limit = 30;
	int start = 0;
	SharedPreferences prefs;

	RecyclerView recyclerView;
	HiraAdapter hiraAdapter;
	EditText txtPage;
	TextView txtMessage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fihirana_item);

		//binding
		TextView f_title = findViewById(R.id.f_title);
		TextView f_description = findViewById(R.id.f_description);
		txtPage = findViewById(R.id.txtPage);
		txtMessage = findViewById(R.id.error_message);



		prefs = getApplicationContext().getSharedPreferences("org.katolika.fihirana.lib", 0);


		Intent intent = getIntent();
		f_id = intent.getIntExtra("org.katolika.fihirana.lib.F_ID", 0);
		f_page = prefs.getInt("fihirana" + f_id, 0);

		fihiranaViewModel = new ViewModelProvider(this).get(FihiranaViewModel.class);

		fihiranaViewModel.getFihirana(f_id).observe(this, fihirana -> {
			f_title.setText(fihirana.getF_title());
			f_description.setText(fihirana.getF_description());
		});



		try{
			f_page = Integer.parseInt(txtPage.getText().toString());
		}
		catch (NumberFormatException e)
		{
			f_page = 0;
		}

		txtPage.setOnEditorActionListener((textView, actionId, keyEvent) -> {
			if(actionId == EditorInfo.IME_ACTION_GO) {
				doFilter();
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
				return true;
			}
			return false;
		});

		txtPage.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					f_page = 0;
				} else {
					f_page = Integer.parseInt(txtPage.getText().toString());
				}
				doFilter();
			}
		});
		InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(txtPage.getApplicationWindowToken(), InputMethodManager.RESULT_UNCHANGED_HIDDEN);

		//
		recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);

		hiraAdapter = new HiraAdapter();
		recyclerView.setAdapter(hiraAdapter);

		hiraAdapter.setOnClickListener(hira -> {
			// Starting new intent
			if(hira != null)
			{
				Intent in1 = new Intent(getApplicationContext(),
						HiraItemActivity.class);
				in1.putExtra("org.katolika.fihirana.lib.H_ID", hira.getId());
				in1.putExtra("org.katolika.fihirana.lib.F_ID", f_id);
				startActivity(in1);
			}

		});


		doFilter();

	}

	private void doFilter() {


		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("fihirana" + f_id, f_page);
		editor.apply();
		Log.d(TAG, "refreshRecycleView: page " + f_page);

		fihiranaViewModel.getHiraListByFihiranaId(f_id, start, limit, f_page).observe(this, hiraList -> {

			if(null == hiraList || hiraList.size() == 0){
				txtMessage.setVisibility(View.VISIBLE);
				txtMessage.setText(R.string.str_hira_not_found);
			}
			else
			{
				txtMessage.setVisibility(View.GONE);
			}

			hiraAdapter.submitList(hiraList, () -> {
				recyclerView.scrollToPosition(0);
			});


		});
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
