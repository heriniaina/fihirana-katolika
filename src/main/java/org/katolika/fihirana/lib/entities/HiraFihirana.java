package org.katolika.fihirana.lib.entities;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "android_hira_fihirana", indices = {@Index(name = "android_hira_fihirana_f_id", value = "f_id"), @Index(name ="android_hira_fihirana_h_id", value = "h_id")})
public class HiraFihirana {

    @PrimaryKey(autoGenerate = true)
    int id;

    int h_id;

    int f_id;

    int f_page;

    public HiraFihirana(int id, int h_id, int f_id, int f_page) {
        this.id = id;
        this.h_id = h_id;
        this.f_id = f_id;
        this.f_page = f_page;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getH_id() {
        return h_id;
    }

    public void setH_id(int h_id) {
        this.h_id = h_id;
    }

    public int getF_id() {
        return f_id;
    }

    public void setF_id(int f_id) {
        this.f_id = f_id;
    }

    public int getF_page() {
        return f_page;
    }

    public void setF_page(int f_page) {
        this.f_page = f_page;
    }
}
