package org.katolika.fihirana.lib.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.katolika.fihirana.lib.entities.Fanovana;
import org.katolika.fihirana.lib.entities.Hira;
import org.katolika.fihirana.lib.models.Sokajy;
import org.katolika.fihirana.lib.models.Fihirana;
import org.katolika.fihirana.lib.models.HiraInfo;
import org.katolika.fihirana.lib.models.Salamo;

import java.util.List;


@Dao
public interface FihiranaDao {

    @Query("SELECT  f.* , count(hf.id) cnt FROM android_fihirana f LEFT JOIN android_hira_fihirana hf ON hf.f_id=f.id GROUP BY f.id HAVING cnt > 0 ORDER BY f.f_title")
    LiveData<List<Fihirana>> getFihiranaList();

    @Query("SELECT h.id, h.h_title, h.h_text, f.f_title, f.f_description, hf.f_page  FROM android_hira h  LEFT JOIN android_hira_fihirana hf ON h.id=hf.h_id  LEFT JOIN android_fihirana f ON f.id=hf.f_id  WHERE f.id=:f_id ORDER BY h_title LIMIT :limit OFFSET :start")
    LiveData<List<HiraInfo>> getHiraByFihiranaId(int f_id, int start, int limit);

    @Query("SELECT h.id, h.h_title, h.h_text, f.f_title, f.f_description, hf.f_page  FROM android_hira h  LEFT JOIN android_hira_fihirana hf ON h.id=hf.h_id  LEFT JOIN android_fihirana f ON f.id=hf.f_id  WHERE f.id=:f_id AND hf.f_page LIKE :page  ORDER BY h_title LIMIT :limit OFFSET :start")
    LiveData<List<HiraInfo>> getHiraByFihiranaId(int f_id, int start, int limit, String page);

    @Query("SELECT  f.* , count(hf.id) cnt FROM android_fihirana f LEFT JOIN android_hira_fihirana hf ON hf.f_id=f.id WHERE f.id =:id GROUP BY f.id HAVING cnt > 0 ORDER BY f.f_title")
    LiveData<Fihirana> getFihirana(int id);

    @Query("SELECT *, 0 as cnt FROM android_sokajy ORDER BY s_title")
    LiveData<List<Sokajy>> getSokajyList();
    @Query("SELECT s.id, s.s_title, s.s_description, count(hs.h_id) AS cnt " +
            " FROM android_sokajy s " +
            " LEFT JOIN android_hira_sokajy hs ON s.id=hs.s_id " +
            " GROUP BY s.id, s.s_title, s.s_description " +
            " HAVING h_id NOT NULL " +
            " ORDER BY s.s_title")
    LiveData<List<Sokajy>> getSokajyListWithCount();

    @Query("SELECT h.id, h.h_title, h.h_text, f.f_title, MAX(f.f_description) f_description, hf.f_page  FROM android_hira h  LEFT JOIN android_hira_fihirana hf ON h.id=hf.h_id  LEFT JOIN android_fihirana f ON f.id=hf.f_id  WHERE h.id NOTNULL AND h.h_title LIKE :search_from_title AND h.h_text LIKE :search_from_content  GROUP BY h.id, h.h_title, h.h_text ORDER BY h_title LIMIT :limit")
    LiveData<List<HiraInfo>> getHiraFromSearch(int limit, String search_from_title, String search_from_content);

    @Query("SELECT h.id, h.h_title, h.h_text, f.f_title, f.f_description, hf.f_page  FROM android_hira h  LEFT JOIN android_hira_fihirana hf ON h.id=hf.h_id  LEFT JOIN android_fihirana f ON f.id=hf.f_id LEFT JOIN android_hira_sokajy hs ON hs.h_id=h.id LEFT JOIN android_sokajy s ON hs.s_id = s.id  WHERE h.id NOTNULL AND h.h_title LIKE :search_from_title AND s.id = :search_from_category AND h.h_text LIKE :search_from_content ORDER BY h_title LIMIT :limit")
    LiveData<List<HiraInfo>> getHiraFromSearch(int limit, String search_from_title, int search_from_category, String search_from_content);

