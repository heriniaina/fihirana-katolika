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


	List<Fihirana> getFihiranaList() {
		// Select All Query
		String selectQuery = "SELECT  f.* , count(hf._id) cnt FROM android_fihirana f LEFT JOIN android_hira_fihirana hf ON hf.f_id=f._id GROUP BY f._id HAVING cnt > 0 ORDER BY f.f_title";

		List<Fihirana> fihiranaList = new ArrayList<>();
		db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {

			do {
				Fihirana f = new Fihirana();
				f.setId(c.getInt(c.getColumnIndex("_id")));
				f.setName(c.getString(c.getColumnIndex("f_title")));
				f.setDescription(c.getString(c.getColumnIndex("f_description")));
				f.setCnt(c.getInt(c.getColumnIndex("cnt")));

				fihiranaList.add(f);
			} while (c.moveToNext());
		}
		c.close();
		db.close();

		return fihiranaList;
	}

	public Fihirana getFihiranaItem(int f_id) {
		Fihirana fihirana = new Fihirana();
		db = this.getReadableDatabase();
		Cursor cursor = db.query("android_fihirana", new String[] { "_id", "f_title",
				"f_description" }, "_id=?",
				new String[] { String.valueOf(f_id) }, null, null, null, null);
		if (cursor.getCount() == 1)
		{
			cursor.moveToFirst();
			fihirana.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			fihirana.setName(cursor.getString(cursor.getColumnIndex("f_title")));
			fihirana.setDescription(cursor.getString(cursor.getColumnIndex("f_description")));
		}
		cursor.close();
		db.close();
		return fihirana;
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
	

	List<Hira> getHiraList(int f_id) {
		int start = 0;
		int limit = 50;
		return getHiraList(f_id, limit, start);
	}
	

	List<Hira> getHiraList(int f_id, int limit, int start){
		List<Hira>  hiraList = new ArrayList<>();
		if(limit == 0) limit = 50;
		String query = "SELECT h._id, h.h_title, f.f_title, f.f_description, hf.f_page "
				+ " FROM android_hira h "
				+ " LEFT JOIN android_hira_fihirana hf ON h._id=hf.h_id "
				+ " LEFT JOIN android_fihirana f ON f._id=hf.f_id ";
		query += " WHERE f._id= ?" ;
		query += " ORDER BY hf.f_page, h_title ";
		query += " LIMIT ?" ;
		query += " OFFSET ?";
		db = this.getReadableDatabase();
		Cursor c = db.rawQuery(query, new String[] {String.valueOf(f_id), String.valueOf(limit), String.valueOf(start)});

		if (c.moveToFirst()) {

			do {
				Hira h = new Hira();
				h.setId(c.getInt(c.getColumnIndex("_id")));
				h.setH_title(c.getString(c.getColumnIndex("h_title")));
				h.setF_description(c.getString(c.getColumnIndex("f_description")));
				h.setF_page(c.getInt(c.getColumnIndex("f_page")));
				h.setF_title(c.getString(c.getColumnIndex("f_title")));

				hiraList.add(h);
			} while (c.moveToNext());
		}
		c.close();
		db.close();

		return hiraList;
	}
	
	public List<Hira> getHiraListOnPage(int f_id, int f_page, int limit, int start) {
		List<Hira>  hiraList = new ArrayList<>();
		String query = "SELECT h._id, h.h_title, f.f_title, f.f_description, hf.f_page "
				+ " FROM android_hira h "
				+ " LEFT JOIN android_hira_fihirana hf ON h._id=hf.h_id "
				+ " LEFT JOIN android_fihirana f ON f._id=hf.f_id ";
		query += " WHERE f._id=? AND hf.f_page=?" ;
		query += " ORDER BY h_title ";
		query += " LIMIT ?" ;
		query += " OFFSET ?";
		

		db = this.getReadableDatabase();
		Cursor c = db.rawQuery(query, new String[] {String.valueOf(f_id), String.valueOf(f_page), String.valueOf(limit), String.valueOf(start)});
		if (c.moveToFirst()) {

			do {
				Hira h = new Hira();
				h.setId(c.getInt(c.getColumnIndex("_id")));
				h.setH_title(c.getString(c.getColumnIndex("h_title")));
				h.setF_description(c.getString(c.getColumnIndex("f_description")));
				h.setF_page(c.getInt(c.getColumnIndex("f_page")));
				h.setF_title(c.getString(c.getColumnIndex("f_title")));

				hiraList.add(h);
			} while (c.moveToNext());
		}
		c.close();
		db.close();

		return hiraList;

	}
	
	public Cursor getPageList(String f_id)
	{
		String query = "SELECT DISTINCT f_page FROM android_hira_fihirana WHERE f_id=? ORDER BY f_page";
		db = this.getReadableDatabase();
		Cursor c = db.rawQuery(query, new String[] {f_id});
		return c;		
	}

	Hira getHiraItem(int h_id) {
		Hira hira = new Hira();
		String query = "SELECT h._id, h.h_title " +
		", f.f_title, f.f_description, hf.f_page " +
				" FROM android_hira h " +
				" LEFT JOIN android_hira_fihirana hf ON h._id=hf.h_id " +
				" LEFT JOIN android_fihirana f ON f._id=hf.f_id " +
				" WHERE h._id=? ";

		db = this.getReadableDatabase();
		Cursor c = db.rawQuery(query, new String[] {String.valueOf(h_id)});
		if (c.getCount() > 0)
		{
			c.moveToFirst();
			hira.setId(c.getInt(c.getColumnIndex("_id")));
			hira.setH_title(c.getString(c.getColumnIndex("h_title")));
			int i=0;
			do {
				hira.setF_title(c.getString(c.getColumnIndex("f_title")));
				hira.setF_page(c.getInt(c.getColumnIndex("f_page")));
				i++;
			} while (c.moveToNext());
		}
		c.close();
		db.close();

		return hira;

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
	
	List<Hira> getHiraFromSearch(String search_from_title, String search_from_content, String search_from_category, int limit, int start)
	{
		List<Hira> hiraList = new ArrayList<>();
		String query = "SELECT DISTINCT h._id, h.h_title, f.f_title, hf.f_page "
				+ " FROM android_hira h "
				+ " LEFT JOIN android_hira_fihirana hf ON h._id=hf.h_id "
				+ " LEFT JOIN android_fihirana f ON f._id=hf.f_id " +
				" LEFT JOIN android_hira_sokajy hs ON h._id=hs.h_id " +
				" LEFT JOIN android_sokajy s ON s._id=hs.s_id " +
				" WHERE 1 ";
        ArrayList<String> where = new ArrayList<>();

		int i = 0;
		
		if (!search_from_category.equals(""))
		{
			query += " AND hs.s_id=? ";
			where.add(search_from_category);
			i++;
		}
		if (!search_from_title.equals(""))
		{
			search_from_title = search_from_title.trim();
			//search_from_title = search_from_title.replace("'", " ");
			//search_from_title = search_from_title.replace("-", " ");
			final String[] splits = TextUtils.split(search_from_title, " ");
			for(String split : splits)
			{
				query += " AND h.h_title LIKE ? ";
				where.add("%"+split+"%");
                i++;
			}
			
		}
		
		query += " ORDER BY h_title ";
		query += " LIMIT " + limit;
		if (start > 0)
			query += " OFFSET " + start;

		db = this.getReadableDatabase();
		
		
		// todo: I have to fix this
		Cursor c;
		c = db.rawQuery(query, where.toArray(new String[where.size()]));
		if(c.getCount() > 0)
		{
			c.moveToFirst();
			do {
				Hira h = new Hira();
				h.setId(c.getInt(c.getColumnIndex("_id")));
				h.setH_title(c.getString(c.getColumnIndex("h_title")));
				h.setF_title(c.getString(c.getColumnIndex("f_title")));
				h.setF_page(c.getInt(c.getColumnIndex("f_page")));

				hiraList.add(h);
			} while (c.moveToNext());
		}

		c.close();
		db.close();

		return hiraList;
	}

	public List<Hira> getSalamoList(int faha, int limit, int start)
	{
		List<Hira> hiraList = new ArrayList<>();
		//p = salamo
		String query = "SELECT DISTINCT p.h_id, p.faha, h.h_title, f.f_title, hf.f_page "
				+ " FROM android_salamo p "
				+ " LEFT JOIN android_hira h ON h._id=p.h_id "
				+ " LEFT JOIN android_hira_fihirana hf ON h._id=hf.h_id "
				+ " LEFT JOIN android_fihirana f ON f._id=hf.f_id " 
				//+ " LEFT JOIN android_hira_sokajy hs ON h._id=hs.h_id " 
				//+ " LEFT JOIN android_sokajy s ON s._id=hs.s_id "
				+ "  WHERE h._id IS NOT NULL ";
				if(faha > 0)
					query += " AND p.faha = " + faha;
				query += " ORDER BY p.faha ";
				query += " LIMIT " + limit + " OFFSET " + start;

		Log.d(TAG, "getSalamoList: " + query);
		
		db = this.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		if(c.getCount() > 0)
		{
			c.moveToFirst();
			do{
				Hira h = new Hira();
				h.setId(c.getInt(c.getColumnIndex("h_id")));
				h.setFaha(c.getInt(c.getColumnIndex("faha")));
				h.setH_title(c.getString(c.getColumnIndex("h_title")));
				h.setF_title(c.getString(c.getColumnIndex("f_title")));
				h.setF_page(c.getInt(c.getColumnIndex("f_page")));

				hiraList.add(h);
			}while (c.moveToNext());
		}
		c.close();
		db.close();

		return hiraList;
	}
	
}
