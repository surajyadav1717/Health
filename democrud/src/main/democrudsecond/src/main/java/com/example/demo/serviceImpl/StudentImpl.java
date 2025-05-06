package com.example.demo.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.StudentDto;
import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;

@Service
public class StudentImpl implements  StudentService{

	@Autowired
	private StudentRepository studentRepository;



	@Override
	public Student saveDetials(StudentDto studentDto) {

		Student student = new Student();

		//student.setStudentId(studentDto.getStudentId());
		student.setStudentFirstName(studentDto.getStudentFirstName());
		student.setStudentLastName(studentDto.getStudentLastName());
		student.setEmailId(studentDto.getEmailId());
		student.setContactNumber(studentDto.getContactNumber());
		student.setAddress(studentDto.getAddress());
		student.setAlternateAdress(studentDto.getAlternateAdress());
		student.setArea(studentDto.getArea());
		student.setCity(studentDto.getCity());
		student.setState(studentDto.getState());
		student.setPincode(studentDto.getPincode());

		System.out.println(student+"=====");

		System.out.println("Data Saved ---");
		return studentRepository.save(student);

	}



	@Override
	public List<Student> getList() {

		return studentRepository.findAll();
	}



	@Override
	public void deleteById(int id) {

		StudentRepository deleteId = studentRepository;

		deleteId.deleteById(id);

	}

}
