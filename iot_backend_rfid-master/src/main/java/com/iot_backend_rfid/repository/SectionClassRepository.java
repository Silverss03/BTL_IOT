package com.iot_backend_rfid.repository;

import com.iot_backend_rfid.model.SectionClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SectionClassRepository extends JpaRepository<SectionClass, Integer> {
    @Query(value = "SELECT sc.* FROM section_class sc " +
            "JOIN room r ON sc.room_id = r.room_id " +
            "WHERE r.room_code = :roomCode " +
            "AND :dateTime BETWEEN DATE_SUB(sc.start_time, INTERVAL 10 MINUTE) AND sc.end_time",
            nativeQuery = true)
    SectionClass findSectionByRoomAndTime(@Param("roomCode") String roomCode,
                                          @Param("dateTime") LocalDateTime dateTime);

}
