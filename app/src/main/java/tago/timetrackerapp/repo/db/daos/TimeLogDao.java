package tago.timetrackerapp.repo.db.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import tago.timetrackerapp.repo.entities.TimeLog;

@Dao
public interface TimeLogDao extends BaseDao<TimeLog> {

    @Query("SELECT * FROM timelog")
    List<TimeLog> getAll();

    @Query("SELECT * FROM timelog WHERE id=:id")
    TimeLog get(long id);

    @Query("SELECT * FROM timelog ORDER BY stop DESC LIMIT 1")
    TimeLog getLast();
}
