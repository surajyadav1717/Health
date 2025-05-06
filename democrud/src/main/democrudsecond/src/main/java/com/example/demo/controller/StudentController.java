package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.StudentDto;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;

@RestController
public class StudentController {

	@Autowired
	private StudentService studentService;


	@PostMapping("/addstudent")
	public void createStudent(@RequestBody  StudentDto studentDto ) {

		System.err.println("Date Added ");
		studentService.saveDetials(studentDto);

	}


	@GetMapping("/fetchall")
	public List<Student> getAllList() {

		return studentService.getList();

	}


	@DeleteMapping("/{id}")

	public void deleteData(@PathVariable Integer id ) {


		studentService.deleteById(id);

	}





}
