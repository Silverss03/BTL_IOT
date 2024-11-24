package com.iot_backend_rfid.service.app;

import com.iot_backend_rfid.common.Constants;
import com.iot_backend_rfid.model.AttendanceLog;
import com.iot_backend_rfid.model.SectionClass;
import com.iot_backend_rfid.model.StudentSectionClass;
import com.iot_backend_rfid.repository.SectionClassRepository;
import com.iot_backend_rfid.repository.StudentSectionClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{
    private final SectionClassRepository sectionClassRepository;
    private final StudentSectionClassRepository studentSectionClassRepository;
    private final AttendanceLogService attendanceLogService;

    @Override
    public String checkTakeAttendance(String payload) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String[] data = payload.split("&&");
        if (data.length < 3) {
            return Constants.CHECKIN_FAIL;
        }

        try {
            String roomCode = data[0];
            String studentCode = data[1];
            LocalDateTime dateTime = LocalDateTime.parse(data[2], formatter);

            SectionClass sectionClass = sectionClassRepository.findSectionByRoomAndTime(roomCode, dateTime);
            if (sectionClass == null) {
                return Constants.CHECKIN_FAIL;
            }

            Optional<StudentSectionClass> studentSectionClassOptional = studentSectionClassRepository
                    .findStudentSectionClassByStudentCodeAndSectionClassId(studentCode, sectionClass.getSectionClassId());

            if (studentSectionClassOptional.isEmpty()) {
                return Constants.CHECKIN_FAIL;
            }

            AttendanceLog attendanceLog=AttendanceLog.builder()
                    .checkInTime(dateTime)
                    .studentSectionClass(studentSectionClassOptional.get())
                    .build();
            attendanceLogService.saveAttendanceLog(attendanceLog);

            LocalDateTime startTime = sectionClass.getStartTime();
            Duration diff = Duration.between(startTime, dateTime);
            long diffMinutes = diff.toMinutes();

            if (diffMinutes >= -10 && diffMinutes <= 5) {
                return Constants.CHECKIN_SUCCESSFUL;
            } else if (diffMinutes > 5 && diffMinutes <= 30) {
                return (diffMinutes-5) + Constants.CHECKIN_LATE;
            } else {
                return Constants.CHECKIN_FAIL;
            }
        } catch (Exception e) {
            return Constants.CHECKIN_FAIL;
        }
    }
}
