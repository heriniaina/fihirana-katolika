package org.katolika.fihirana.lib.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.katolika.fihirana.lib.entities.*;

@Database(entities = {Hira.class, Fihirana.class, HiraFihirana.class, HiraSokajy.class, Sokajy.class, Salamo.class, Fanovana.class}, version = 1, exportSchema = false)
public abstract class FihiranaDatabase extends RoomDatabase {
    private static FihiranaDatabase database;
    public abstract FihiranaDao fihiranaDao();

    public static synchronized FihiranaDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    FihiranaDatabase.class,
                    "fihirana-2021_05_09_1630846262.db")
                    .createFromAsset("fihirana-2021_05_09_1630846262.db")
                    .build();
        }
        return database;
    }
}