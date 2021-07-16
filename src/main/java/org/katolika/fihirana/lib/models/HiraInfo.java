package org.katolika.fihirana.lib.models;

import org.katolika.fihirana.lib.entities.Hira;

public class HiraInfo extends Hira {
    String f_title;
    String f_description;
    int f_page;

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

    public int getF_page() {
        return f_page;
    }

    public void setF_page(int f_page) {
        this.f_page = f_page;
    }
}
