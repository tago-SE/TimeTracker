package tago.timetrackerapp.repo.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Activity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "color")
    public int color;

    @ColumnInfo(name = "icon")
    public int icon;

    @Ignore
    private Category category;

    @ColumnInfo(name = "category_id")
    public long categoryId;


    public Activity() {
        // Empty constructor
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category != null) {
            categoryId = category.id;
        } else {
            categoryId = 0;
        }
        this.category = category;
    }

    @Ignore
    public Activity(String name, int color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                ", icon=" + icon +
                ", category=" + category +
                ", categoryId=" + categoryId +
                '}';
    }
}
