package tago.timetrackerapp.repo.local.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import tago.timetrackerapp.repo.entities.Category;

@Dao
public interface CategoryDao extends BaseDao<Category> {

    @Query("SELECT * FROM category")
    List<Category> getAll();

    @Query("SELECT * FROM category WHERE id=:id")
    Category get(long id);
}
