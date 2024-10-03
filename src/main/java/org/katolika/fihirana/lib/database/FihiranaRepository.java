package org.katolika.fihirana.lib.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.katolika.fihirana.lib.entities.Fanovana;
import org.katolika.fihirana.lib.entities.Hira;
import org.katolika.fihirana.lib.entities.HiraFihirana;
import org.katolika.fihirana.lib.entities.HiraSokajy;
import org.katolika.fihirana.lib.models.Fihirana;
import org.katolika.fihirana.lib.models.HiraInfo;
import org.katolika.fihirana.lib.models.Salamo;
import org.katolika.fihirana.lib.models.Sokajy;

import java.util.ArrayList;
import java.util.List;

public class FihiranaRepository {
    private static final String TAG = "FihiranaRepository";
    FihiranaDao fihiranaDao;

    public FihiranaRepository(Application application) {
        FihiranaDatabase database = FihiranaDatabase.getInstance(application);
        fihiranaDao = database.fihiranaDao();
    }

    public LiveData<List<Fihirana>> getFihiranaList() {
        return fihiranaDao.getFihiranaList();
    }

    public LiveData<List<HiraInfo>> getHiraByFihiranaId(int f_id, int start, int limit) {
        return fihiranaDao.getHiraByFihiranaId(f_id, start, limit);
    }

    public LiveData<List<HiraInfo>> getHiraByFihiranaId(int f_id, int start, int limit, int page) {
        return fihiranaDao.getHiraByFihiranaId(f_id, start, limit, page + "%");
    }

    public LiveData<Fihirana> getFihirana(int id) {
        return fihiranaDao.getFihirana(id);
    }

    public LiveData<List<Sokajy>> getSokajyList() {
        return fihiranaDao.getSokajyList();
    }

    public LiveData<List<HiraInfo>> getHiraFromSearch(int limit, String search_from_title, int search_from_category, String search_from_content) {
        String queryString = "SELECT h.id, h.h_title, h.h_text, f.f_title, MAX(f.f_description) f_description, hf.f_page  FROM android_hira h  LEFT JOIN android_hira_fihirana hf ON h.id=hf.h_id  LEFT JOIN android_fihirana f ON f.id=hf.f_id LEFT JOIN android_hira_sokajy hs ON hs.h_id=h.id LEFT JOIN android_sokajy s ON hs.s_id = s.id";
        List<Object> args = new ArrayList<>();
        queryString += " WHERE h.id NOTNULL";
        if(search_from_category > 0) {
            queryString += " AND s.id = ?";
            args.add(search_from_category);
        }
        if(!search_from_title.isEmpty()) {
            String[] arr_search_title = search_from_title.split(" ");
            for (int i = 0; i < arr_search_title.length; i++) {
                if(arr_search_title[i].length() > 1) {
                    queryString += " AND h.h_title LIKE ? ";
                    args.add("%"+arr_search_title[i]+"%");
                }
            }
        }
        if(!search_from_content.isEmpty()) {
            String[] arr_search_content = search_from_content.split(" ");
            for (int i = 0; i < arr_search_content.length; i++) {
                if(arr_search_content[i].length() > 1) {
                    queryString += " AND h.h_text LIKE ? ";
                    args.add("%"+arr_search_content[i]+"%");
                }
            }
        }

        queryString += " GROUP BY h.id ORDER BY h_title LIMIT "+limit;
        Log.d(TAG, "getHiraFromSearch: " + queryString);
        SimpleSQLiteQuery simpleSQLiteQuery = new SimpleSQLiteQuery(queryString, args.toArray());

        return fihiranaDao.executeQuery(simpleSQLiteQuery);
    }

    public LiveData<HiraInfo> getHiraItem(int h_id) {
        return fihiranaDao.getHiraItem(h_id);
    }

    public LiveData<List<Salamo>> getSalamoList() {
        return fihiranaDao.getSalamoList();
    }

    public LiveData<List<Salamo>> getSalamoList(int faha) {
        return fihiranaDao.getSalamoList(faha + "%");
    }

    LiveData<List<Sokajy>> getSokajyListWithCount() {
        return fihiranaDao.getSokajyListWithCount();
    }

    LiveData<List<HiraInfo>> getHiraByCategory(int s_id, int start, int limit) {
        return fihiranaDao.getHiraByCategory(s_id);
    }

    public void updateHira(Hira hira) {
        FihiranaDatabase.databaseWriteExecutor.execute(() -> {
            fihiranaDao.insertHira(hira);
        });
    }

