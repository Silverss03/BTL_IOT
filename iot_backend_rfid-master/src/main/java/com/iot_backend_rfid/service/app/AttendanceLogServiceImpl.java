package com.iot_backend_rfid.service.app;

import com.iot_backend_rfid.model.AttendanceLog;
import com.iot_backend_rfid.repository.AttendanceLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceLogServiceImpl implements AttendanceLogService{
    private final AttendanceLogRepository attendanceLogRepository;

    public void saveAttendanceLog(AttendanceLog attendanceLog){
        attendanceLogRepository.saveAndFlush(attendanceLog);
    }
}
