package com.pkk.wetravelserver.controllers;

import com.pkk.wetravelserver.javabean.MessageResponse;
import com.pkk.wetravelserver.model.Video;
import com.pkk.wetravelserver.services.UserService;
import com.pkk.wetravelserver.services.VideoService;
import com.pkk.wetravelserver.util.StorageUtil;
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
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/storage")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
@RequiredArgsConstructor
public class VideoController {

    @Value("${app.storage.path}")
    private String pathToStorage;

    @Value("${app.storage,default.userimg}")
    private String userImg;
    private final VideoService videoService;

    private final UserService userService;

    private final StorageUtil storageUtil;
    private final Logger logger = LoggerFactory.getLogger(VideoController.class);

    @PostMapping("/video/upload")
    public ResponseEntity<?> handleVideoUpload(@RequestParam("video") MultipartFile multipartFile, @RequestParam("user_id") Long userid, @RequestParam("location") String location) {
        File userIdDirectory = storageUtil.initUserIdDir(pathToStorage + File.separator + userid, logger);
        String name = multipartFile.getOriginalFilename();
        logger.info("#handleUpload user_id: {}, catch file: {}", userid, name);
        try {
            storageUtil.saveMultipartFile(multipartFile, userIdDirectory, name, logger);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        Video video = new Video(
                name,
                userIdDirectory.getPath() + File.separator + name,
                LocalDateTime.now(),
                location
        );
        videoService.saveInstance(video);
        return ResponseEntity.ok(new MessageResponse("Video uploaded successfully."));
    }

    @GetMapping("/videos")
    public List<Video> getVideoList() {
        return videoService.getAllVideo();
    }

    @PostMapping("/img/upload")
    public ResponseEntity<?> uploadUserImg(@RequestParam("user_img") MultipartFile multipartFile, @RequestParam("user_id") Long userid) {
        File userIdDirectory =  storageUtil.initUserIdDir(pathToStorage + File.separator + userid, logger);
        String name = multipartFile.getOriginalFilename();
        String extension = name.substring(name.indexOf('.'));
        logger.info("#imgUpload user_id: {}", userid);
        try {
            storageUtil.saveMultipartFile(multipartFile, userIdDirectory, userImg + extension, logger);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        userService.saveUserImgPath(userid, userIdDirectory.getAbsolutePath() + userImg + extension);
        return ResponseEntity.ok(new MessageResponse("Img uploaded successfully."));
    }

}
