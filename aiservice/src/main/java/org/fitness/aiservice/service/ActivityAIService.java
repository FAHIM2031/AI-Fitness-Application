package org.fitness.aiservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fitness.aiservice.model.Activity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public void generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        log.info("Response from AI {} "+geminiService.getRecommendations(prompt));
    }

    private String createPromptForActivity(Activity activity) {
        return String.format(
                """
                Analyze this fitness activity and provide detailed recommendations in the following exact format:
                {
                    "analysis": {
                    "overall": "Overall analysis here",
                    "pace": "Pace analysis here",
                    "heartRate": "Heart rate analysis here",
                    "caloriesBurned": "Calories analysis here",
               
                    },
                    "Improvement": [
                    {
                      "area": "Area name",
                      "recommendation": "Detailed recommendation"
                    }],
                    
                    "Suggesions": [
                    {
                        "workout": "workout name",
                        "description": "Detailed workout description",
                        }],
                        
                    "safety": [
                        "Safety point 1",
                        "safety point 2"
                        ]
             
                       }
                       
                       Analze this activity:
                       Activity Type: %s
                       Duration: %d minutes
                       calories Burned: %d minutes
                       Additional Metries: %s
                       
                       
                       provide detailed analysis focusing on perfomance,improvements, next workout suggesions and safety guidelines.
                       Ensure the response follows the EXACT JSON format shown above.
                       """,
                                activity.getType(),
                                activity.getDurantion(),
                                activity.getCaloriesBurned(),
                                activity.getAdditionalMetrics()


        );
    }

}
