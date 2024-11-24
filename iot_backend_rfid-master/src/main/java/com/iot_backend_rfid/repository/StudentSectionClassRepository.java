package com.iot_backend_rfid.repository;

import com.iot_backend_rfid.model.StudentSectionClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StudentSectionClassRepository extends JpaRepository<StudentSectionClass, Integer> {
    @Query("SELECT ssc " +
            "FROM StudentSectionClass ssc " +
            "WHERE ssc.student.studentCode = :studentCode " +
            "AND ssc.sectionClass.sectionClassId = :sectionClassId")
    Optional<StudentSectionClass> findStudentSectionClassByStudentCodeAndSectionClassId(
            @Param("studentCode") String studentCode,
            @Param("sectionClassId") Integer sectionClassId);


}
