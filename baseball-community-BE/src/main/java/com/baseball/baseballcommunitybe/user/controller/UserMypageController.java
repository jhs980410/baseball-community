package com.baseball.baseballcommunitybe.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mypage")
public class UserMypageController {


    @GetMapping("")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("");
    }
}
