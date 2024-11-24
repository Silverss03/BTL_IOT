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
public class StudentSectionClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentSectionClassId;

    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "sectionClassId")
    private SectionClass sectionClass;
}
