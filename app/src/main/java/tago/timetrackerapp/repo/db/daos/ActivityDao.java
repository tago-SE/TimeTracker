package tago.timetrackerapp.repo.db.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import tago.timetrackerapp.repo.entities.Activity;

@Dao
public interface ActivityDao extends BaseDao<Activity> {

    @Query("SELECT * FROM activity")
    List<Activity> getAll();


    @Query("SELECT * FROM activity WHERE id=:id")
    Activity get(long id);

    @Query("SELECT * FROM activity WHERE category_id=:categoryId")
    List<Activity> getAllByCategoryId(long categoryId);
}
