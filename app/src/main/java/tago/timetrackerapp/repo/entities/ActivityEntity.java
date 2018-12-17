package tago.timetrackerapp.repo.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
        /*(foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "id",
        childColumns = "category_id"))*/
public class ActivityEntity implements EntityInt {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private int color;
    private int icon;

    @ColumnInfo(name = "category_id")
    private long categoryId;

    @Ignore
    private Category category;

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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long id) {
        categoryId = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null) {
            setCategoryId(0);
        } else {
            setCategoryId(category.id);
        }
        this.category = category;
    }

    @Override
    public String toString() {
        return "ActivityEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                ", icon=" + icon +
                ", categoryId=" + categoryId +
                ", category=" + category +
                '}';
    }
}
