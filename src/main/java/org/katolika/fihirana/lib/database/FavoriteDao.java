package org.katolika.fihirana.lib.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.katolika.fihirana.lib.entities.Favorite;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT COUNT(id) FROM android_favorite WHERE h_id= :h_id")
    LiveData<Integer> isFavorite(int h_id);

    @Insert
    void insert(Favorite favorite);

    @Query("DELETE FROM android_favorite WHERE h_id = :h_id")
    void deleteByHid(int h_id);

    @Query("SELECT * FROM android_favorite ORDER BY h_title")
    LiveData<List<Favorite>> getFavoriteList();
}
