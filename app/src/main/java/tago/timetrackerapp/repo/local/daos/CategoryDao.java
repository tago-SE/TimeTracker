package tago.timetrackerapp.repo.local.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import tago.timetrackerapp.repo.entities.CategoryEntity;

@Dao
public interface CategoryDao extends BaseDao<CategoryEntity> {

    @Query("SELECT * FROM categoryentity")
    List<CategoryEntity> getAll();

    @Query("SELECT * FROM categoryentity WHERE id=:id")
    CategoryEntity get(long id);
}
