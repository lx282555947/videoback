package com.example.video.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VideoTestControllerTest {
    @Autowired
    private VideoTestController videoTestController;

    @Test
    void testVideo() throws Exception {
        String success = videoTestController.testVideo();
        System.out.println(success);
    }
}