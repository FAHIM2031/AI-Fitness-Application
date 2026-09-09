package org.fitness.activityservice.service;

import lombok.RequiredArgsConstructor;
import org.fitness.activityservice.ActivityRepository;
import org.fitness.activityservice.dto.ActivityRequest;
import org.fitness.activityservice.dto.ActivityResonse;
import org.fitness.activityservice.model.Activity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    private final UserValidationService userValidationService;
    private final KafkaTemplate<String,Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public ActivityResonse trackActivity(ActivityRequest request) {

        boolean isValidUser = userValidationService.validateUser(request.getUserID());

        if (!isValidUser){
            throw  new RuntimeException("Invalid user ID"+ request.getUserID());
        }
        Activity activity= Activity.builder()
                .userID(request.getUserID())
                .type(request.getType())
                .durantion(request.getDurantion())
                .caloriesBurned(request.getCaloriesBurned())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity=activityRepository.save(activity);

        try {
            kafkaTemplate.send(topicName, savedActivity.getUserID(),savedActivity);
        }catch(Exception e){
            e.printStackTrace();
        }
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