    public LiveData<List<Hira>> getHiraEmptyText() {
        return fihiranaDao.getHiraEmptyText();
    }

    public void saveSokajyJson(int h_id, JSONArray sokajyArray) {
        FihiranaDatabase.databaseWriteExecutor.execute(() -> {
            fihiranaDao.deleteHiraFromSokajy(h_id);
            List<HiraSokajy> hiraSokajyList = new ArrayList<>();
            try {
                for (int i = 0; i < sokajyArray.length(); i++) {
                    Log.d(TAG, "run: sokajyarray s_id " + sokajyArray.getJSONObject(i).getInt("s_id"));
                    Log.d(TAG, "run: hid " + h_id);
                    HiraSokajy hiraSokajy = new HiraSokajy(sokajyArray.getJSONObject(i).getInt("_id"), h_id, sokajyArray.getJSONObject(i).getInt("s_id"));
                    hiraSokajyList.add(hiraSokajy);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!hiraSokajyList.isEmpty()) {
                Log.d(TAG, hiraSokajyList.toString());
                fihiranaDao.insertListHiraSokajy(hiraSokajyList);
            }
        });
    }

    public void saveFihiranaJson(int h_id, JSONArray fihiranaArray) {
        FihiranaDatabase.databaseWriteExecutor.execute(() -> {
            fihiranaDao.deleteHiraFromFihirana(h_id);
            List<HiraFihirana> hiraFihiranaList = new ArrayList<>();
            try {
                for (int i = 0; i < fihiranaArray.length(); i++) {

                    HiraFihirana hiraFihirana = new HiraFihirana(fihiranaArray.getJSONObject(i).getInt("_id"), h_id, fihiranaArray.getJSONObject(i).getInt("f_id"), fihiranaArray.getJSONObject(i).getInt("f_page"));
                    hiraFihiranaList.add(hiraFihirana);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!hiraFihiranaList.isEmpty()) {
                fihiranaDao.insertListHiraFihirana(hiraFihiranaList);
            }
        });

    }

    public void insertHira(Hira hira) {
        FihiranaDatabase.databaseWriteExecutor.execute(() -> {
            fihiranaDao.insertHira(hira);
        });
    }

    public void insertHiraList(List<Hira> hiraList) {
        FihiranaDatabase.databaseWriteExecutor.execute(() -> {
            fihiranaDao.insertListHira(hiraList);
        });
    }

    public void updateHiraList(List<Hira> hiraList) {
        FihiranaDatabase.databaseWriteExecutor.execute(() -> {
            fihiranaDao.updateHiraList(hiraList);
        });
    }

    public LiveData<Integer> getHiraEmptyTextCount() {
        return fihiranaDao.getHiraEmptyTextCount();
    }

    public LiveData<Integer> getHiraCount() {
        return fihiranaDao.getHiraCount();
    }

    public void updateChange(int id) {

        FihiranaDatabase.databaseWriteExecutor.execute(() -> {
            fihiranaDao.updateChange(id);
                });
    }

    public LiveData<Fanovana> getLastChange() {
        return fihiranaDao.getLastChange();
    }

    public void deleteHiraById(int id) {
        FihiranaDatabase.databaseWriteExecutor.execute(() -> {
            fihiranaDao.deleteHiraById(id);
        });
    }

    public void saveSalamoJson(int h_id, int faha) {
        FihiranaDatabase.databaseWriteExecutor.execute(() -> {
            fihiranaDao.deleteHiraFromSalamo(h_id);
            org.katolika.fihirana.lib.entities.Salamo salamo = new org.katolika.fihirana.lib.entities.Salamo(h_id, h_id, faha);
            fihiranaDao.insertHiraSalamo(salamo);
        });

    }

    public void insertFihirana(org.katolika.fihirana.lib.entities.Fihirana fihirana) {
        FihiranaDatabase.databaseWriteExecutor.execute(()-> {
            fihiranaDao.insertFihirana(fihirana);
        });
    }

    public void updateFihirana(org.katolika.fihirana.lib.entities.Fihirana fihirana) {
        FihiranaDatabase.databaseWriteExecutor.execute(() -> {
            fihiranaDao.updateFihirana(fihirana);
        });
    }

    public void insertFanovana(Fanovana fanovana) {
        FihiranaDatabase.databaseWriteExecutor.execute(()-> {
            fihiranaDao.insertFanovana(fanovana);
        });
    }

    public void insertChangeList(List<Fanovana> fanovanaList) {
        FihiranaDatabase.databaseWriteExecutor.execute(()-> {
            fihiranaDao.insertFanovana(fanovanaList);
        });
    }

}
