package org.katolika.fihirana.lib.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Hira {
    //h._id, h.h_title, f.f_title, f.f_description, hf.f_page
    int id;
    int faha; //ho an'ny salamo
    String h_title;
    List<String> f_title = new ArrayList<>();
    String f_description;
    List<Integer> f_page = new ArrayList<>();

    public Hira() {
    }

    public int getFaha() {
        return faha;
    }

    public void setFaha(int faha) {
        this.faha = faha;
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

    public List<String> getF_title() {
        return f_title;
    }

    public void setF_title(String f_title) {
        this.f_title.add(f_title);
    }

    public String getF_description() {
        return f_description;
    }

    public void setF_description(String f_description) {
        this.f_description = f_description;
    }

    public List<Integer> getF_page() {
        return f_page;
    }

    public void setF_page(int f_page) {
        this.f_page.add(f_page);
    }
}
