package org.katolika.fihirana.lib.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "android_fihirana", indices = {@Index(name = "android_fihirana_f_title", value="f_title")})
public class Fihirana {

    @PrimaryKey(autoGenerate = true)
    int id;

    String f_title;

    String f_description;

    public Fihirana() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getF_title() {
        return f_title;
    }

    public void setF_title(String f_title) {
        this.f_title = f_title;
    }

    public String getF_description() {
        return f_description;
    }

    public void setF_description(String f_description) {
        this.f_description = f_description;
    }
}
