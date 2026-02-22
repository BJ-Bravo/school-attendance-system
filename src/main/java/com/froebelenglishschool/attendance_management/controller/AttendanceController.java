package com.froebelenglishschool.attendance_management.controller;

import com.froebelenglishschool.attendance_management.entity.*;
import com.froebelenglishschool.attendance_management.repository.*;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Controller
public class AttendanceController {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private AttendanceRepository attendanceRepo;

//    @Autowired
//    private Student student;//TESTING BEAN

    @GetMapping("/attendance")
    public String attendancePage(@RequestParam(value = "className", required = false) String className,
                                 HttpSession session, Model model) {
        String assignedClass = className != null ? className : (String) session.getAttribute("assignedClass");
        if (assignedClass == null) {
            return "redirect:/login";
        }

        //System.out.println(student.getFirstName() +" "+student.getLastName()+" " +student.getRollNo());//TESTING BEAN
        //updating session with the assigned class
        session.setAttribute("assignedClass", assignedClass);

        List<Student> students = studentRepo.findByAssignedClassOrderByRollNo(assignedClass);

        YearMonth currentYearMonth = YearMonth.now();
        LocalDate firstDate = currentYearMonth.atDay(1);
        LocalDate lastDate = currentYearMonth.atEndOfMonth();

        List<LocalDate> monthDates = new ArrayList<>();
        for (int i = 1; i <= lastDate.getDayOfMonth(); i++) {
            monthDates.add(currentYearMonth.atDay(i));
        }

        //this is to fetch the attendance for this month
        List<Attendance> attendanceList = attendanceRepo.findByStudent_AssignedClassAndDateBetween(assignedClass, firstDate, lastDate);

        Map<Long, Map<LocalDate, Attendance.Status>> attendanceMap = new HashMap<>();
        for (Attendance a : attendanceList) {
            if (a.getStudent() != null) {
                attendanceMap.putIfAbsent(a.getStudent().getId(), new HashMap<>());
                attendanceMap.get(a.getStudent().getId()).put(a.getDate(), a.getStatus());
            }
        }

        LocalDate today = LocalDate.now();

        model.addAttribute("students", students);
        model.addAttribute("dates", monthDates);
        model.addAttribute("attendanceMap", attendanceMap);
        model.addAttribute("today", today);
        model.addAttribute("className", assignedClass);

        return "attendance";
    }

    @PostMapping("/attendance/submit")
    @ResponseBody
    public String submitAttendance(@RequestBody Map<Long, String> attendanceData, HttpSession session) {
        String assignedClass = (String) session.getAttribute("assignedClass");
        if (assignedClass == null) {
            return "error";
        }
        LocalDate today = LocalDate.now();

        attendanceData.forEach((studentId, statusStr) -> {
            Optional<Student> studentOpt = studentRepo.findById(studentId);
            if (studentOpt.isPresent() && assignedClass.equals(studentOpt.get().getAssignedClass())) {
                Student student = studentOpt.get();
                Attendance.Status status = "P".equalsIgnoreCase(statusStr) ? Attendance.Status.PRESENT : Attendance.Status.ABSENT;

                Optional<Attendance> attendanceOpt = attendanceRepo.findByStudentAndDate(student, today);
                Attendance attendance;
                if (attendanceOpt.isPresent()) {
                    attendance = attendanceOpt.get();
                    attendance.setStatus(status);
                } else {
                    attendance = new Attendance();
                    attendance.setStudent(student);
                    attendance.setDate(today);
                    attendance.setStatus(status);
                }
                attendanceRepo.save(attendance);
            }
        });

        return "success";
    }

}