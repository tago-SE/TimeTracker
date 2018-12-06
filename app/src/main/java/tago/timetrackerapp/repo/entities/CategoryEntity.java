package tago.timetrackerapp.repo.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class CategoryEntity implements EntityInt {

    @NonNull
    @PrimaryKey
    private long id;

    @Override
    public void setId(long id) {

    }

    @Override
    public long getId() {
        return 0;
    }
}
