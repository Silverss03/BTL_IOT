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
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    private String adminCode;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
