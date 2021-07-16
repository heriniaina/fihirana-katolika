package org.katolika.fihirana.lib;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.adapters.SalamoAdapter;
import org.katolika.fihirana.lib.database.FihiranaViewModel;
import org.katolika.fihirana.lib.models.Hira;
import org.katolika.fihirana.lib.models.Salamo;

import java.util.List;

public class SalamoActivity extends BaseActivity {


	FihiranaViewModel fihiranaViewModel;
	SalamoAdapter salamoAdapter;
	RecyclerView recyclerView;
	SharedPreferences prefs;

	int faha = 0;
	int start = 0;
	int limit = 30;
	TextView txtError;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_salamo);

		fihiranaViewModel = new ViewModelProvider(this).get(FihiranaViewModel.class);

		final EditText editText = findViewById(R.id.editText);
		prefs = getSharedPreferences("org.katolika.fihirana.lib", 0);
		faha = prefs.getInt("salamo", 0);

		if(faha > 0 ) editText.append(String.valueOf(faha));
		editText.setOnEditorActionListener((textView, i, keyEvent) -> {
			if(i == EditorInfo.IME_ACTION_GO)
			{
				if(!editText.getText().toString().equals(""))
				{
					faha = Integer.parseInt(editText.getText().toString());
					doFilter();
					return true;
				}

			}
			return false;
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
				} else {
					faha = Integer.parseInt(editText.getText().toString());
				}
				doFilter();
			}
		});

		recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		salamoAdapter = new SalamoAdapter();
		recyclerView.setAdapter(salamoAdapter);


		salamoAdapter.setOnRowClickListener(new SalamoAdapter.OnRowClickListener() {
			@Override
			public void onClick(Salamo salamo) {

				Intent in = new Intent(getApplicationContext(),
						HiraItemActivity.class);
				// passing sqlite row id
				in.putExtra("org.katolika.fihirana.lib.H_ID", salamo.getH_id());
				startActivity(in);
			}
		});

		doFilter();

	}

	private void doFilter()
	{
		txtError = findViewById(R.id.error_message);
		LiveData<List<Salamo>> liveData;
		if(faha > 0) {
			liveData = fihiranaViewModel.getSalamoList(faha);
		} else {
			liveData = fihiranaViewModel.getSalamoList();
		}
		liveData.observe(this, salamoList -> {

			if(salamoList != null && salamoList.size() > 0) {

				txtError.setVisibility(View.GONE);

			}
			else
			{

				txtError.setVisibility(View.VISIBLE);
				txtError.setText(R.string.str_hira_not_found);
			}
			salamoAdapter.submitList(salamoList, () -> {
				recyclerView.scrollToPosition(0);
			});
		});

	}



}
