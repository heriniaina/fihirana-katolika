package org.katolika.fihirana.lib.models;

public class Fihirana  extends org.katolika.fihirana.lib.entities.Fihirana {

    int cnt;

    public void setName(String name) {
        this.setF_title(name);
    }

    public void setDescription(String description) {
        setF_description(description);
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
