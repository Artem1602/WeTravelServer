package com.pkk.wetravelserver.controllers;

import com.pkk.wetravelserver.services.storage.StreamingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;


@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    private final StreamingService streamingService;

    @GetMapping("/all")
    public String allAccess() {
        return "public API";
    }

    @GetMapping(value = "/all/{title}")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> getVideo(
            @PathVariable("title")
            String title) {
        try {
            return streamingService.loadPartialMediaFile("W:\\WeTravelFileStorage\\1\\" + title, "", logger);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "user API";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String moderatorAccess() {
        return "moderator API";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "admin API";
    }
}
