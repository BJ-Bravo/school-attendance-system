package com.froebelenglishschool.attendance_management.repository;

import com.froebelenglishschool.attendance_management.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAssignedClassOrderByRollNo(String assignedClass);
}
