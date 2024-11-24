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
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teacherId;

    private String teacherCode;

    private String teacherDepartment;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;

}
