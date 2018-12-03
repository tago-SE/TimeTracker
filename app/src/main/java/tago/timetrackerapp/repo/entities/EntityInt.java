package tago.timetrackerapp.repo.entities;

import android.arch.persistence.room.Entity;

@Entity
public interface EntityInt {

    void setId(long id);
    long getId();
}
