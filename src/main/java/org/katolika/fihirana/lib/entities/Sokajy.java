package org.katolika.fihirana.lib.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "android_sokajy")
public class Sokajy {

    @PrimaryKey(autoGenerate = true)
    int id;

    String s_title;

    String s_description;

    public Sokajy() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getS_title() {
        return s_title;
    }

    public void setS_title(String s_title) {
        this.s_title = s_title;
    }

    public String getS_description() {
        return s_description;
    }

    public void setS_description(String s_description) {
        this.s_description = s_description;
    }
}
