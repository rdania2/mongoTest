package com.blazemeter.demo.repository;

import com.blazemeter.demo.document.Course;
import com.blazemeter.demo.document.Grade;
import com.blazemeter.demo.document.Student;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GradeRepository extends MongoRepository<Grade, String> {

	Optional<Grade> findGradeByStudentAndCourse(Student student, Course course);

	Optional<Grade> deleteGradeByCourseAndStudent(Course course, Student student);

}
