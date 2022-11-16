package com.pkk.wetravelserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_data")
@Getter @Setter @NoArgsConstructor
public class UserData {

    @Id
    @Column(name = "user_id", nullable = false)
    private long user_id;

    private String status;

    private String info;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}
