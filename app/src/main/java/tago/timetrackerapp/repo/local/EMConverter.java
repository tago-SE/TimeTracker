package tago.timetrackerapp.repo.local;

import java.util.ArrayList;
import java.util.List;

import tago.timetrackerapp.model.Activity;
import tago.timetrackerapp.model.Category;
import tago.timetrackerapp.repo.entities.ActivityEntity;
import tago.timetrackerapp.repo.entities.CategoryEntity;

/**
 * This class responsible for Entity-to-Model and Model-to-Entity conversions.
 */
public class EMConverter {


    /*
     *      Category conversion
     */

    public static CategoryEntity toEntity(Category model) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setColor(model.getColor());
        return entity;
    }

    public static Category toModel(CategoryEntity e) {
        Category m = new Category();
        m.setId(e.getId());
        m.setName(e.getName());
        m.setColor(e.getColor());
        return m;
    }

    /*
     *      Activity conversion
     */

    public static List<ActivityEntity> toEntities(List<Activity> models) {

        return null;
    }

    public static ActivityEntity toEntity(Activity model) {
        ActivityEntity entity = new ActivityEntity();
        entity.setId(model.getId());
        entity.setColor(model.getColor());
        entity.setIcon(model.getIcon());
        entity.setName(model.getName());
        entity.setCategory(EMConverter.toEntity(model.getCategory()));
        return entity;
    }

    public static List<Activity> toModels(List<ActivityEntity> entities) {
        List<Activity> models = new ArrayList<>();
        if (entities != null)
            for (ActivityEntity entity : entities) {
                models.add(EMConverter.toModel(entity));
            }
        return models;
    }

    public static Activity toModel(ActivityEntity entity) {
        Activity model = new Activity();
        model.setId(entity.getId());
        model.setColor(entity.getColor());
        model.setIcon(entity.getIcon());
        model.setName(entity.getName());
        model.setCategory(EMConverter.toModel(entity.getCategory()));
        return model;
    }

}