    @Query("SELECT h.id, h.h_title, h.h_text, f.f_title, f.f_description, hf.f_page  FROM android_hira h  LEFT JOIN android_hira_fihirana hf ON h.id=hf.h_id  LEFT JOIN android_fihirana f ON f.id=hf.f_id WHERE h.id = :h_id")
    LiveData<HiraInfo> getHiraItem(int h_id);

    @Query("SELECT s.id, s.faha, s.h_id, h.h_title, f.f_title, hf.f_page FROM android_salamo s LEFT JOIN android_hira h ON s.h_id=h.id LEFT JOIN android_hira_fihirana hf ON h.id=hf.h_id  LEFT JOIN android_fihirana f ON f.id=hf.f_id  WHERE  h.id NOTNULL AND s.faha > 0 GROUP BY s.id, s.faha, s.h_id, h.h_title ORDER BY faha")
    LiveData<List<Salamo>> getSalamoList();

    @Query("SELECT s.id, s.faha, s.h_id, h.h_title, f.f_title, hf.f_page FROM android_salamo s LEFT JOIN android_hira h ON s.h_id=h.id LEFT JOIN android_hira_fihirana hf ON h.id=hf.h_id  LEFT JOIN android_fihirana f ON f.id=hf.f_id  WHERE  h.id NOTNULL AND s.faha LIKE :faha GROUP BY s.id, s.faha, s.h_id, h.h_title ORDER BY faha")
    LiveData<List<Salamo>> getSalamoList(String faha);

    @Query("SELECT h.id, h.h_title, h.h_text, f.f_title, MAX(f.f_description) f_description, hf.f_page  FROM android_hira h  LEFT JOIN android_hira_fihirana hf ON h.id=hf.h_id  LEFT JOIN android_fihirana f ON f.id=hf.f_id LEFT JOIN android_hira_sokajy hs ON hs.h_id=h.id LEFT JOIN android_sokajy s ON hs.s_id = s.id WHERE  h.id NOTNULL AND s.id = :s_id  GROUP BY h.id, h.h_title, h.h_text ORDER BY h_title")// LIMIT :limit OFFSET :start")
    LiveData<List<HiraInfo>> getHiraByCategory(int s_id);

    @Update
    void updateHira(Hira hira);

    @Insert
    void insertHira(Hira hira);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListHira(List<Hira> hiraList);

    @Update
    void updateHiraList(List<Hira> hiraList);

    @Query("SELECT COUNT(id) FROM android_hira WHERE h_text = '' OR h_text IS NULL")
    LiveData<Integer> getHiraEmptyTextCount();

    @Query("SELECT id, h_text, h_title FROM android_hira WHERE h_text = '' OR h_text IS NULL")
    LiveData<List<Hira>> getHiraEmptyText();

    @Query("SELECT COUNT(id) FROM android_hira")
    LiveData<Integer> getHiraCount();

    @Query("UPDATE android_fihirana_changes SET _id = :id ")
    void updateChange(int id);

    @Query("SELECT * FROM android_fihirana_changes ORDER BY _id DESC LIMIT 1")
    LiveData<Fanovana> getLastChange();

    @Query("DELETE FROM android_hira WHERE id = :id")
    void deleteHiraById(int id);

    @Query("DELETE FROM android_hira_fihirana WHERE h_id=:h_id")
    void deleteHiraFromFihirana(int h_id);

    @Query("DELETE FROM android_hira_sokajy WHERE h_id=:h_id")
    void deleteHiraFromSokajy(int h_id);

    @Query("DELETE FROM android_salamo WHERE h_id=:h_id")
    void deleteHiraFromSalamo(int h_id);
}
