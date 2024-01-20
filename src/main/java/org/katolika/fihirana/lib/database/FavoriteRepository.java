package org.katolika.fihirana.lib.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.katolika.fihirana.lib.entities.Favorite;
import org.katolika.fihirana.lib.models.Salamo;
import org.katolika.fihirana.lib.models.Sokajy;

import java.util.List;

public class FavoriteRepository {
    FavoriteDao favoriteDao;

    public FavoriteRepository(Application application) {
        FavoriteDatabase database = FavoriteDatabase.getInstance(application);
        favoriteDao = database.favoriteDao();
    }

    LiveData<Integer> isFavorite(int h_id){
        return favoriteDao.isFavorite(h_id);
    }

    public void insert(Favorite favorite) {
        FavoriteDatabase.databaseWriteExecutor.execute(() -> {
            favoriteDao.insert(favorite);
        });
    }


    public void deleteByHid(int h_id) {
        FavoriteDatabase.databaseWriteExecutor.execute(() -> {
            favoriteDao.deleteByHid(h_id);
        });
    }


    LiveData<List<Favorite>> getFavoriteList(){
        return favoriteDao.getFavoriteList();
    }

}

