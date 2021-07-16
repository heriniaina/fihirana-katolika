package org.katolika.fihirana.lib.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "android_hira", indices = {@Index(name = "android_hira_h_title", value = "h_title")})
public class Hira {

    @PrimaryKey(autoGenerate = true)
    int id;

    String h_title;

    @NonNull
    String h_text;

    public Hira() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getH_title() {
        return h_title;
    }

    public void setH_title(String h_title) {
        this.h_title = h_title;
    }

    public String getH_text() {
        return h_text;
    }

    public void setH_text(String h_text) {
        this.h_text = h_text;
    }
}
