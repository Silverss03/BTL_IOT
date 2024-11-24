package com.iot_backend_rfid.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attendanceLogId;

    private LocalDateTime checkInTime;

    @ManyToOne
    @JoinColumn(name = "StudentSectionClassId")
    private StudentSectionClass studentSectionClass;
}
