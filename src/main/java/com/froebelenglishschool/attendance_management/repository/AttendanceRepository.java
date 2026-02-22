package com.froebelenglishschool.attendance_management.repository;

import com.froebelenglishschool.attendance_management.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent_AssignedClassAndDateBetween(String assignedClass, LocalDate startDate, LocalDate endDate);
    List<Attendance> findByDateAndStudent_AssignedClass(LocalDate date, String assignedClass);
    Optional<Attendance> findByStudentAndDate(Student student, LocalDate date);
    List<Attendance> findByStudent_AssignedClassAndDate(String assignedClass, LocalDate date);
}

