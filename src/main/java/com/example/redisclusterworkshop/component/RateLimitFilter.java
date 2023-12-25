package com.example.redisclusterworkshop.component;

import com.example.redisclusterworkshop.exception.RateLimitException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RedissonClient redissonClient;

    public RateLimitFilter(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String userIdentifier = extractUserIdentifier(request);

        String rateLimitKey = "rate_limit:" + userIdentifier;

        RBucket<Long> bucket = redissonClient.getBucket(rateLimitKey);

        long requestCount = bucket.get() != null ? bucket.get() : 0L;
        System.out.println("request count: " + requestCount + "user:" + rateLimitKey);
        if (requestCount < 2) {
            bucket.set(requestCount + 1, 10, TimeUnit.SECONDS);
            filterChain.doFilter(request, response);
        } else {
            throw new RateLimitException();
        }

        filterChain.doFilter(request, response);
    }

    private String extractUserIdentifier(HttpServletRequest request) {
        return request.getParameter("user");
    }
    @Bean
    public FilterRegistrationBean<RateLimitFilter> myCustomerFilter()
    {
        FilterRegistrationBean<RateLimitFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new RateLimitFilter(redissonClient));
        bean.addUrlPatterns("/api/*");

        return bean;
    }
}

