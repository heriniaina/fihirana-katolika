package org.katolika.fihirana.lib.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.katolika.fihirana.lib.entities.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Hira.class, Fihirana.class, HiraFihirana.class, HiraSokajy.class, Sokajy.class, Salamo.class, Fanovana.class}, version = 1, exportSchema = false)
public abstract class FihiranaDatabase extends RoomDatabase {
    private static FihiranaDatabase database;
    public abstract FihiranaDao fihiranaDao();

    public static synchronized FihiranaDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    FihiranaDatabase.class,
                    "fihirana-2024_20_01_1705711882.db")
                    .createFromAsset("database/fihirana-2024_20_01_1705711882.db")
                    .build();
        }
        return database;
    }
}