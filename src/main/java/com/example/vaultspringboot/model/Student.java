package com.example.vaultspringboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity(name = "students")
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "firstname", length = 20, nullable = false)
    private String firstName;

    @Column(name = "lastname", length = 20, nullable = false)
    private String lastName;
    
    private int age;
}
