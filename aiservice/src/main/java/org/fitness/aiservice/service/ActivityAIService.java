package org.fitness.aiservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fitness.aiservice.model.Activity;
import org.fitness.aiservice.model.Recommendation;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    public Recommendation generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getRecommendations(prompt);
        log.info("Response from AI {} ",aiResponse);
        return processAIResponse(activity,aiResponse);
    }

    private Recommendation processAIResponse(Activity activity, String aiResponse) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);
            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .get("parts")
                    .get(0)
                    .path("text");


            String jsonContent = textNode.asText().trim();

            if (jsonContent.startsWith("```")) {
                jsonContent = jsonContent.replaceFirst("^```(?:json)?\\s*", "");
                jsonContent = jsonContent.replaceFirst("\\s*```\\s*$", "");
            }

            jsonContent = jsonContent.trim();


            log.info("Response from cleaned AI {} ",jsonContent);

            JsonNode analysisJson = mapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis,analysisNode,"overall","Overall");
            addAnalysisSection(fullAnalysis,analysisNode,"pace","Pace");
            addAnalysisSection(fullAnalysis,analysisNode,"heartRate","Heart Rate");
            addAnalysisSection(fullAnalysis,analysisNode,"caloriesBurned","Calories Burned: ");

            List<String> improvements = extractImprovements(analysisJson.path("Improvement"));
            List<String> suggestions = extractsuggetions(analysisJson.path("Suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserID())
                    .type(activity.getType().toString())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();

        }
        catch (Exception e){
            e.printStackTrace();
            return createDefaultRecommendation(activity);

        }
    }

    private Recommendation createDefaultRecommendation(Activity activity) {

        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserID())
                .type(activity.getType().toString())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestions(Collections.singletonList("Consider consulting a fitness consultant"))
                .safety(Arrays.asList(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body"
                ))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safety = new ArrayList<>();
        if(safetyNode.isArray()){
            safetyNode.forEach(item -> safety.add(item.asText()));
        }
        return safety.isEmpty() ?
                Collections.singletonList("Follow general safety guidelines"):
                safety;
    }

    private List<String> extractsuggetions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if(suggestionsNode.isArray()){
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s", workout, description));

            });
        }
        return suggestions.isEmpty() ?
                Collections.singletonList("No Specific suggestions provided"):
                suggestions;

    }

    private List<String> extractImprovements(JsonNode improvementsNode) {

        List<String> improvements = new ArrayList<>();
        if(improvementsNode.isArray()){
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s", area, detail));

            });
        }
        return improvements.isEmpty() ?
                Collections.singletonList("No Specific improvements provided"):
                improvements;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
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
