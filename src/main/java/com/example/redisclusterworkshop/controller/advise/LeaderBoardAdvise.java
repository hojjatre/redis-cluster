package com.example.redisclusterworkshop.controller.advise;

import com.example.redisclusterworkshop.exception.RateLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LeaderBoardAdvise {

    @ExceptionHandler({RateLimitException.class})
    ResponseEntity<Object> rateLimit(){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("بیش از حد مجاز درخواست دادید. صبر کنید.");
    }
}
