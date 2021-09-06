package org.katolika.fihirana.lib.models;

public class Salamo extends org.katolika.fihirana.lib.entities.Salamo {

    String h_title;
    String f_title;
    int f_page;

    public Salamo(int id, int h_id, int faha) {
        super(id, h_id, faha);
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

    public int getF_page() {
        return f_page;
    }

    public void setF_page(int f_page) {
        this.f_page = f_page;
    }
}
