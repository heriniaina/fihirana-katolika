package org.katolika.fihirana.lib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    int itemId = item.getItemId();
		if (itemId == R.id.about) {
			showAboutActivity(this);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	protected void showAboutActivity(Context context) {
		Intent infoView = new Intent(context, AboutActivity.class);
		startActivity(infoView);
	}
	
	/*
	protected void showPrefenceActivity(Context context) {
		Intent prefView = new Intent(context, AppPreferencesActivity.class);
		startActivity(prefView);
	}
	
	public void openBookmarksActivity(Context context) {
		boolean groupBy = Preference.getInstance(this).isBookmarksGroupByDate();
		Intent i = new Intent(context, groupBy ? BookmarksExpandableListActivity.class : BookmarksListActivity.class);
    	startActivityForResult(i, OPEN_BOOKMARKS_ACTIVITY);
	}
	*/

}
