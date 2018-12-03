package tago.timetrackerapp.repo.entities;

public abstract class Entity {

    protected long id;

    public Entity() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
