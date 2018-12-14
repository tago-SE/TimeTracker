package tago.timetrackerapp.repo;

import tago.timetrackerapp.model.Category;
import tago.timetrackerapp.repo.entities.CategoryEntity;

public class Converter {

    public static CategoryEntity toEntity(Category m) {
        CategoryEntity e = new CategoryEntity();
        e.setId(m.getId());
        e.setName(m.getName());
        e.setColor(m.getColor());
        return e;
    }

    public static Category toModel(CategoryEntity e) {
        Category m = new Category();
        m.setId(e.getId());
        m.setName(e.getName());
        m.setColor(e.getColor());
        return m;
    }
}
