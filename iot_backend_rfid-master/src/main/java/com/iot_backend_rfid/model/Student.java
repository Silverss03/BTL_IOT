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
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    private String studentCode;

    private String studentCourse;

    private String fieldStudy;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
