package com.blazemeter.demo.controller;

import com.blazemeter.demo.document.Grade;
import com.blazemeter.demo.exception.CourseNotFoundException;
import com.blazemeter.demo.exception.GradeNotFoundException;
import com.blazemeter.demo.exception.StudentNotFoundException;
import com.blazemeter.demo.service.SchoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/grade")
public class GradeController {
	private final static Logger log = LoggerFactory.getLogger(GradeController.class);

	private final SchoolService schoolService;

	@Autowired
    GradeController(SchoolService schoolService){
		this.schoolService = schoolService;
	}

	@GetMapping("/findGrade/{courseName}/{studentEmail}")
	public Long findGrade(@NotNull @PathVariable String courseName,
								 @NotNull @PathVariable String studentEmail) throws GradeNotFoundException, CourseNotFoundException, StudentNotFoundException {
		return schoolService.findGradeByStudentEmailAndCourseName(courseName, studentEmail).getGrade();
	}

	@PostMapping("/saveGrade")
	public Grade saveGrade(@NotNull @RequestParam Long grade,
						   @NotNull @RequestParam String courseName,
						   @NotNull @RequestParam String studentEmail) throws CourseNotFoundException, StudentNotFoundException {
		return schoolService.saveGrade(grade, courseName, studentEmail);
	}

	@DeleteMapping("/deleteGrade")
	public ResponseEntity<String> deleteGrade(@NotNull @RequestParam String courseName,
											  @NotNull @RequestParam String studentEmail) throws CourseNotFoundException, StudentNotFoundException {
		schoolService.deleteByStudentEmailAndCourseName(courseName, studentEmail);
		return ResponseEntity.accepted().body("Grade with courseName" + courseName + " and studentEmail " + studentEmail + " deleted");
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private void ExceptionHandler(Exception ex) {
		log.error("GradeController : exception {}", ex);
	}
}
