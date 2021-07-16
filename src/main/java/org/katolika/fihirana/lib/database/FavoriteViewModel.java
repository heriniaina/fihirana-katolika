package org.katolika.fihirana.lib.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.katolika.fihirana.lib.entities.Favorite;
import org.katolika.fihirana.lib.models.HiraInfo;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {
    FavoriteRepository favoriteRepository;
    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        favoriteRepository = new FavoriteRepository(application);
    }

    public LiveData<Integer> isFavorite(int h_id) {
        return favoriteRepository.isFavorite(h_id);
    }

    public void saveFavorite(HiraInfo hira) {
        Favorite favorite = new Favorite();

        favorite.setF_page(String.valueOf(hira.getF_page()));
        favorite.setId(hira.getId());
        favorite.setF_title(hira.getF_title());
        favorite.setH_id(hira.getId());
        favorite.setH_title(hira.getH_title());

        favoriteRepository.insert(favorite);
    }

    public void removeFavorite(int h_id) {
        favoriteRepository.deleteByHid(h_id);
    }

    public LiveData<List<Favorite>> getFavoriteList(){
        return favoriteRepository.getFavoriteList();
    }

}
