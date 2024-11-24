package com.iot_backend_rfid.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deviceId;

    private String deviceName;

    private Boolean deviceStatus;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;
}
