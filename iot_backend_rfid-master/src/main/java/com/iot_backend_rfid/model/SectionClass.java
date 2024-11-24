package com.iot_backend_rfid.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sectionClassId;

    private String sectionClassName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="TeacherSectionClass",
            joinColumns = @JoinColumn(name = "sectionClassId",referencedColumnName = "sectionClassId"),
            inverseJoinColumns = @JoinColumn(name = "teacherId",referencedColumnName = "teacherId")
    )
    private Collection<Teacher> teachers=new HashSet<>();

}

