package org.katolika.fihirana.lib;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.katolika.fihirana.lib.models.Hira;

public class FavoriteDb extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "FavoriteDB";

    public FavoriteDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_FAVORITE_TABLE = "CREATE TABLE android_favorite ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "h_id INTEGER, h_title TEXT, f_title TEXT, f_page TEXT)";

        // create books table
        db.execSQL(CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        // db.execSQL("DROP TABLE IF EXISTS books");

        // create fresh books table
        this.onCreate(db);
    }

    public int isFavorite(int h_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id FROM android_favorite WHERE h_id=?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(h_id)});
        int cnt = c.getCount();
        c.close();
        db.close();
        return cnt;
    }


    void removeFavorite(int h_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM android_favorite WHERE h_id = ?";
        db.execSQL(sql, new String[]{String.valueOf(h_id)});
        db.close();
    }

    void saveFavorite(Hira h) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO android_favorite (id, h_id, h_title, f_title, f_page) VALUES (?, ?, ?, ?, ?)";
        db.execSQL(sql, new String[]{String.valueOf(h.getId()),
                String.valueOf(h.getId()),
                h.getH_title(),
                h.getF_title().get(0),
                String.valueOf(h.getF_page().get(0))
        });
        db.close();
    }

    Cursor getFavoriteList() {

        // default limit
        String query = "SELECT h_id, h_title, f_title, f_page "
                + " FROM android_favorite "
                + " ORDER BY LOWER(h_title) ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        // db.close();

        return c;

    }
}
