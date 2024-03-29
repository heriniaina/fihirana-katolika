package org.katolika.fihirana.lib.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


import org.katolika.fihirana.lib.entities.Favorite;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Favorite.class},version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {
    private static FavoriteDatabase database;

    public abstract FavoriteDao favoriteDao();

    public static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
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