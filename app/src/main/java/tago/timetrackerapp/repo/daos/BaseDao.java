package tago.timetrackerapp.repo.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import tago.timetrackerapp.repo.entities.ActivityEntity;
import tago.timetrackerapp.repo.entities.EntityInt;

@Dao
interface BaseDao<E> {

    @Insert
    void insertAll(E... entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(E... entity);

    @Update
    void update(E... entities);

    @Delete
    void delete(E... entities);



}
