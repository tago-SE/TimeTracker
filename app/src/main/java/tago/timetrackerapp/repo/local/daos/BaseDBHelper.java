package tago.timetrackerapp.repo.local.daos;

import tago.timetrackerapp.repo.local.db.AppDatabase;

public abstract class BaseDBHelper<T, D> {

    protected AppDatabase db;
    protected BaseDao dao;

    public BaseDBHelper(AppDatabase db, BaseDao<D> dao) {
        this.db = db;
        this.dao = dao;
    }




}
