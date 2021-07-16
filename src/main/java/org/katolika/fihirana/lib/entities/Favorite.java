package org.katolika.fihirana.lib.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "android_favorite")
public class Favorite {

    @PrimaryKey(autoGenerate = true)
    Integer id;

    Integer h_id;

    String h_title;

    String f_title;

    String f_page;

    public Favorite() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getH_id() {
        return h_id;
    }

    public void setH_id(int h_id) {
        this.h_id = h_id;
    }

    public String getH_title() {
        return h_title;
    }

    public void setH_title(String h_title) {
        this.h_title = h_title;
    }

    public String getF_title() {
        return f_title;
    }

    public void setF_title(String f_title) {
        this.f_title = f_title;
    }

    public String getF_page() {
        return f_page;
    }

    public void setF_page(String f_page) {
        this.f_page = f_page;
    }
}
