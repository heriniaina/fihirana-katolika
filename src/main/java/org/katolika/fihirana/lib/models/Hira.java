package org.katolika.fihirana.lib.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Hira  extends org.katolika.fihirana.lib.entities.Hira {
    //h._id, h.h_title, f.f_title, f.f_description, hf.f_page
    int faha; //ho an'ny salamo

    String f_description;


    public int getFaha() {
        return faha;
    }

    public void setFaha(int faha) {
        this.faha = faha;
    }

    public String getF_description() {
        return f_description;
    }

    public void setF_description(String f_description) {
        this.f_description = f_description;
    }

}
