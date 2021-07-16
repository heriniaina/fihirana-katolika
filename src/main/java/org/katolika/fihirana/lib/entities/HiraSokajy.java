package org.katolika.fihirana.lib.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "android_hira_sokajy", indices = {@Index(name = "android_hira_sokajy_h_id",value = "h_id"), @Index(name = "android_hira_sokajy_s_id", value = "s_id")})
public class HiraSokajy {

    @PrimaryKey(autoGenerate = true)
    int id;

    Integer s_id;

    Integer h_id;

    public HiraSokajy() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getS_id() {
        return s_id;
    }

    public void setS_id(Integer s_id) {
        this.s_id = s_id;
    }

    public Integer getH_id() {
        return h_id;
    }

    public void setH_id(Integer h_id) {
        this.h_id = h_id;
    }
}
