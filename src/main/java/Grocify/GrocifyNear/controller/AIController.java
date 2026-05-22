package Grocify.GrocifyNear.controller;

import Grocify.GrocifyNear.service.AIRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

    private final AIRecommendationService aiRecommendationService;

    public AIController(AIRecommendationService aiRecommendationService) {
        this.aiRecommendationService = aiRecommendationService;
    }

    @GetMapping("/ask")
    public ResponseEntity<?> askAI(@RequestParam String question) {

        return ResponseEntity.ok(
                aiRecommendationService
                        .answerQuestion(question)
        );
    }
}