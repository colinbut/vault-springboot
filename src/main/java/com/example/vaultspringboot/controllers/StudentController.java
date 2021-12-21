package com.example.vaultspringboot.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.vaultspringboot.exceptions.StudentNotFoundException;
import com.example.vaultspringboot.model.Student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students/")
public class StudentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);
    private List<Student> students;

    public StudentController(){
        students = new ArrayList<>();
        init();
    }

    private void init() {
        Student colin = new Student();
        colin.setFirstName("Colin");
        colin.setLastName("But");
        colin.setAge(32);
        students.add(colin);
    }

    @GetMapping("/")
    public List<Student> getStudents(){
        return students;
    }

    @GetMapping("/{firstname}")
    public Student getStudent(@PathVariable("firstname") String firstname){
        LOGGER.info("Retrieving student with name: {}", firstname);
        Optional<Student> studentOpt = students.stream().filter(student -> student.getFirstName().equalsIgnoreCase(firstname)).findFirst();
        if (!studentOpt.isPresent()) {
            throw new StudentNotFoundException();
        } 
        return studentOpt.get();
    }
}
