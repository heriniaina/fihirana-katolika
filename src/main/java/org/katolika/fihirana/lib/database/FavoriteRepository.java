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
        new InsertFavoriteAsyncTask(favoriteDao).execute(favorite);
    }

    static class InsertFavoriteAsyncTask extends AsyncTask<Favorite, Void, Void> {
        private FavoriteDao favoriteDao;

        private InsertFavoriteAsyncTask(FavoriteDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            favoriteDao.insert(favorites[0]);
            return null;
        }
    }

    public void deleteByHid(int h_id) {
        new DeleteFavoriteByAidAsyncTask(favoriteDao).execute(h_id);
    }

    static class DeleteFavoriteByAidAsyncTask extends  AsyncTask<Integer, Void, Void> {

        FavoriteDao favoriteDao;

        private DeleteFavoriteByAidAsyncTask(FavoriteDao favoriteDao) {this.favoriteDao = favoriteDao;}
        @Override
        protected Void doInBackground(Integer... ids) {
            favoriteDao.deleteByHid(ids[0]);
            return null;
        }
    }

    LiveData<List<Favorite>> getFavoriteList(){
        return favoriteDao.getFavoriteList();
    }

}
