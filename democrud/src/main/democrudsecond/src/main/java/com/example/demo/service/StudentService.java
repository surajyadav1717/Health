package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.demo.dto.StudentDto;
import com.example.demo.entity.Student;

@Service
public interface StudentService {
	
	
	public Student saveDetials(StudentDto studentDto);
	
	public List<Student> getList();
	
	public void deleteById(int id);
}
