package com.froebelenglishschool.attendance_management;

import com.froebelenglishschool.attendance_management.entity.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfig {

    //TESTING BEAN
    @Bean
    public Student student(){
        Student student = new Student();
        student.setFirstName("TestFname");
        student.setLastName("TestLname");
        student.setRollNo(56);
        student.setAssignedClass("Test");
        return student;
    }
}
