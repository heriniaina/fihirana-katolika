package org.katolika.fihirana.lib.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.katolika.fihirana.lib.entities.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Hira.class, Fihirana.class, HiraFihirana.class, HiraSokajy.class, Sokajy.class, Salamo.class, Fanovana.class}, version = 2, exportSchema = false)
public abstract class FihiranaDatabase extends RoomDatabase {
    private static FihiranaDatabase database;
    public abstract FihiranaDao fihiranaDao();
    public static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static synchronized FihiranaDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    FihiranaDatabase.class,
                    "fihirana-2024_24_01_1705711882.db")
                    .createFromAsset("database/fihirana-2024_24_01_1705711882.db")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return database;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };
}