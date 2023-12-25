package com.example.redisclusterworkshop.service;

import com.example.redisclusterworkshop.configutration.RedissonConfig;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaderboardCache {
    private static String leaderboardKey = "leaderboard";
    RedissonConfig redissonConfig;

    @NonFinal
    RMap<String, Integer> leaderboard;
    public LeaderboardCache(RedissonConfig redissonConfig) {
        this.redissonConfig = redissonConfig;
    }

    @PostConstruct
    public void data() {
        log.info("Start Caching...");
        leaderboard = redissonConfig.redissonClient().getMap(leaderboardKey);
    }
}
