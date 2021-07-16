package org.katolika.fihirana.lib;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import org.katolika.fihirana.lib.models.Fihirana;
import org.katolika.fihirana.lib.models.Hira;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "fihirana";
	private static final int DATABASE_VERSION = 2019102801;
	private SQLiteDatabase db;
	private static final String TAG = "DatabaseHelper";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		// you can use an alternate constructor to specify a database location
		// (such as a folder on the sd card)
		// you must ensure that this folder is available and you have permission
		// to write to it
		// super(context, DATABASE_NAME,
		// context.getExternalFilesDir(null).getAbsolutePath(), null,
		// DATABASE_VERSION);
		setForcedUpgradeVersion(2019102801);
	}





	public int getHiraCount() {
		// TODO Auto-generated method stub
		String f_id = "";
		
		return getHiraCount(f_id);
	}
	
	public int getHiraCount(String f_id){
		String query = "SELECT count(h._id) FROM android_hira h ";
		if(f_id != "")
		{
			query += " LEFT JOIN android_hira_fihirana hf ON h._id= hf.h_id ";
			query += " WHERE hf.f_id = " + f_id;
		}
		db = this.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		c.moveToFirst();
		int result = c.getInt(0);
		c.close();
		db.close();
		return result;
		
	}
	
	public int getHiraCountBySid(String s_id){
		String query = "SELECT count(h._id) FROM android_hira h ";
			query += " LEFT JOIN android_hira_sokajy hs ON h._id= hs.h_id ";
			query += " WHERE hs.s_id = " + s_id;
		
			db = this.getReadableDatabase();
			Cursor c = db.rawQuery(query, null);
			c.moveToFirst();
			int result = c.getInt(0);
			c.close();
			db.close();
			return result;
		
	}
	


	public Cursor getCategoryNameList()
	{
		String query = "SELECT s._id, s.s_title "
				+ " FROM android_sokajy s ORDER BY _id";
		
		db = this.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		// db.close();

		return c;
	}
	public Cursor getCategoryList() {
		String start = "";
		String limit = "";
		return getCategoryList(limit, start);
	}

	public Cursor getCategoryList(String limit, String start) {
		if (limit == "")
			limit = "50"; // default limit
		String query = "SELECT s._id, s.s_title, s.s_description "
				+ " FROM android_sokajy s ";
		query += " LIMIT " + limit;
		if (start != "")
			query += " OFFSET " + start;

		db = this.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		// db.close();

		return c;

	}
	
	public Cursor getCategoryListNotEmpty()
	{
		String query = "SELECT s._id, s.s_title, s.s_description, count(hs.h_id) AS cnt " +
				" FROM android_sokajy s " +
				" LEFT JOIN android_hira_sokajy hs ON s._id=hs.s_id " +
				" GROUP BY s._id, s.s_title, s.s_description " +
				" HAVING h_id NOT NULL " +
				" ORDER BY s.s_title";

		db = this.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		// db.close();

		return c;
	}

	public Cursor getCategoryItem(String s_id) {
		
		String query = "SELECT s._id, s.s_title, s.s_description "
				+ " FROM android_sokajy s  WHERE s._id=? LIMIT 1";
		
		db = this.getReadableDatabase();
		Cursor c = db.rawQuery(query, new String[] {s_id});
		// db.close();

		return c;
	}
	
	public Cursor getHiraListBySid(String s_id) {
		int start = 0;
		int limit = 50;
		return getHiraListBySid(s_id, limit, start);
	}

	public Cursor getHiraListBySid(String s_id, int  limit, int start) {
		if (limit == 0)
			limit = 50; // default limit
		String query = "SELECT h._id, h.h_title, f.f_title, hf.f_page, s.s_title, s.s_description "
				+ " FROM android_hira h "
				+ " LEFT JOIN android_hira_fihirana hf ON h._id=hf.h_id "
				+ " LEFT JOIN android_fihirana f ON f._id=hf.f_id " +
				" LEFT JOIN android_hira_sokajy hs ON h._id=hs.h_id " +
				" LEFT JOIN android_sokajy s ON s._id=hs.s_id ";
		if (s_id != "")
			query += " WHERE hs.s_id=" + s_id;
		query += " ORDER BY h.h_title ";
		query += " LIMIT " + limit;
		if (start != 0)
			query += " OFFSET " + start;

		db = this.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		// db.close();

		return c;

	}
	

	
}
