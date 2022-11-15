package com.pkk.wetravelserver.controllers;

import com.pkk.wetravelserver.model.UserData;
import com.pkk.wetravelserver.services.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userdata")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserDataController {
    private final UserDataService service;

    @PutMapping("/fill")
    public UserData fillUserData(@RequestBody UserData userData) throws Exception {
        return service.fillUserData(userData);
    }
}
