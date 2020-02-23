package com.blazemeter.demo.service;

import com.blazemeter.demo.document.Course;
import com.blazemeter.demo.document.Grade;
import com.blazemeter.demo.document.Student;
import com.blazemeter.demo.exception.CourseNotFoundException;
import com.blazemeter.demo.exception.GradeNotFoundException;
import com.blazemeter.demo.exception.StudentNotFoundException;
import com.blazemeter.demo.repository.CourseRepository;
import com.blazemeter.demo.repository.GradeRepository;
import com.blazemeter.demo.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class SchoolService {

	private static final Logger log = LoggerFactory.getLogger(SchoolService.class);

	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;
	private final GradeRepository gradeRepository;
	private final MongoTemplate mongoTemplate;

	@Autowired
	SchoolService(StudentRepository studentRepository, CourseRepository courseRepository, GradeRepository gradeRepository,
				  MongoTemplate mongoTemplate) {
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
		this.gradeRepository = gradeRepository;
		this.mongoTemplate = mongoTemplate;
	}

	@Cacheable(value = "students", key = "#email", unless = "#result == null")
	public Student findStudentByEmail(String email) throws StudentNotFoundException {
		log.info("Finding Student by email :{}", email);
		return studentRepository.findStudentByEmail(email)
				.orElseThrow(() -> new StudentNotFoundException("Student with email " + email + " NotFound!"));
	}

	@CachePut(value = "student", key = "#email")
	public Student saveStudent(Student student) {
		log.info("Saving student :{}", student.toString());
		return studentRepository.save(student);
	}

	@CacheEvict(value = "student", key = "#email")
	public void deleteStudent(String mail) throws StudentNotFoundException {
		log.info("deleting Student by id :{}", mail);

		Student student = findStudentByEmail(mail);
		studentRepository.delete(student);

		//delete grades referencing to this student
		Query query = new Query();
		query.fields().elemMatch("student", Criteria.where("$id").is(student.getStudentId()));
		mongoTemplate.findAllAndRemove(query, Grade.class);

		//delete references from courses
		query = Query.query(Criteria.where("$id").is(student.getStudentId()));
		Update update = new Update().pull("students", query);
		mongoTemplate.updateMulti(new Query(), update, Course.class);
	}

	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	public Course findCourseByName(String courseName) throws CourseNotFoundException {
		log.info("Finding Course by name :{}", courseName);
		return courseRepository.findCourseByName(courseName)
				.orElseThrow(() -> new CourseNotFoundException("Course with the name " + courseName + " NotFound!"));
	}

	public Course saveCourse(String courseName) {
		log.info("Saving course :{}", courseName);
		Course course = new Course();
		course.setName(courseName);
		return courseRepository.save(course);
	}

	public void deleteCourseByName(String courseName) throws CourseNotFoundException {
		log.info("deleting Course by name {}", courseName);
		Course course = findCourseByName(courseName);
		courseRepository.delete(course);

		//delete grades referencing to this student
		Query query = new Query();
		query.fields().elemMatch("course", Criteria.where("$id").is(course.getCourseId()));
		mongoTemplate.findAllAndRemove(query, Grade.class);
	}

	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}


	public Course addStudentToCourse(String courseName, String studentEmail) throws StudentNotFoundException, CourseNotFoundException {
		log.info("Adding student {} to course :{}", studentEmail, courseName);
		Student student = findStudentByEmail(studentEmail);
		Course course = findCourseByName(courseName);
		course.getStudents().add(student);
		return courseRepository.save(course);
	}

	public Course removeStudentFromCourse(String courseName, String studentEmail) throws StudentNotFoundException, CourseNotFoundException {
		log.info("Removing student {} from course :{}", studentEmail, courseName);
		Student student = findStudentByEmail(studentEmail);
		Course course = findCourseByName(courseName);
		course.getStudents().remove(student);
		return courseRepository.save(course);
	}

	public Grade findGradeByStudentEmailAndCourseName(String courseName, String studentEmail) throws GradeNotFoundException, CourseNotFoundException, StudentNotFoundException {
		log.info("Finding Grade by courseName {} and studentEmail {}", courseName, studentEmail);

		Student student = findStudentByEmail(studentEmail);
		Course course = findCourseByName(courseName);
		return gradeRepository.findGradeByStudentAndCourse(student, course)
				.orElseThrow(() -> new GradeNotFoundException("Grade with the name " + courseName + " NotFound!"));
	}

	public Grade saveGrade(Long grade, String courseName, String studentEmail) throws StudentNotFoundException, CourseNotFoundException {
		log.info("Saving grade :{}", grade);
		//saved by reference
		Student studentRef = findStudentByEmail(studentEmail);
		Course courseRef = findCourseByName(courseName);
		Grade gradeObj = new Grade();
		gradeObj.setStudent(studentRef);
		gradeObj.setCourse(courseRef);
		gradeObj.setGrade(grade);
		return gradeRepository.save(gradeObj);
	}

	public void deleteByStudentEmailAndCourseName(String courseName, String studentEmail) throws CourseNotFoundException, StudentNotFoundException {
		Student student = findStudentByEmail(studentEmail);
		Course course = findCourseByName(courseName);
		gradeRepository.deleteGradeByCourseAndStudent(course, student);
	}
}
