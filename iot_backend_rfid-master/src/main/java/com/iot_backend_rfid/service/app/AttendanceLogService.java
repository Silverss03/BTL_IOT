package com.iot_backend_rfid.service.app;

import com.iot_backend_rfid.model.AttendanceLog;

public interface AttendanceLogService {
    void saveAttendanceLog(AttendanceLog attendanceLog);
}
