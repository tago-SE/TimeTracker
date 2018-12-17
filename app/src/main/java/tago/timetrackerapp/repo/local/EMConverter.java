package tago.timetrackerapp.repo.local;

import java.util.ArrayList;
import java.util.List;

import tago.timetrackerapp.model.Activity;
import tago.timetrackerapp.repo.entities.ActivityEntity;

/**
 * This class responsible for Entity-to-Model and Model-to-Entity conversions.
 */
public class EMConverter {



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
        //entity.setCategory(EMConverter.toEntity(model.getCategory()));
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
       // model.setCategory(EMConverter.toModel(entity.getCategory()));
        return model;
    }

}
