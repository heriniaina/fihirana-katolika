package org.katolika.fihirana.lib;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.adapters.FihiranaAdapter;
import org.katolika.fihirana.lib.database.FihiranaViewModel;

public class FihiranaActivity extends BaseActivity {


	FihiranaViewModel fihiranaViewModel;
	
    public static String TAG_ID = "f_id";
    public static String TAG_TITLE = "f_title";
    public static String TAG_DESC = "f_description";
    public static String TAG_CNT = "cnt";
    private static final String TAG = "FihiranaActivity";



    RecyclerView recyclerView;
    FihiranaAdapter fihiranaAdapter;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fihirana);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        fihiranaAdapter = new FihiranaAdapter();
        fihiranaAdapter.setClickListener((fihirana) -> {
            int f_id = fihirana.getId();
            // Starting new intent
            Intent in = new Intent(getApplicationContext(), FihiranaItemActivity.class);
            // passing sqlite row id
            in.putExtra("org.katolika.fihirana.lib.F_ID", f_id);
            startActivity(in);
        });

        recyclerView.setAdapter(fihiranaAdapter);

        fihiranaViewModel = new ViewModelProvider(this).get(FihiranaViewModel.class);
        fihiranaViewModel.getFihiranaList().observe(this, fihiranaList -> {
            Log.d(TAG, "onCreate: rowcount " + fihiranaList.size());
            fihiranaAdapter.submitList(fihiranaList);
        });

    }
}
