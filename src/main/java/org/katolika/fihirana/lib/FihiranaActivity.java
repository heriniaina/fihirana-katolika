package org.katolika.fihirana.lib;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.adapters.FihiranaAdapter;
import org.katolika.fihirana.lib.interfaces.ItemClickListener;
import org.katolika.fihirana.lib.models.Fihirana;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FihiranaActivity extends BaseActivity {

	private ProgressDialog pDialog;
	List<Fihirana> fList;
	String[] sqliteIds;
	DatabaseHelper db;
	
    public static String TAG_ID = "f_id";
    public static String TAG_TITLE = "f_title";
    public static String TAG_DESC = "f_description";
    public static String TAG_CNT = "cnt";
    private static final String TAG = "FihiranaActivity";



    RecyclerView recyclerView;
    FihiranaAdapter fihiranaAdapter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fihirana);
		
		fList = new ArrayList<>();
		recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);


		recyclerView.setLayoutManager(new LinearLayoutManager(this));


		fihiranaAdapter = new FihiranaAdapter(fList);
		fihiranaAdapter.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Fihirana fihirana = fList.get(position);

                int f_id = fihirana.getId();
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), FihiranaItemActivity.class);
                // passing sqlite row id
                in.putExtra("org.katolika.fihirana.lib.F_ID", f_id);
                startActivity(in);
            }
        });

        recyclerView.setAdapter(fihiranaAdapter);



		
		new loadFihiranaList().execute();
		

	}

	

	
	/**
     * Background Async Task to get RSS data from URL
     * */
    public class loadFihiranaList extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(
                    FihiranaActivity.this);
            pDialog.setMessage("Loading websites ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting all stored website from SQLite
         * */
        
        @Override
        protected String doInBackground(String... args) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                	db = new DatabaseHelper(FihiranaActivity.this);  

	          		//raha vao assignement ito fa tsy addall dia tsy miseho
                	fList.addAll(db.getFihiranaList());
	          		fihiranaAdapter.notifyDataSetChanged();
                }
            });
            return null;
        }
        
 /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String args) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
        }
    }
	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	}
}
