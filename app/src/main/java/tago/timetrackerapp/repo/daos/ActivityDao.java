package tago.timetrackerapp.repo.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import tago.timetrackerapp.repo.entities.ActivityEntity;

@Dao
public interface ActivityDao extends BaseDao<ActivityEntity> {

    @Query("SELECT * FROM activityentity")
    List<ActivityEntity> getAll();
}
