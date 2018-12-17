package tago.timetrackerapp.repo.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

@Entity
public class CategoryEntity implements EntityInt {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private int color;

    @Ignore
    private List<ActivityEntity> activities;

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
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
