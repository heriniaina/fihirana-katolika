package org.katolika.fihirana.lib.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "android_fihirana_changes")
public class Fanovana {


    @PrimaryKey
    private int _id;

    private int c_date;

    private int c_id;

    private String c_table;

    private String c_type;

    public Fanovana() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getC_date() {
        return c_date;
    }

    public void setC_date(int c_date) {
        this.c_date = c_date;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getC_table() {
        return c_table;
    }

    public void setC_table(String c_table) {
        this.c_table = c_table;
    }

    public String getC_type() {
        return c_type;
    }

    public void setC_type(String c_type) {
        this.c_type = c_type;
    }
}
