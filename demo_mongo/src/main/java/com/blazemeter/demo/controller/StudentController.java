package com.blazemeter.demo.controller;

import com.blazemeter.demo.document.Student;
import com.blazemeter.demo.exception.StudentNotFoundException;
import com.blazemeter.demo.service.SchoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
	private final static Logger log = LoggerFactory.getLogger(StudentController.class);

	private final SchoolService schoolService;

	@Autowired
    StudentController(SchoolService schoolService){
		this.schoolService = schoolService;
	}

	@GetMapping("/findStudentByEmail/{email}")
	public Student findStudentByEmail(@PathVariable String email) throws StudentNotFoundException {
		return schoolService.findStudentByEmail(email);
	}

	@PostMapping("/saveStudent")
	public Student saveStudent(@Valid @RequestBody Student student) {
		return this.schoolService.saveStudent(student);
	}

	@DeleteMapping("/deleteStudentByEmail/{email}")
	public ResponseEntity<String> deleteStudentById(@PathVariable("email") String email) throws StudentNotFoundException {
		schoolService.deleteStudent(email);
		return ResponseEntity.accepted().body("Student with email " + email + " deleted");
	}

	@GetMapping("/getAllStudents")
	public List<Student> getAllStudents() {
		return schoolService.getAllStudents();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private void exceptionHandler(Exception ex) {
		log.error("StudentController : exception {}", ex);
	}
}
