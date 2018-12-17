package tago.timetrackerapp.repo.db.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

@Dao
interface BaseDao<E> {

    @Insert
    void insert(E... entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(E... entity);

    @Update
    void update(E... entities);

    @Delete
    void delete(E... entities);



}
