package tago.timetrackerapp.model;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private long id;
    private String name;
    private int color;
    private List<Activity> activities;

    public Category() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


}
