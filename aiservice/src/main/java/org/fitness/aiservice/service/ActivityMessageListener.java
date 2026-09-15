package org.fitness.aiservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fitness.aiservice.model.Activity;
import org.fitness.aiservice.model.Recommendation;
import org.fitness.aiservice.repository.RecommendationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private  final ActivityAIService activityAIService;
    private  final RecommendationRepository recommendationRepository;

    @KafkaListener(topics="${kafka.topic.name}",groupId = "activity-processor-group")
    public void processActivity(Activity activity) {
        log.info("Received Activity for processing:{}",activity.getUserID());
        Recommendation recommendation = activityAIService.generateRecommendation(activity);
        recommendationRepository.save(recommendation);


    }

}
