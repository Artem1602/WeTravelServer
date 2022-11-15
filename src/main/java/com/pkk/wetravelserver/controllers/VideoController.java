package com.pkk.wetravelserver.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/api/storage")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
@RequiredArgsConstructor
public class VideoController {

    @Value("${app.files.storage.path}")
    private String pathToStorage;

    private final Logger logger = LoggerFactory.getLogger(VideoController.class);

    @PostMapping("/upload")
    public ResponseEntity<?> handleUpload(@RequestParam("user-file") MultipartFile multipartFile, @RequestParam("user_id") String userid) {

        File userIdDirectory = new File(pathToStorage + File.separator + userid);
        if (!userIdDirectory.exists()) {
            logger.info("Personal user folder creation status: {}", userIdDirectory.mkdir());
        }

        String name = multipartFile.getOriginalFilename();
        logger.info("#handleUpload user_id: {}, catch file: {}", userid, name);

        try (InputStream inputStream = multipartFile.getInputStream();
             OutputStream outputStream = new FileOutputStream(userIdDirectory.getPath() + File.separator + name)
        ) {
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            logger.error("#handleUpload " + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok("File uploaded successfully.");
    }
}
