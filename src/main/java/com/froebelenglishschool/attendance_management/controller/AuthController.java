package com.froebelenglishschool.attendance_management.controller;

import ch.qos.logback.core.model.Model;
import com.froebelenglishschool.attendance_management.entity.Teacher;
import com.froebelenglishschool.attendance_management.repository.TeacherRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private TeacherRepository teacherRepo;
    //private = who is allowed to access data and methods
    /* or private means the variable or method can be accessed only inside its own class
     TeacherRepository = data type, teacherRepo = variable
     so that you can create object after declaring variable like.. teacherRepo = new TeacherRepositry()..which spring does it
       this process is also called Dependency Injection
       injects repository object into this variable and now controller can use it
       basically : creates a private variable that will hold the repository object used to interact with the database.*/

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // Thymeleaf template
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {

        var optTeacher = teacherRepo.findByEmailAndPassword(email, password);

        if (optTeacher.isPresent()) {
            Teacher teacher = optTeacher.get();
            session.setAttribute("teacherId", teacher.getId());
            session.setAttribute("assignedClass", teacher.getAssignedClass());
            return "redirect:/attendance";
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}