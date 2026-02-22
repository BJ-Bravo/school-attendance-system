package com.froebelenglishschool.attendance_management.controller;

import com.froebelenglishschool.attendance_management.entity.Student;
import com.froebelenglishschool.attendance_management.repository.StudentRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;

    @GetMapping("/addStudent")
    public String addStudentPage() {
        return "addStudent";
    }

    @PostMapping("/addStudent")
    public String addStudent(@RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam int rollNo,
                             HttpSession session,
                             Model model) {
        String assignedClass = (String) session.getAttribute("assignedClass");
        if (assignedClass == null) {
            return "redirect:/login";
        }
        Student s = new Student();
        s.setFirstName(firstName);
        s.setLastName(lastName);
        s.setRollNo(rollNo);
        s.setAssignedClass(assignedClass);
        studentRepo.save(s);

        model.addAttribute("message", "Student Registered Successfully");
        return "/addStudent";
    }

    @GetMapping("/editStudent")
    public String editStudentPage(@RequestParam Long id, Model model, HttpSession session) {
        String assignedClass = (String) session.getAttribute("assignedClass");
        if (assignedClass == null) {
            return "redirect:/login";
        }
        Optional<Student> studentOpt = studentRepo.findById(id);
        /*Optional<Student>: record may or may not exist ..instead of returning null .Spring returns
        * Optional<Student> which is a wrapper object representing : value persent or absent
        * studentOpt: contains Student object ..it check isPresent() or isEmpty() it is necessary when/for calling .get()
        * studentRepo.findById(id): this is a method from JpaRepository ..find by primary key id ..return type : Optional<Student> */
        
        if (studentOpt.isPresent() && assignedClass.equals(studentOpt.get().getAssignedClass())) {
            model.addAttribute("student", studentOpt.get());
            return "editStudent"; // Create this template
        }
        return "redirect:/attendance";
    }

    @PostMapping("/editStudent")
    public String editStudent(@RequestParam Long id,
                              @RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam int rollNo,
                              HttpSession session) {
        String assignedClass = (String) session.getAttribute("assignedClass");
        if (assignedClass == null) {
            return "redirect:/login";
        }
        Optional<Student> studentOpt = studentRepo.findById(id);
        if (studentOpt.isPresent() && assignedClass.equals(studentOpt.get().getAssignedClass())) {
            Student student = studentOpt.get();
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setRollNo(rollNo);
            studentRepo.save(student);
        }
        return "redirect:/attendance";
    }

    @PostMapping("/deleteStudent")
    public String deleteStudent(@RequestParam Long id, HttpSession session) {
        String assignedClass = (String) session.getAttribute("assignedClass");
        if (assignedClass == null) {
            return "redirect:/login";
        }
        Optional<Student> studentOpt = studentRepo.findById(id);
        if (studentOpt.isPresent() && assignedClass.equals(studentOpt.get().getAssignedClass())) {
            studentRepo.deleteById(id);
        }
        return "redirect:/attendance";
    }
}


