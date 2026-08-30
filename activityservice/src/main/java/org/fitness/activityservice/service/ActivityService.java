package org.fitness.activityservice.service;

import lombok.RequiredArgsConstructor;
import org.fitness.activityservice.ActivityRepository;
import org.fitness.activityservice.dto.ActivityRequest;
import org.fitness.activityservice.dto.ActivityResonse;
import org.fitness.activityservice.model.Activity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    public ActivityResonse trackActivity(ActivityRequest request) {
        Activity activity= Activity.builder()
                .userID(String.valueOf(request.getType()))
                .type(request.getType())
                .durantion(request.getDurantion())
                .caloriesBurned(request.getCaloriesBurned())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity=activityRepository.save(activity);
        return mapToResponse(savedActivity);

    }

    private ActivityResonse mapToResponse(Activity activity) {
        ActivityResonse response = new ActivityResonse();
        response.setId(activity.getId());
        response.setUserID(activity.getUserID());
        response.setType(activity.getType());
        response.setDurantion(activity.getDurantion());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }
}
