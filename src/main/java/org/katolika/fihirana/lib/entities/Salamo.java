package org.katolika.fihirana.lib.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "android_salamo", indices = {@Index(name ="android_salamo_h_id", value = {"h_id", "faha"})})
public class Salamo {

    @PrimaryKey(autoGenerate = true)
    int id;

    int h_id;

    int faha;

    public Salamo() {
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

    public int getFaha() {
        return faha;
    }

    public void setFaha(int faha) {
        this.faha = faha;
    }
}
