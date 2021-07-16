package org.katolika.fihirana.lib.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.katolika.fihirana.lib.entities.Fanovana;
import org.katolika.fihirana.lib.entities.Hira;
import org.katolika.fihirana.lib.models.Sokajy;
import org.katolika.fihirana.lib.models.Fihirana;
import org.katolika.fihirana.lib.models.HiraInfo;
import org.katolika.fihirana.lib.models.Salamo;

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
        return fihiranaDao.getHiraByFihiranaId(f_id, start, limit,  page + "%");
    }

    public LiveData<Fihirana> getFihirana(int id) {
        return fihiranaDao.getFihirana(id);
    }

    public LiveData<List<Sokajy>> getSokajyList() {
        return fihiranaDao.getSokajyList();
    }

    public LiveData<List<HiraInfo>> getHiraFromSearch(int limit, String search_from_title, int search_from_category, String search_from_content) {
        if(search_from_category == 0) {
            return fihiranaDao.getHiraFromSearch(limit, "%" + search_from_title.trim() + "%", "%" + search_from_content.trim() + "%");
        } else {
            return fihiranaDao.getHiraFromSearch(limit, "%" + search_from_title.trim() + "%", search_from_category, "%" + search_from_content.trim() + "%");
        }
    }

    public LiveData<HiraInfo> getHiraItem(int h_id) {
        return fihiranaDao.getHiraItem(h_id);
    }

    public LiveData<List<Salamo>> getSalamoList() {return fihiranaDao.getSalamoList();}

    public LiveData<List<Salamo>> getSalamoList(int faha) {return fihiranaDao.getSalamoList(faha + "%");}

    LiveData<List<Sokajy>> getSokajyListWithCount(){
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

    public void insertHira(Hira hira) {
        new InsertHiraThread(fihiranaDao, hira).start();
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

    public void insertHiraList(List<Hira> hiraList) {
        new InsertHiraListThread(fihiranaDao, hiraList).start();
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

    public void updateHiraList(List<Hira> hiraList) {
        new UpdateHiraListThread(fihiranaDao, hiraList).start();
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

    public LiveData<Integer> getHiraEmptyTextCount() {
        return fihiranaDao.getHiraEmptyTextCount();
    }

    public LiveData<Integer> getHiraCount() { return fihiranaDao.getHiraCount();}

    public void updateChange(int id) {
        new UpdateChangeThread(fihiranaDao, id).start();
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
            fihiranaDao.updateChange(id);        }
    }

    public LiveData<Fanovana> getLastChange() {
        return fihiranaDao.getLastChange();
    }

    public void deleteHiraById(int id) {
        new DeleteHiraThread(fihiranaDao, id).start();
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
