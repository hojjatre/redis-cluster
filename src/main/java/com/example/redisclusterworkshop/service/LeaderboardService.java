package com.example.redisclusterworkshop.service;

import com.example.redisclusterworkshop.configutration.RedissonConfig;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaderboardService {

    RedissonConfig redissonConfig;
    LeaderboardCache leaderboardCache;

    public LeaderboardService(RedissonConfig redissonConfig, LeaderboardCache leaderboardCache) {
        this.redissonConfig = redissonConfig;
        this.leaderboardCache = leaderboardCache;
    }

    public void addScore(String user, Integer score) {
        RMap<String, Integer> leaderboard = redissonConfig.redissonClient().getMap("leaderboard");
        leaderboard.put(user, score);
    }

    public Map<String, Integer> getScores() {
        RMap<String, Integer> leaderboard = redissonConfig.redissonClient().getMap("leaderboard");
        Map<String, Integer> data = new HashMap<>();
        leaderboard.keySet().stream().forEach(
                e -> {
                    data.put(e, leaderboard.get(e));
                }
        );
        return data;
    }

    @Scheduled(fixedRate = 10000)
    public void logLeaderboard() {
        String filePath = "leaderboard_log.txt";

        RLock lock = redissonConfig.redissonClient().getLock("leaderboard_log_lock");
        try {
            if (lock.tryLock(0, 1, TimeUnit.SECONDS)) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
                    writer.println("Leaderboard Log - " + System.currentTimeMillis());

                    Map<String, Integer> scores = getScores();
                    for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                        writer.println(entry.getKey() + ": " + entry.getValue());
                    }

                    writer.println();
                    writer.flush();
                    log.info("All data from cache log in file.");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
