package org.katolika.fihirana.lib.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import org.katolika.fihirana.lib.entities.Favorite;


@Database(entities = {Favorite.class},version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {
    private static FavoriteDatabase database;

    public abstract FavoriteDao favoriteDao();

    public static synchronized FavoriteDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    FavoriteDatabase.class,
                    "FavoriteDB")
                    .build();
        }
        return database;
    }

}