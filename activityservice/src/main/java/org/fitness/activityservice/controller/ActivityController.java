package org.fitness.activityservice.controller;

import lombok.AllArgsConstructor;
import org.fitness.activityservice.ActivityserviceApplication;
import org.fitness.activityservice.dto.ActivityRequest;
import org.fitness.activityservice.dto.ActivityResonse;
import org.fitness.activityservice.service.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class ActivityController {
    private ActivityService activityService;


    @PostMapping
    public ResponseEntity<ActivityResonse> trackActivity(@RequestBody ActivityRequest request){
        return ResponseEntity.ok(activityService.trackActivity(request));
    }

}
