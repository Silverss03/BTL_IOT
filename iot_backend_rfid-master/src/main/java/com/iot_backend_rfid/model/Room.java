package com.iot_backend_rfid.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Table
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    private String roomName;

    private String roomCode;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Collection<Device> devices=new HashSet<>();
}
