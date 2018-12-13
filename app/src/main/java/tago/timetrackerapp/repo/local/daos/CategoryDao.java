package tago.timetrackerapp.repo.local.daos;

import android.arch.persistence.room.Dao;

import tago.timetrackerapp.repo.entities.CategoryEntity;

@Dao
public interface CategoryDao extends BaseDao<CategoryEntity> {

}
