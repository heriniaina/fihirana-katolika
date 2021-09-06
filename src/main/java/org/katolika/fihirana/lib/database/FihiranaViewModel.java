package org.katolika.fihirana.lib.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.katolika.fihirana.lib.entities.Fanovana;
import org.katolika.fihirana.lib.entities.Hira;
import org.katolika.fihirana.lib.models.Sokajy;
import org.katolika.fihirana.lib.models.Fihirana;
import org.katolika.fihirana.lib.models.HiraInfo;
import org.katolika.fihirana.lib.models.Salamo;

import java.util.List;

public class FihiranaViewModel extends AndroidViewModel {
    FihiranaRepository fihiranaRepository;
    public FihiranaViewModel(Application application) {
        super(application);
        fihiranaRepository = new FihiranaRepository(application);
    }

    public LiveData<List<Fihirana>> getFihiranaList() {
        return fihiranaRepository.getFihiranaList();
    }

    public LiveData<List<HiraInfo>> getHiraListByFihiranaId(int f_id, int start, int limit, int page) {
        if(page == 0) {
            return fihiranaRepository.getHiraByFihiranaId(f_id, start, limit);
        }
        else {
            return fihiranaRepository.getHiraByFihiranaId(f_id, start, limit, page);
        }

    }

    public LiveData<Fihirana> getFihirana(int id) {
        return fihiranaRepository.getFihirana(id);
    }

    public LiveData<List<Sokajy>> getSokajyList() {
        return fihiranaRepository.getSokajyList();
    }

    public LiveData<List<HiraInfo>> getHiraFromSearch(int limit, String search_from_title, int search_from_category, String search_from_content) {
        return fihiranaRepository.getHiraFromSearch(limit, search_from_title, search_from_category, search_from_content);
    }

    public LiveData<HiraInfo> getHiraItem(int h_id) {
        return fihiranaRepository.getHiraItem(h_id);
    }

    public LiveData<List<Salamo>> getSalamoList() {return fihiranaRepository.getSalamoList();}

    public LiveData<List<Salamo>> getSalamoList(int faha) {return fihiranaRepository.getSalamoList(faha);}

    public LiveData<List<Sokajy>> getSokajyListWithCount() {
        return fihiranaRepository.getSokajyListWithCount();
    }

    public LiveData<List<HiraInfo>> getHiraByCategory(int s_id, int start, int limit) {
        return fihiranaRepository.getHiraByCategory(s_id, start, limit);
    }

    public void updateHira(Hira hira) {
        fihiranaRepository.updateHira(hira);
    }

    public void insertHira(Hira hira) {
        fihiranaRepository.insertHira(hira);
    }
    public void insertHiraList(List<Hira> hiraList) {
        fihiranaRepository.insertHiraList(hiraList);
    }

    public LiveData<Integer> getHiraEmptyTextCount() {
        return fihiranaRepository.getHiraEmptyTextCount();
    }

    public LiveData<List<Hira>> getHiraEmptyText() {
        return fihiranaRepository.getHiraEmptyText();
    }

    public void updateHiraList(List<Hira> hiraList) {
        fihiranaRepository.updateHiraList(hiraList);
    }

    public LiveData<Integer> getHiraCount() {return fihiranaRepository.getHiraCount();}

    public void updateChange(int id) { fihiranaRepository.updateChange(id);}

    public LiveData<Fanovana> getLastChange() {return fihiranaRepository.getLastChange(); }

    public void deleteHiraById(int id) {
        fihiranaRepository.deleteHiraById(id);
    }

    public void saveSokajyJson(int h_id, JSONArray sokajyArray) {
        fihiranaRepository.saveSokajyJson(h_id, sokajyArray);
    }

    public void saveFihiranaJson(int h_id, JSONArray fihiranaArray) {
        fihiranaRepository.saveFihiranaJson(h_id, fihiranaArray);
    }

    public void saveSalamoJson(int h_id, int salamo) {
        fihiranaRepository.saveSalamoJson(h_id, salamo);
    }

    public void insertFihirana(org.katolika.fihirana.lib.entities.Fihirana fihirana) {
        fihiranaRepository.insertFihirana(fihirana);
    }

    public void updateFihirana(org.katolika.fihirana.lib.entities.Fihirana fihirana) {
        fihiranaRepository.updateFihirana(fihirana);
    }

    public void insertChange(Fanovana fanovana) {
        fihiranaRepository.insertFanovana(fanovana);
    }

    public void insertChangeList(List<Fanovana> fanovanaList) {
        fihiranaRepository.insertChangeList(fanovanaList);
    }
}
