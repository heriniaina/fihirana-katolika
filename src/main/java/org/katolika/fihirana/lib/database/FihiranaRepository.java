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
        String queryString = "SELECT h.id, h.h_title, h.h_text, f.f_title, MAX(f.f_description) f_description, hf.f_page  FROM android_hira h  LEFT JOIN android_hira_fihirana hf ON h.id=hf.h_id  LEFT JOIN android_fihirana f ON f.id=hf.f_id";
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

        queryString += " ORDER BY h_title LIMIT "+limit;
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
        new UpdateHiraThread(fihiranaDao, hira).start();
    }

    public LiveData<List<Hira>> getHiraEmptyText() {
        return fihiranaDao.getHiraEmptyText();
    }

    public void saveSokajyJson(int h_id, JSONArray sokajyArray) {
        new SaveSokajyThread(fihiranaDao, h_id, sokajyArray).start();
    }

    public void saveFihiranaJson(int h_id, JSONArray fihiranaArray) {
        new SaveFihiranaJsonThread(fihiranaDao, h_id, fihiranaArray).start();
    }

    public void insertHira(Hira hira) {
        new InsertHiraThread(fihiranaDao, hira).start();
    }

    public void insertHiraList(List<Hira> hiraList) {
        new InsertHiraListThread(fihiranaDao, hiraList).start();
    }

    public void updateHiraList(List<Hira> hiraList) {
        new UpdateHiraListThread(fihiranaDao, hiraList).start();
    }

    public LiveData<Integer> getHiraEmptyTextCount() {
        return fihiranaDao.getHiraEmptyTextCount();
    }

    public LiveData<Integer> getHiraCount() {
        return fihiranaDao.getHiraCount();
    }

    public void updateChange(int id) {
        new UpdateChangeThread(fihiranaDao, id).start();
    }

    public LiveData<Fanovana> getLastChange() {
        return fihiranaDao.getLastChange();
    }

    public void deleteHiraById(int id) {
        new DeleteHiraThread(fihiranaDao, id).start();
    }

    public void saveSalamoJson(int id, int salamo) {
        new SaveSalamoJsonThread(fihiranaDao, id, salamo).start();
    }

    public void insertFihirana(org.katolika.fihirana.lib.entities.Fihirana fihirana) {
        new InsertFihiranaThread(fihiranaDao, fihirana).start();
    }

    public void updateFihirana(org.katolika.fihirana.lib.entities.Fihirana fihirana) {
        new UpdateFihiranaThread(fihiranaDao, fihirana).start();
    }

    public void insertFanovana(Fanovana fanovana) {
        new InsertFanovanaThread(fihiranaDao, fanovana).start();
    }

    public void insertChangeList(List<Fanovana> fanovanaList) {
        new InsertFanovanaListThread(fihiranaDao, fanovanaList).start();
    }

    private static class InsertFanovanaListThread extends Thread {
        FihiranaDao fihiranaDao;
        List<Fanovana> fanovanaList;

        public InsertFanovanaListThread(FihiranaDao fihiranaDao, List<Fanovana> fanovanaList) {
            this.fihiranaDao = fihiranaDao;
            this.fanovanaList = fanovanaList;
        }


        @Override
        public void run() {
            fihiranaDao.insertFanovana(fanovanaList);
        }
    }

    private static class InsertFanovanaThread extends Thread {
        FihiranaDao fihiranaDao;
        Fanovana fanovana;

        public InsertFanovanaThread(FihiranaDao fihiranaDao, Fanovana fanovana) {
            this.fihiranaDao = fihiranaDao;
            this.fanovana = fanovana;
        }


        @Override
        public void run() {
            fihiranaDao.insertFanovana(fanovana);
        }
    }

    private static class UpdateFihiranaThread extends  Thread {
        FihiranaDao fihiranaDao;
        org.katolika.fihirana.lib.entities.Fihirana fihirana;

        public UpdateFihiranaThread(FihiranaDao fihiranaDao, org.katolika.fihirana.lib.entities.Fihirana fihirana) {
            this.fihiranaDao = fihiranaDao;
            this.fihirana = fihirana;
        }


        @Override
        public void run() {
            fihiranaDao.updateFihirana(fihirana);
        }
    }

    private static class InsertFihiranaThread extends Thread {
        FihiranaDao fihiranaDao;
        org.katolika.fihirana.lib.entities.Fihirana fihirana;

        public InsertFihiranaThread(FihiranaDao fihiranaDao, org.katolika.fihirana.lib.entities.Fihirana fihirana) {
            this.fihirana = fihirana;
            this.fihiranaDao = fihiranaDao;
        }


        @Override
        public void run() {
            fihiranaDao.insertFihirana(fihirana);
        }
    }

    private static class SaveSalamoJsonThread extends Thread {
        private FihiranaDao fihiranaDao;
        private int h_id;
        private int faha;

        public SaveSalamoJsonThread(FihiranaDao fihiranaDao, int id, int faha) {
            this.fihiranaDao = fihiranaDao;
            this.h_id = id;
            this.faha = faha;
        }

        @Override
        public void run() {
            fihiranaDao.deleteHiraFromSalamo(h_id);
            org.katolika.fihirana.lib.entities.Salamo salamo = new org.katolika.fihirana.lib.entities.Salamo(h_id, h_id, faha);
            fihiranaDao.insertHiraSalamo(salamo);
        }
    }

    private static class SaveFihiranaJsonThread extends Thread {
        private FihiranaDao fihiranaDao;
        private int h_id;
        private JSONArray fihiranaArray;

        public SaveFihiranaJsonThread(FihiranaDao fihiranaDao, int h_id, JSONArray fihiranaArray) {
            this.fihiranaDao = fihiranaDao;
            this.h_id = h_id;
            this.fihiranaArray = fihiranaArray;
        }


        @Override
        public void run() {
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
            if (hiraFihiranaList.size() > 0) {
                fihiranaDao.insertListHiraFihirana(hiraFihiranaList);
            }
        }
    }

    private static class SaveSokajyThread extends Thread {
        private FihiranaDao fihiranaDao;
        private int h_id;
        private JSONArray sokajyArray;

        public SaveSokajyThread(FihiranaDao fihiranaDao, int h_id, JSONArray sokajyArray) {
            this.fihiranaDao = fihiranaDao;
            this.h_id = h_id;
            this.sokajyArray = sokajyArray;
        }

        @Override
        public void run() {
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
            if (hiraSokajyList.size() > 0) {
                Log.d(TAG, hiraSokajyList.toString());
                fihiranaDao.insertListHiraSokajy(hiraSokajyList);
            }

        }
    }

    private static class UpdateHiraThread extends Thread {
        private FihiranaDao fihiranaDao;
        private Hira hira;

        UpdateHiraThread(FihiranaDao fihiranaDao, Hira hira) {
            this.fihiranaDao = fihiranaDao;
            this.hira = hira;
        }

        @Override
        public void run() {
            Log.d(TAG, "run: updated " + hira.getH_text());
            fihiranaDao.updateHira(hira);
        }
    }

    private static class InsertHiraThread extends Thread {
        FihiranaDao fihiranaDao;
        Hira hira;

        InsertHiraThread(FihiranaDao fihiranaDao, Hira hira) {
            this.fihiranaDao = fihiranaDao;
            this.hira = hira;
        }

        @Override
        public void run() {
            fihiranaDao.insertHira(hira);
        }
    }

    private static class InsertHiraListThread extends Thread {
        FihiranaDao fihiranaDao;
        List<Hira> hiraList;

        InsertHiraListThread(FihiranaDao fihiranaDao, List<Hira> hiraList) {
            this.fihiranaDao = fihiranaDao;
            this.hiraList = hiraList;
        }

        @Override
        public void run() {
            fihiranaDao.insertListHira(hiraList);
        }
    }

    private static class UpdateHiraListThread extends Thread {
        FihiranaDao fihiranaDao;
        List<Hira> hiraList;

        UpdateHiraListThread(FihiranaDao fihiranaDao, List<Hira> hiraList) {
            this.fihiranaDao = fihiranaDao;
            this.hiraList = hiraList;
        }

        @Override
        public void run() {
            fihiranaDao.updateHiraList(hiraList);
        }
    }

    private static class UpdateChangeThread extends Thread {
        FihiranaDao fihiranaDao;
        int id;

        UpdateChangeThread(FihiranaDao fd, int id) {
            this.fihiranaDao = fd;
            this.id = id;
        }

        @Override
        public void run() {
            fihiranaDao.updateChange(id);
        }
    }

    private static class DeleteHiraThread extends Thread {
        FihiranaDao fihiranaDao;
        int id;

        DeleteHiraThread(FihiranaDao fd, int id) {
            this.fihiranaDao = fd;
            this.id = id;
        }

        @Override
        public void run() {
            fihiranaDao.deleteHiraById(id);
            fihiranaDao.deleteHiraFromFihirana(id);
            fihiranaDao.deleteHiraFromSalamo(id);
            fihiranaDao.deleteHiraFromSokajy(id);
        }
    }
}
