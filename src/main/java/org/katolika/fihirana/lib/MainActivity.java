package org.katolika.fihirana.lib;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

//import org.apache.http.util.ByteArrayBuffer;

public class MainActivity extends BaseActivity {
	public final static String SEARCH_FROM_TITLE = "org.katolika.fihirana.lib.SEARCH_FROM_TITLE";

    private static final String TAG = "MainActivityTag";
    private static final int curVersion = 23;

	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /*
         * check database
         */
        
       
        setContentView(R.layout.activity_main);

        //ImageButton btn_search = (ImageButton) findViewById(R.id.search);
		//btn_search.setVisibility(View.INVISIBLE);
        
		//ImageButton btn_home = (ImageButton) findViewById(R.id.home);
		//btn_home.setVisibility(View.GONE);

        ConstraintLayout layout = findViewById(R.id.home_info_row);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAboutActivity();
            }
        });
		
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("FKATF", 0);
        long lastUpdateTime =  prefs.getLong("lastUpdateTime", 0);
        Log.v("PREF", "Saved LastUpdatetime = " + String.valueOf(lastUpdateTime));
		/* Should Activity Check for Updates Now? */
        /* monthly check */
        long newUpdateTime = System.currentTimeMillis();
        //long irayvolana = (30 * 24 * 60 * 60 * 1000L);
        long irayvolana = (60 * 1000L);
        
        if ((lastUpdateTime + irayvolana) < newUpdateTime) {

        	//Log.v("PREF", "Diff = " + ((lastUpdateTime + (30 * 24 * 60 * 60 * 1000)) - newUpdateTime));
            /* Save current timestamp for next Check*/
        	Log.v("PREF", "New LastUpdatetime = " + String.valueOf(newUpdateTime));
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("lastUpdateTime", newUpdateTime);
            editor.apply();

            /* Start Update */            
            checkUpdate();
        }
        long telovolana = (90 * 24 * 60 * 60 * 1000L);
        if ((lastUpdateTime + telovolana) < System.currentTimeMillis()) {

                        
            //showPub.start();
        	showPub();
        }
    }
    
    private void showPub()
    {
    	try {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        	
            //.setIcon(R.drawable.icon)
            builder.setTitle("Sorona Masina")
            .setMessage("Vao nivoaka ny application SORONA MASINA hahitanao ny vakiteny isanandro. Tianao hojerena?")
            .setPositiveButton("Eny", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                            /* User clicked OK so do some stuff */
                    	Intent intent = new Intent(Intent.ACTION_VIEW);
                    	intent.setData(Uri.parse("market://details?id=org.katolika.soronamasina"));
                    	startActivity(intent);
                    }
            })
            .setNegativeButton("Tsy izao", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                            /* User clicked Cancel */
                    }
            })
            .show();
		}
		catch(Exception e)
		{
			Log.e("error", e.toString());
		}
    }

    private void checkUpdate()
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://katolika.org/media/org.katolika.fihirana/version.txt";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        // Display the first 10 characters of the response string.

                        String s = response.trim();

                        int newVersion = Integer.valueOf(s);

                        Log.d(TAG, " newVersion " + newVersion);




                        /* Is a higher version than the current already out? */
                        if (newVersion > curVersion) {
                            /* Post a Handler for the UI to pick up and open the Dialog */
                            showUpdate();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    

   
    public void displayFihiranaActivity(View view)
    {
    	startActivity(new Intent(this, FihiranaActivity.class));
    }
    
    public void displayTadiavoActivity(View view)
    {
    	startActivity(new Intent(this, SearchFormActivity.class));
    }
    
    public void displaySokajyActivity(View view)
    {
    	startActivity(new Intent(this, CategoryListActivity.class));
    }
    
    public void displayUpdateActivity(View view)
    {
    	//startActivity(new Intent(this, DatabaseUpgrade.class));
    }
    
    public void displayAboutActivity()
    {
    	Intent infoView = new Intent(this, AboutActivity.class);
		startActivity(infoView);
    }
    public void displaySalamoActivity(View view)
    {
    	startActivity(new Intent(this, SalamoActivity.class));
    }
    
    public void displayFavoriteActivity(View view)
    {
    	startActivity(new Intent(this, FavoriteActivity.class));
    }
    /* This Runnable creates a Dialog and asks the user to open the Market */ 
    private void showUpdate ()
    {
        TextView txtInfoTitle = findViewById(R.id.home_info_list_title);
        txtInfoTitle.setTextColor(Color.RED);
        TextView txtInfoDesc = findViewById(R.id.txtInfoDesc);
        RelativeLayout layout = findViewById(R.id.home_info_row);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=name:org.katolika.fihirana.lib"));
                startActivity(intent);
            }
        });

            //.setIcon(R.drawable.icon)
        txtInfoTitle.setText(R.string.activity_main_new_version_title);
        txtInfoDesc.setText(R.string.activity_main_new_version_description);


    }
    

}
