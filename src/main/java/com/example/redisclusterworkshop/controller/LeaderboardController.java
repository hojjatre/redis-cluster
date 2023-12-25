package com.example.redisclusterworkshop.controller;

import com.example.redisclusterworkshop.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @PostMapping("/addScore")
    public void addScore(@RequestParam("user") String user, @RequestParam("score") Integer score) {
        leaderboardService.addScore(user, score);
    }

    @GetMapping("/scores")
    public ResponseEntity<Map<String, Integer>> getScores() {
        return ResponseEntity.ok(leaderboardService.getScores());
    }
}
