package com.pkk.wetravelserver.controllers;

import com.pkk.wetravelserver.javabean.MessageResponse;
import com.pkk.wetravelserver.model.Video;
import com.pkk.wetravelserver.services.UserService;
import com.pkk.wetravelserver.services.VideoService;
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

    private final Logger logger = LoggerFactory.getLogger(VideoController.class);

    @PostMapping("/video/upload")
    public ResponseEntity<?> handleVideoUpload(@RequestParam("video") MultipartFile multipartFile, @RequestParam("user_id") Long userid, @RequestParam("location") String location) {
        File userIdDirectory = initUserIdDir(userid);
        String name = multipartFile.getOriginalFilename();
        logger.info("#handleUpload user_id: {}, catch file: {}", userid, name);
        try {
            saveMultipartFile(multipartFile, userIdDirectory, name);
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
        File userIdDirectory = initUserIdDir(userid);
        String name = multipartFile.getOriginalFilename();
        String extension = name.substring(name.indexOf('.'));
        logger.info("#imgUpload user_id: {}", userid);
        try {
            saveMultipartFile(multipartFile, userIdDirectory, userImg + extension);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        userService.saveUserImgPath(userid, userIdDirectory.getAbsolutePath() + userImg + extension);
        return ResponseEntity.ok(new MessageResponse("Img uploaded successfully."));
    }

    private File initUserIdDir(Long userid) {
        File userIdDirectory = new File(pathToStorage + File.separator + userid);
        if (!userIdDirectory.exists()) {
            logger.info("Personal user folder creation status: {}", userIdDirectory.mkdir());
        }
        return userIdDirectory;
    }

    private Boolean saveMultipartFile(MultipartFile multipartFile, File userIdDirectory, String fileName) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream();
             OutputStream outputStream = new FileOutputStream(userIdDirectory.getPath() + File.separator + fileName)
        ) {
            IOUtils.copy(inputStream, outputStream);
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
