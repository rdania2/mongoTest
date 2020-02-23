package com.blazemeter.demo.repository;

import com.blazemeter.demo.document.Course;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {

	Optional<Course> findCourseByCourseId(ObjectId courseId);

	Optional<Course> findCourseByName(String courseName);

}
