package com.blazemeter.demo.controller;

import com.blazemeter.demo.document.Course;
import com.blazemeter.demo.exception.CourseNotFoundException;
import com.blazemeter.demo.exception.StudentNotFoundException;
import com.blazemeter.demo.service.SchoolService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
	private final static Logger log = LoggerFactory.getLogger(CourseController.class);

	private final SchoolService schoolService;

	@Autowired
    CourseController(SchoolService schoolService){
		this.schoolService = schoolService;
	}

	@GetMapping("/findCourseByName/{name}")
	public Course findCourseById(@PathVariable String name) throws CourseNotFoundException {
		return schoolService.findCourseByName(name);
	}

	@PostMapping("/saveCourse")
	public Course saveCourse(@Valid @RequestBody String courseName) {
		return this.schoolService.saveCourse(courseName);
	}

	@DeleteMapping("/deleteCourseByName/{name}")
	public ResponseEntity<String> deleteCourseById(@PathVariable("name") String name) throws CourseNotFoundException {
		schoolService.deleteCourseByName(name);
		return ResponseEntity.accepted().body("Course with name " + name + " deleted");
	}

	@GetMapping("/getAllCoursess")
	public List<Course> getAllCourses() {
		return schoolService.getAllCourses();
	}

	@PostMapping("/addStudentToCourseByEmail")
	public Course addStudentToCourseByEmail(@RequestParam String courseName,
                                     @RequestParam String studentEmail) throws CourseNotFoundException, StudentNotFoundException {
		return schoolService.addStudentToCourse(courseName, studentEmail);
	}

	@DeleteMapping("/removeStudentFromCourseByEmail")
	public Course removeStudentFromCourseByEmail(@RequestParam String courseName,
										  @RequestParam String studentEmail)
			throws CourseNotFoundException, StudentNotFoundException {
		return schoolService.removeStudentFromCourse(courseName, studentEmail);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private void ExceptionHandler(Exception ex) {
		log.error("CourseController : exception {}", ex);
	}

}
