package org.fitness.activityservice.dto;

import lombok.Data;
import org.fitness.activityservice.model.ActivityType;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityRequest {
    private String userID;
    private ActivityType type;
    private Integer durantion;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String,Object> additionalMetrics;
}
