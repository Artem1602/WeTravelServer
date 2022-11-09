package com.pkk.wetravelserver.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_data")
@Getter @Setter @NoArgsConstructor
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String status;

    private String info;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

}
