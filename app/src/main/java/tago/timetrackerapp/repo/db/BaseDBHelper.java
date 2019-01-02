package tago.timetrackerapp.repo.db;

public class BaseDBHelper {

    // timestamp for when the DB records were last modified (delete, insert or update).
    protected long timestampMilliseconds;

    public long getLastModifiedMilliseconds() {
        return timestampMilliseconds;
    }
}
