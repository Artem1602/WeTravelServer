package com.pkk.wetravelserver.services.model;

import com.pkk.wetravelserver.model.Video;
import com.pkk.wetravelserver.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository repository;

    public Video saveInstance(Video video){
        return repository.save(video);
    }

    public List<Video> getAllVideo(){
        return repository.findAll();
    }
}
