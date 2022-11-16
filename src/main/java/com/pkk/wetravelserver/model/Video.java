package com.pkk.wetravelserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @Column(nullable = false)
    private String storagePath;

    @Column(nullable = false)
    private LocalDateTime uploadingTime;

    @Column(nullable = false)
    private String location;

    public Video(String name, String storagePath, LocalDateTime uploadingTime, String location) {
        this.name = name;
        this.storagePath = storagePath;
        this.uploadingTime = uploadingTime;
        this.location = location;
    }
}
