package com.bnb.doggydoo.homemodule.ui;

//public class Route {
//}

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route{
    @SerializedName("legs")
    private List<Legs> legses;

    public List<Legs> getLegses() {
        return legses;
    }

    public void setLegses(List<Legs> legses) {
        this.legses = legses;
    }
}
